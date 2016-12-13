package io.intino.pandora.plugin.codegeneration.server.jms.channel;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Channel;
import io.intino.pandora.model.Schema;
import io.intino.pandora.plugin.codegeneration.action.ChannelActionRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class SubscriptionModelRenderer {
	private final Project project;
	private final List<Channel> channels;
	private File src;
	private File gen;
	private String packageName;
	private final String boxName;

	public SubscriptionModelRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		channels = graph.find(Channel.class);
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		channels.forEach(this::processModel);
	}

	private void processModel(Channel channel) {
		final Frame frame = new Frame().addTypes("subscription").
				addSlot("type", "Consumer").
				addSlot("package", packageName).
				addSlot("box", boxName).
				addSlot("name", channel.name());
		if (!channel.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		Commons.writeFrame(new File(gen, "subscriptions"), snakeCaseToCamelCase(channel.name()) + "Subscription", template().format(frame));
		createCorrespondingAction(channel);
	}


	private void createCorrespondingAction(Channel channel) {
		new ChannelActionRenderer(project, channel, src, packageName, boxName).execute();
	}

	private Template template() {
		Template template = SubscriptionModelTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
