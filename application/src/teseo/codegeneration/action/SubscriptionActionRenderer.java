package teseo.codegeneration.action;

import org.siani.itrules.model.Frame;
import teseo.SubscriptionModel;

import java.io.File;

import static teseo.helpers.Commons.writeFrame;

public class SubscriptionActionRenderer extends ActionRenderer {
	private final SubscriptionModel model;


	public SubscriptionActionRenderer(SubscriptionModel model, File destiny, String packageName) {
		super(destiny, packageName);
		this.model = model;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", model.name());
		frame.addSlot("package", packageName);
		setupMessage(model.message(), frame);
		frame.addSlot("returnType", "void");
		if (!alreadyRendered(destiny, model.name()))
			writeFrame(destinyPackage(destiny), model.name() + "Action", template().format(frame));
	}

	private void setupMessage(SubscriptionModel.Message message, Frame frame) {
		frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", "message").addSlot("type", formatType(message.asType())));
	}

}
