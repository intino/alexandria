package io.intino.pandora.model.codegeneration.server.jms.channel;

import io.intino.pandora.model.Channel;
import io.intino.pandora.model.Queue;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class ChannelRenderer {
	private final List<Channel> channels;
	private final File gen;
	private final String packageName;
	private final String boxName;

	public ChannelRenderer(Graph graph, File gen, String packageName, String boxName) {
		channels = graph.find(Channel.class);
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
				addSlot("subscription", subscriptions(channel));
		if (!channel.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		Commons.writeFrame(gen, snakeCaseToCamelCase(channel.name()) + "Channel", template().format(frame));
	}

	private Frame subscriptions(Channel channel) {
		final Frame frame = new Frame().addTypes("subscription").
				addSlot("path", channel.path()).
				addSlot("type", channel.is(Queue.class) ? "Queue" : "Topic").
				addSlot("name", channel.name());
		if (channel.isDurable()) frame.addSlot("durable", channel.name());
		return frame;
	}

	private Template template() {
		Template template = ChannelTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
