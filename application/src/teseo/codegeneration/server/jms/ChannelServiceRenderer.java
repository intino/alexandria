package teseo.codegeneration.server.jms;

import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Channel;
import teseo.InputChannel;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.writeFrame;

public class ChannelServiceRenderer {
	private final List<Channel> channels;
	private final File gen;
	private final String packageName;

	public ChannelServiceRenderer(Graph graph, File gen, String packageName) {
		channels = graph.find(Channel.class);
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("channels").
				addSlot("package", packageName).
				addSlot("channel", (AbstractFrame[]) processChannels());
		writeFrame(gen, "Channels", template().format(frame));
	}

	private Frame[] processChannels() {
		return channels.stream().map(this::processChannel).toArray(Frame[]::new);
	}

	private Frame processChannel(Channel channel) {
		final Frame frame = new Frame().addTypes("channel", channel instanceof InputChannel ? "input" : "output").
				addSlot("name", channel.name()).
				addSlot("path", channel.path()).
				addSlot("type", channel.isQueue() ? "Queue" : "Topic").
				addSlot("message", channel.message().asType().type());
		return channel instanceof InputChannel && ((InputChannel) channel).timeout() > 0 ? frame.addTypes("timeout").addSlot("timeout", ((InputChannel) channel).timeout()) : frame;
	}

	private Template template() {
		Template template = ChannelServiceTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
