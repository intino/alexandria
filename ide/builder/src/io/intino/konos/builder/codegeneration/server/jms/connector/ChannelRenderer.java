package io.intino.konos.builder.codegeneration.server.jms.connector;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.action.ChannelActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Bus.Channel;
import io.intino.konos.model.Bus.Queue;
import io.intino.konos.model.Schema;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class ChannelRenderer {
	private final Project project;
	private final List<Channel> channels;
	private final File src;
	private final File gen;
	private final String packageName;
	private final String boxName;

	public ChannelRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		channels = graph.find(Channel.class);
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		channels.forEach(this::processChannel);
	}

	private void processChannel(Channel channel) {
		Frame frame = new Frame().addTypes("channel").
				addSlot("package", packageName).
				addSlot("name", channel.name()).
				addSlot("box", boxName).
				addSlot("subscription", subscription(channel));
		if (!channel.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		Commons.writeFrame(new File(gen, "bus"), snakeCaseToCamelCase(channel.name()) + "Channel", template().format(frame));
		createCorrespondingAction(channel);
	}

	private void createCorrespondingAction(Channel channel) {
		new ChannelActionRenderer(project, channel, src, packageName, boxName).execute();
	}

	private Frame subscription(Channel channel) {
		final Frame frame = new Frame().addTypes("subscription").
				addSlot("path", customize(channel.path())).
				addSlot("type", channel.is(Queue.class) ? "Queue" : "Topic").
				addSlot("name", channel.name());
		if (channel.isDurable()) frame.addSlot("durable", customizeDurable(channel.asDurable().clientID()));
		return frame;
	}

	private Frame customize(String path) {
		Frame frame = new Frame().addTypes("path");
		frame.addSlot("name", path);
		for (String parameter : Commons.extractParameters(path)) frame.addSlot("custom", parameter);
		return frame;
	}

	private Frame customizeDurable(String clientId) {
		Frame frame = new Frame().addTypes("durable");
		frame.addSlot("value", "");
		for (String parameter : Commons.extractParameters(clientId)) frame.addSlot("custom", parameter);
		return frame;
	}

	private Template template() {
		return Formatters.customize(ChannelTemplate.create());
	}
}
