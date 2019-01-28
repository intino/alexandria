package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.action.ActionRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.extractUrlPathParameters;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.KonosGraph.displayFor;

public class PageRenderer extends ActionRenderer {

	private final Settings settings;
	private final UIService.Resource resource;
	private final UIService service;

	public PageRenderer(Settings settings, UIService.Resource resource) {
		super(settings.project(), settings.src(), settings.packageName(), settings.boxName(), "ui");
		this.settings = settings;
		this.resource = resource;
		this.service = resource.core$().ownerAs(UIService.class);
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action", "ui");
		frame.addSlot("name", resource.name$());
		frame.addSlot("uiService", resource.core$().ownerAs(UIService.class).name$());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		frame.addSlot("type", resource.isEditorPage() ? "Editor" : "Resource");

		if (resource.isBlankPage()) frame.addSlot("importRoots", packageName);
		else if (resource.isDesktopPage()) frame.addSlot("importDesktops", packageName);
		else if (resource.isEditorPage()) frame.addSlot("importEditors", packageName);

		if (resource.isEditorPage()) frame.addSlot("editor", new Frame("editor"));
		frame.addSlot("component", componentFrame());
		frame.addSlot("parameter", parameters());
		service.useList().stream().map(use -> frame.addSlot("usedAppUrl", new Frame("usedAppUrl", isCustom(use.url()) ? "custom" : "standard").addSlot("value", isCustom(use.url()) ? customValue(use.url()) : use.url()))).collect(Collectors.toList());
		if (service.favicon() != null) frame.addSlot("favicon", service.favicon());
		else if (service.title() != null) frame.addSlot("title", service.title());
		settings.classes().put(resource.getClass().getSimpleName() + "#" + firstUpperCase(resource.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(resource.name$())) + suffix());
		if (!alreadyRendered(destiny, resource.name$()))
			writeFrame(destinyPackage(destiny), resource.name$() + suffix(), template().format(frame));
		writeFrame(destinyPackage(settings.gen()), "Abstract" + firstUpperCase(resource.name$()) + suffix(), template().format(frame.addTypes("gen")));
	}

	private Frame componentFrame() {
		Frame result = new Frame("component").addSlot("value", displayFor(resource).name$());
		if (resource.isEditorPage()) {
			Display display = resource.asEditorPage().editor();
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

	@Override
	protected File destinyPackage(File destiny) {
		return new File(destiny, UIRenderer.format(UIRenderer.Pages, UIRenderer.Target.Service));
	}

	@Override
	protected String suffix() {
		return "Page";
	}
}
