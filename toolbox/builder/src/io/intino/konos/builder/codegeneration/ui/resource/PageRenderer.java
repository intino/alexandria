package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.ActionRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.model.Service;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.format;
import static io.intino.konos.builder.helpers.Commons.*;
import static io.intino.konos.model.KonosGraph.templateFor;

public class PageRenderer extends ActionRenderer {

	private final CompilationContext compilationContext;
	private final Service.UI.Resource resource;
	private final Service.UI service;

	public PageRenderer(CompilationContext compilationContext, Service.UI.Resource resource) {
		super(compilationContext, "ui");
		this.compilationContext = compilationContext;
		this.resource = resource;
		this.service = resource.core$().ownerAs(Service.UI.class);
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder().add("action").add("ui");
		Service.UI uiService = resource.core$().ownerAs(Service.UI.class);
		builder.add("name", resource.name$());
		if (resource.isPage()) builder.add("templateName", resource.asPage().template().name$());
		builder.add("uiService", uiService.name$());
		builder.add("package", packageName());
		builder.add("box", boxName());
		builder.add("importTemplates", packageName());
		builder.add("component", componentFrame());
		builder.add("parameter", parameters());
		builder.add("contextProperty", contextPropertyFrame());
		service.useList().forEach(use -> builder.add("usedUnit", usedUnitFrame(use)));
		if (service.title() != null) builder.add("title", titleFrame());
		if (service.favicon() != null) builder.add("favicon", service.favicon());
		compilationContext.classes().put(resource.getClass().getSimpleName() + "#" + firstUpperCase(resource.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(resource.name$())) + suffix());
		if (!alreadyRendered(src(), resource.name$())) {
			writeFrame(destinationPackage(src()), resource.name$() + suffix(), template().render(builder.toFrame()));
			if (target.equals(Target.Owner))
				context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(destinationPackage(src()), resource.name$() + suffix()).getAbsolutePath()));
		}
		writeFrame(destinationPackage(gen()), "Abstract" + firstUpperCase(resource.name$()) + suffix(), template().render(builder.add("gen").toFrame()));
		if (target.equals(Target.Owner))
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(destinationPackage(gen()), "Abstract" + firstUpperCase(resource.name$()) + suffix()).getAbsolutePath()));
	}

	private FrameBuilder titleFrame() {
		FrameBuilder result = new FrameBuilder("title");
		String title = service.title();
		if (title.startsWith("{") && title.endsWith("}")) {
			title = title.substring(1, title.length()-1);
			result.add("configuration");
		}
		result.add("title", title);
		return result;
	}

	@Override
	protected ContextType contextType() {
		return ContextType.Spark;
	}

	private Frame usedUnitFrame(Service.UI.Use use) {
		FrameBuilder result = new FrameBuilder("usedUnit");
		result.add(isCustomParameter(use.url()) ? "custom" : "standard");
		result.add("name", use.name().toLowerCase());
		result.add("url", isCustomParameter(use.url()) ? customParameterValue(use.url()) : use.url());
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

	@Override
	protected File destinationPackage(File destiny) {
		return new File(destiny, format(CodeGenerationHelper.Pages, Target.Owner));
	}

	@Override
	protected String suffix() {
		return "Page";
	}
}
