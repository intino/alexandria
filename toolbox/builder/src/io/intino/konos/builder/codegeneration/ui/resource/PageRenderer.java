package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.ActionRenderer;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.model.graph.Service;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.format;
import static io.intino.konos.builder.helpers.Commons.extractUrlPathParameters;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.KonosGraph.templateFor;

public class PageRenderer extends ActionRenderer {

	private final Settings settings;
	private final Service.UI.Resource resource;
	private final Service.UI service;

	public PageRenderer(Settings settings, Service.UI.Resource resource) {
		super(settings, "ui");
		this.settings = settings;
		this.resource = resource;
		this.service = resource.core$().ownerAs(Service.UI.class);
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder().add("action").add("ui");
		Service.UI uiService = resource.core$().ownerAs(Service.UI.class);
		builder.add("name", resource.name$());
		builder.add("uiService", uiService.name$());
		builder.add("package", packageName());
		builder.add("box", boxName());
		builder.add("importTemplates", packageName());
		builder.add("component", componentFrame());
		builder.add("parameter", parameters());
		builder.add("contextProperty", contextPropertyFrame());
		service.useList().forEach(use -> builder.add("usedUnit", usedUnitFrame(use)));
		if (service.favicon() != null) builder.add("favicon", service.favicon());
		else if (service.title() != null) builder.add("title", service.title());
		settings.classes().put(resource.getClass().getSimpleName() + "#" + firstUpperCase(resource.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(resource.name$())) + suffix());
		if (!alreadyRendered(src(), resource.name$()))
			writeFrame(destinyPackage(src()), resource.name$() + suffix(), template().render(builder.toFrame()));
		writeFrame(destinyPackage(gen()), "Abstract" + firstUpperCase(resource.name$()) + suffix(), template().render(builder.add("gen").toFrame()));
	}

	@Override
	protected ContextType contextType() {
		return ContextType.Spark;
	}

	private Frame usedUnitFrame(Service.UI.Use use) {
		FrameBuilder result = new FrameBuilder("usedUnit");
		result.add(isCustom(use.url()) ? "custom" : "standard");
		result.add("name", use.name$());
		result.add("url", isCustom(use.url()) ? customValue(use.url()) : use.url());
		return result.toFrame();
	}

	private FrameBuilder componentFrame() {
		return new FrameBuilder("component").add("value", templateFor(resource).name$());
	}

	private FrameBuilder[] parameters() {
		List<String> parameters = extractUrlPathParameters(resource.path());
		return parameters.stream().map(parameter -> new FrameBuilder().add("parameter")
				.add("type", "String")
				.add("name", parameter)).toArray(FrameBuilder[]::new);
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	private String customValue(String value) {
		return value != null ? value.substring(1, value.length() - 1) : "";
	}

	@Override
	protected File destinyPackage(File destiny) {
		return new File(destiny, format(CodeGenerationHelper.Pages, Target.Owner));
	}

	@Override
	protected String suffix() {
		return "Page";
	}
}
