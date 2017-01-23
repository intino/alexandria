package io.intino.pandora.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Activity;
import io.intino.pandora.builder.helpers.Commons;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;

public class UIActionRenderer extends ActionRenderer {

	private final Activity.AbstractPage page;

	public UIActionRenderer(Project project, Activity.AbstractPage page, File src, String packageName, String boxName) {
		super(project, src, packageName, boxName);
		this.page = page;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", page.name());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		frame.addSlot("returnType", "String");
		frame.addSlot("parameter", (AbstractFrame[]) parameters());
		frame.addSlot("ui", "");
		if (!alreadyRendered(destiny, page.name()))
			Commons.writeFrame(destinyPackage(destiny), page.name() + "Action", template().format(frame));
		//TODO else Update
	}

	private Frame[] parameters() {
		final Frame[] frames = new Frame[2];
		frames[0]= new Frame().addTypes("parameter").addSlot("type", "io.intino.pandora.server.activity.services.push.ActivitySession").addSlot("name", "session");
		frames[1]= new Frame().addTypes("parameter").addSlot("type", "String").addSlot("name", "clientId");
		return frames;
	}

}
