package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.extractUrlPathParameters;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.KonosGraph.componentFor;

public class UIActionRenderer extends ActionRenderer {

	private final UIService.Resource resource;
	private final File gen;
	private final UIService service;
	private final Map<String, String> classes;

	public UIActionRenderer(Project project, UIService.Resource resource, File src, File gen, String packageName, String boxName, Map<String, String> classes) {
		super(project, src, packageName, boxName);
		this.gen = gen;
		this.resource = resource;
		this.service = resource.core$().ownerAs(UIService.class);
		this.classes = classes;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action", "ui");
		frame.addSlot("name", resource.name$());
		frame.addSlot("uiService", resource.core$().ownerAs(UIService.class).name$());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		frame.addSlot("type", resource.isEditorPage() ? "Editor" : "Resource");
		frame.addSlot("importDialogs", packageName);
		frame.addSlot("importDisplays", packageName);
		if (resource.isEditorPage()) frame.addSlot("editor", new Frame("editor"));
		frame.addSlot("component", componentFrame());
		frame.addSlot("parameter", parameters());
		service.useList().stream().map(use -> frame.addSlot("usedAppUrl", new Frame("usedAppUrl", isCustom(use.url()) ? "custom" : "standard").addSlot("value", isCustom(use.url()) ? customValue(use.url()) : use.url()))).collect(Collectors.toList());
		if (service.favicon() != null) frame.addSlot("favicon", service.favicon());
		else if (service.title() != null) frame.addSlot("title", service.title());
		classes.put(resource.getClass().getSimpleName() + "#" + firstUpperCase(resource.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(resource.name$())) + "Action");
		if (!alreadyRendered(destiny, resource.name$()))
			writeFrame(destinyPackage(destiny), resource.name$() + "Action", template().format(frame));
		writeFrame(destinyPackage(gen), "Abstract" + firstUpperCase(resource.name$()) + "Action", template().format(frame.addTypes("gen")));
	}

	private Frame componentFrame() {
		Frame result = new Frame("component").addSlot("value", componentFor(resource).name$());
		if (resource.isEditorPage()) {
			Component display = resource.asEditorPage().editor().display();
			result.addSlot("editor", new Frame("editor").addSlot("display", display.name$()));
		}
		return result;
	}

	private Frame[] parameters() {
		List<String> parameters = extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("type", "String")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	private String customValue(String value) {
		return value != null ? value.substring(1, value.length() - 1) : "";
	}

}
