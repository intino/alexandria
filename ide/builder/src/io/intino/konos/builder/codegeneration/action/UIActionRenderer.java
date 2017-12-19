package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Display;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class UIActionRenderer extends ActionRenderer {

	private final Activity.AbstractPage page;

	public UIActionRenderer(Project project, Activity.AbstractPage page, File src, String packageName, String boxName) {
		super(project, src, packageName, boxName);
		this.page = page;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action", "page");
		frame.addSlot("name", page.name$());
		frame.addSlot("activity", page.core$().ownerAs(Activity.class).name$());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		if (page.uses().i$(Dialog.class)) frame.addSlot("importDialogs", packageName);
		else frame.addSlot("importDisplays", packageName);
		frame.addSlot("ui", page.uses().name$() + suffix());
		frame.addSlot("parameter", parameters());
		if (!alreadyRendered(destiny, page.name$()))
			Commons.writeFrame(destinyPackage(destiny), page.name$() + "Action", template().format(frame));
		//TODO else Update
	}

	@NotNull
	private String suffix() {
		if (page.uses().i$(Dialog.class)) return Dialog.class.getSimpleName();
		else return page.uses().getClass().getSimpleName().equals(Display.class.getSimpleName()) ? Display.class.getSimpleName() : "";
	}

	private Frame[] parameters() {
		List<String> parameters = page.paths().stream().filter(path -> path.contains(":"))
				.map(Commons::extractUrlPathParameters).flatMap(Collection::stream).collect(toList());
		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("type", "String")
				.addSlot("name", parameter)).collect(toList()).toArray(new Frame[0]);
	}

}
