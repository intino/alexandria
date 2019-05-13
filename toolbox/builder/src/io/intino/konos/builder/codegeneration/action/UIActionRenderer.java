package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.ui.UIService;

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
		super(project, src, packageName, boxName, "ui");
		this.gen = gen;
		this.resource = resource;
		this.service = resource.core$().ownerAs(UIService.class);
		this.classes = classes;
	}

	public void execute() {
		FrameBuilder builder = new FrameBuilder("action", "ui");
		builder.add("name", resource.name$());
		builder.add("uiService", resource.core$().ownerAs(UIService.class).name$());
		builder.add("package", packageName);
		builder.add("box", boxName);
		builder.add("type", resource.isEditorPage() ? "Editor" : "Resource");
		builder.add("importDialogs", packageName);
		builder.add("importDisplays", packageName);
		if (resource.isEditorPage()) builder.add("editor", new FrameBuilder("editor"));
		builder.add("component", componentFrame());
		builder.add("parameter", parameters());
		service.useList().stream().map(use -> builder.add("usedAppUrl", new FrameBuilder("usedAppUrl", isCustom(use.url()) ? "custom" : "standard").add("value", isCustom(use.url()) ? customValue(use.url()) : use.url()))).collect(Collectors.toList());
		if (service.favicon() != null) builder.add("favicon", service.favicon());
		else if (service.title() != null) builder.add("title", service.title());
		classes.put(resource.getClass().getSimpleName() + "#" + firstUpperCase(resource.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(resource.name$())) + "Action");
		if (!alreadyRendered(destiny, resource.name$()))
			writeFrame(destinyPackage(destiny), resource.name$() + "Action", template().render(builder));
		writeFrame(destinyPackage(gen), "Abstract" + firstUpperCase(resource.name$()) + "Action", template().render(builder.add("gen")));
	}

	private Frame componentFrame() {
		FrameBuilder result = new FrameBuilder("component").add("value", componentFor(resource).name$());
		if (resource.isEditorPage()) {
			Component display = resource.asEditorPage().editor().display();
			result.add("editor", new FrameBuilder("editor").add("display", display.name$()).toFrame());
		}
		return result.toFrame();
	}

	private Frame[] parameters() {
		List<String> parameters = extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new FrameBuilder("parameter")
				.add("type", "String")
				.add("name", parameter).toFrame()).toArray(Frame[]::new);
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	private String customValue(String value) {
		return value != null ? value.substring(1, value.length() - 1) : "";
	}
}
