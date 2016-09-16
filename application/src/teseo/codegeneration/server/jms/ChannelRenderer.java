package teseo.codegeneration.server.jms;

import org.siani.itrules.Template;
import org.siani.itrules.engine.formatters.StringFormatter;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Channel;
import teseo.InputChannel;
import teseo.codegeneration.action.ChannelActionRenderer;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.writeFrame;

public class ChannelRenderer {
	private final List<InputChannel> channels;
	private File src;
	private File gen;
	private String packageName;

	public ChannelRenderer(Graph graph, File src, File gen, String packageName) {
		channels = graph.find(InputChannel.class);
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		channels.forEach(this::processChannel);
	}

	private void processChannel(InputChannel channel) {
		final Frame frame = new Frame().addTypes("channel").
				addSlot("type", "Consumer").
				addSlot("package", packageName).
				addSlot("name", channel.name()).
				addSlot("message", message(channel.message()));
		writeFrame(new File(gen, "channels"), StringFormatter.get().get("firstuppercase").format(channel.name()).toString() + "Channel", template().format(frame));
		createCorrespondingAction(channel);
	}


	private void createCorrespondingAction(InputChannel channel) {
		new ChannelActionRenderer(channel, src, packageName).execute();
	}

	private Frame message(Channel.Message message) {
		return new Frame().addTypes("message", message.asType().type(), message.asType().getClass().getSimpleName()).addSlot("type", message.asType().type());
	}

	private Template template() {
		Template template = ChannelTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
