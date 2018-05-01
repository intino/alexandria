package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.Commons.extractUrlPathParameters;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class UIActionRenderer extends ActionRenderer {

	private final UIService.Resource resource;
	private final File gen;
	private final UIService service;

	public UIActionRenderer(Project project, UIService.Resource resource, File src, File gen, String packageName, String boxName) {
		super(project, src, packageName, boxName);
		this.gen = gen;
		this.resource = resource;
		this.service = resource.core$().ownerAs(UIService.class);
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action", "resource");
		frame.addSlot("name", resource.name$());
		frame.addSlot("uiService", resource.core$().ownerAs(UIService.class).name$());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		if (resource.uses().i$(Dialog.class)) frame.addSlot("importDialogs", packageName);
		else frame.addSlot("importDisplays", packageName);
		frame.addSlot("component", resource.uses().name$());
		frame.addSlot("parameter", parameters());
		if (service.favicon() != null) frame.addSlot("favicon", service.favicon());
		else if (service.title() != null) frame.addSlot("title", service.title());
		if (!alreadyRendered(destiny, resource.name$())) writeFrame(destinyPackage(destiny), resource.name$() + "Action", template().format(frame));
		writeFrame(destinyPackage(gen), "Abstract" + firstUpperCase(resource.name$()) + "Action", template().format(frame.addTypes("gen")));
	}

	private Frame[] parameters() {
		List<String> parameters = extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("type", "String")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}

}
