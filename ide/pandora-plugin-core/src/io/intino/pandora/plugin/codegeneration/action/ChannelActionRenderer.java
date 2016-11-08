package io.intino.pandora.plugin.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.pandora.plugin.Channel;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.model.Frame;

import java.io.File;

public class ChannelActionRenderer extends ActionRenderer {
	private final Channel model;


	public ChannelActionRenderer(Project project, Channel model, File destiny, String packageName, String boxName) {
		super(project, destiny, packageName, boxName);
		this.model = model;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", model.name());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		setupMessage(model.message(), frame);
		frame.addSlot("returnType", "void");
		if (!alreadyRendered(destiny, model.name()))
			Commons.writeFrame(destinyPackage(destiny), model.name() + "Action", template().format(frame));
		//TODO else Update
	}

	private void setupMessage(Channel.Message message, Frame frame) {
		frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", "message").addSlot("type", formatType(message.asType())));
	}

}
