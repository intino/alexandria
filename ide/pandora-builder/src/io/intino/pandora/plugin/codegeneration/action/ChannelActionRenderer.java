package io.intino.pandora.plugin.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Bus.Channel;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.model.Frame;

import java.io.File;

public class ChannelActionRenderer extends ActionRenderer {
	private final Channel channel;


	public ChannelActionRenderer(Project project, Channel model, File destiny, String packageName, String boxName) {
		super(project, destiny, packageName, boxName);
		this.channel = model;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", channel.name());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", "message").addSlot("type", "String"));
		frame.addSlot("returnType", "void");
		if (!alreadyRendered(destiny, channel.name()))
			Commons.writeFrame(destinyPackage(destiny), channel.name() + "Action", template().format(frame));
		//TODO else Update
	}
}
