package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Activity;
import io.intino.konos.model.Dialog;
import io.intino.konos.model.Display;
import org.siani.itrules.model.Frame;

import java.io.File;

public class UIActionRenderer extends ActionRenderer {

	private final Activity.AbstractPage page;

	public UIActionRenderer(Project project, Activity.AbstractPage page, File src, String packageName, String boxName) {
		super(project, src, packageName, boxName);
		this.page = page;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action", "page");
		frame.addSlot("name", page.name());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		if (page.uses().is(Dialog.class)) frame.addSlot("importDialogs", packageName);
		else frame.addSlot("importDisplays", packageName);
		frame.addSlot("ui", page.uses().name() + (page.uses().is(Dialog.class) ? Dialog.class.getSimpleName() : Display.class.getSimpleName()));
		if (!alreadyRendered(destiny, page.name()))
			Commons.writeFrame(destinyPackage(destiny), page.name() + "Action", template().format(frame));
		//TODO else Update
	}

}
