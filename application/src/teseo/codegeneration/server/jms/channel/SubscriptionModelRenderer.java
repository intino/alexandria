package teseo.codegeneration.server.jms.channel;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.SubscriptionModel;
import teseo.codegeneration.action.SubscriptionActionRenderer;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.writeFrame;

public class SubscriptionModelRenderer {
	private final List<SubscriptionModel> channels;
	private File src;
	private File gen;
	private String packageName;

	public SubscriptionModelRenderer(Graph graph, File src, File gen, String packageName) {
		channels = graph.find(SubscriptionModel.class);
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		channels.forEach(this::processModel);
	}

	private void processModel(SubscriptionModel subscription) {
		final Frame frame = new Frame().addTypes("subscription").
				addSlot("type", "Consumer").
				addSlot("package", packageName).
				addSlot("name", subscription.name()).
				addSlot("message", message(subscription.message()));
		writeFrame(new File(gen, "subscriptions"), snakeCaseToCamelCase(subscription.name()) + "Subscription", template().format(frame));
		createCorrespondingAction(subscription);
	}


	private void createCorrespondingAction(SubscriptionModel channel) {
		new SubscriptionActionRenderer(channel, src, packageName).execute();
	}

	private Frame message(SubscriptionModel.Message message) {
		return new Frame().addTypes("message", message.asType().type(), message.asType().getClass().getSimpleName()).addSlot("type", message.asType().type());
	}

	private Template template() {
		Template template = SubscriptionModelTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
