package teseo.codegeneration.server.jms.channel;

import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Channel;
import teseo.Queue;
import teseo.SubscriptionModel;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.writeFrame;

public class ChannelRenderer {
	private final List<Channel> channels;
	private final File gen;
	private final String packageName;

	public ChannelRenderer(Graph graph, File gen, String packageName) {
		channels = graph.find(Channel.class);
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		channels.forEach(this::processChannel);
	}

	private void processChannel(Channel channel) {
		Frame frame = new Frame().addTypes("channel").
				addSlot("package", packageName).
				addSlot("name", channel.name()).
				addSlot("subscription", (AbstractFrame[]) subscriptions(channel));
		writeFrame(gen, snakeCaseToCamelCase(channel.name()) + "Channel", template().format(frame));
	}

	private Frame[] subscriptions(Channel channel) {
		return channel.subscriptionModelList().stream().map(this::processSubscription).toArray(Frame[]::new);
	}

	private Frame processSubscription(SubscriptionModel model) {
		return new Frame().addTypes("subscription").
				addSlot("path", model.path()).
				addSlot("type", model.is(Queue.class) ? "Queue" : "Topic").
				addSlot("name", model.name());

	}

	private Template template() {
		Template template = ChannelTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
