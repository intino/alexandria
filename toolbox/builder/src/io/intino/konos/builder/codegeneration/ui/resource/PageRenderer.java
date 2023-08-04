package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.action.ActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.model.Service;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.format;
import static io.intino.konos.builder.helpers.Commons.*;
import static io.intino.konos.model.KonosGraph.templateFor;

public class PageRenderer extends ActionRenderer {

	private final CompilationContext compilationContext;
	private final Service.UI.Resource resource;
	private final Service.UI service;
	private Target target;

	public PageRenderer(CompilationContext compilationContext, Service.UI.Resource resource, Target target) {
		super(compilationContext, types(resource.core$().ownerAs(Service.UI.class)));
		this.compilationContext = compilationContext;
		this.resource = resource;
		this.service = resource.core$().ownerAs(Service.UI.class);
		this.target = target;
	}

	@Override
	public void render() {
		writeWebPage(buildFrame());
		if (isMobile(service)) writeMobilePage(buildFrame().add("mobile"));
	}

	private FrameBuilder buildFrame() {
		FrameBuilder builder = new FrameBuilder().add("action").add("ui");
		Service.UI uiService = resource.core$().ownerAs(Service.UI.class);
		builder.add("name", resource.name$());
		if (resource.isPage()) builder.add("templateName", resource.asPage().template().name$());
		if (resource.isAssetPage()) builder.add("asset");
		builder.add("returnType", returnTypeFrame());
		builder.add("executeBody", executeBodyFrame());
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
		return builder;
	}

	private void writeWebPage(FrameBuilder builder) {
		compilationContext.classes().put(resource.getClass().getSimpleName() + "#" + firstUpperCase(resource.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(resource.name$())) + suffix(Target.Server));
		if (!alreadyRendered(src(target), resource.name$())) {
			writeFrame(destinationPackage(src(target)), resource.name$() + suffix(target), template().render(builder.toFrame()));
			if (target.equals(Target.Server))
				context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(destinationPackage(src(target)), resource.name$() + suffix(target)).getAbsolutePath()));
		}
		writeFrame(destinationPackage(gen(target)), "Abstract" + firstUpperCase(resource.name$()) + suffix(target), template().render(builder.add("gen").toFrame()));
		if (target.equals(Target.Server))
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(destinationPackage(gen(target)), "Abstract" + firstUpperCase(resource.name$()) + suffix(target)).getAbsolutePath()));
	}

	private void writeMobilePage(FrameBuilder builder) {
		Target target = Target.MobileShared;
		compilationContext.classes().put(resource.getClass().getSimpleName() + "#" + firstUpperCase(resource.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(resource.name$())) + suffix(Target.MobileShared));
		if (!alreadyRendered(src(Target.Server), resource.name$(), target)) {
			writeFrame(destinationPackage(src(Target.Server)), resource.name$() + suffix(target), template().render(builder.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(destinationPackage(src(Target.Server)), resource.name$() + suffix(target)).getAbsolutePath()));
		}
		writeFrame(destinationPackage(gen(Target.Server)), "Abstract" + firstUpperCase(resource.name$()) + suffix(target), template().render(builder.add("gen").toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(destinationPackage(gen(Target.Server)), "Abstract" + firstUpperCase(resource.name$()) + suffix(target)).getAbsolutePath()));
	}

	private FrameBuilder executeBodyFrame() {
		FrameBuilder result = new FrameBuilder("executeBody");
		service.useList().forEach(use -> result.add("usedUnit", usedUnitFrame(use)));
		if (resource.isPage()) result.add("templateName", resource.asPage().template().name$());
		if (resource.isStaticPage()) {
			result.add("static");
			result.add("text", resource.asStaticPage().content());
		}
		if (resource.isAssetPage()) result.add("asset");
		return result;
	}

	private FrameBuilder returnTypeFrame() {
		FrameBuilder result = new FrameBuilder("returnType");
		if (resource.isAssetPage()) result.add("asset");
		return result;
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
		FrameBuilder result = new FrameBuilder("component");
		if (resource.isPage()) result.add("value", templateFor(resource).name$());
		if (resource.isStaticPage()) {
			result.add("static");
			result.add("text", resource.asStaticPage().content());
		}
		if (resource.isAssetPage()) result.add("asset");
		return result;
	}

	private FrameBuilder[] parameters() {
		List<String> parameters = extractUrlPathParameters(resource.path());
		parameters.addAll(resource.parameterList().stream().map(Layer::name$).collect(Collectors.toList()));
		return parameters.stream().map(parameter -> new FrameBuilder().add("parameter")
				.add("type", "String")
				.add("name", parameter)).toArray(FrameBuilder[]::new);
	}

	@Override
	protected File destinationPackage(File destiny) {
		return new File(destiny, format(CodeGenerationHelper.Pages, Target.Server));
	}

	@Override
	protected String suffix(Target target) {
		return target == Target.MobileShared ? "MobilePage" : "Page";
	}

	private static String[] types(Service.UI service) {
		List<String> result = new ArrayList<>();
		result.add("ui");
		return result.toArray(new String[0]);
	}

}
