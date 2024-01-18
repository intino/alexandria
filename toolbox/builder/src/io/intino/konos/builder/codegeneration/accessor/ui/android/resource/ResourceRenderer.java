package io.intino.konos.builder.codegeneration.accessor.ui.android.resource;

import cottons.utils.StringHelper;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.accessor.ui.android.templates.PageTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.Display;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Service;
import io.intino.konos.model.Template;

import java.io.File;

import static cottons.utils.StringHelper.camelCaseToSnakeCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.hasAbstractClass;

public class ResourceRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceRenderer {

	public ResourceRenderer(CompilationContext compilationContext, Service.UI.Resource resource) {
		super(compilationContext, resource, Target.Android);
	}

	@Override
	public void render() {
		if (!resource.isPage()) return;
		writeActivity(buildFrame());
		writeActivityTemplate(buildFrame());
	}

	private void writeActivity(FrameBuilder builder) {
		builder.add("activity");
		File file = Commons.kotlinFile(src(target), File.separator + "pages" + File.separator + firstUpperCase(resource.name$()) + "Activity");
		file.getParentFile().mkdirs();
		Commons.write(file.toPath(), setup(new PageTemplate()).render(builder.toFrame()));
	}

	private void writeActivityTemplate(FrameBuilder builder) {
		builder.add("template");
		File file = new File(res(target) + File.separator + "layout" + File.separator + StringHelper.camelCaseToSnakeCase(resource.name$()).replace("-", "_") + "_activity.xml");
		file.getParentFile().mkdirs();
		Commons.write(file.toPath(), setup(new PageTemplate()).render(builder.toFrame()));
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder result = super.buildFrame().add("resource");
		Service.UI uiService = resource.core$().ownerAs(Service.UI.class);
		result.add("name", resource.name$());
		if (resource.isMain()) result.add("main");
		Template template = KonosGraph.templateFor(resource);
		result.add("url", uiService.url());
		result.add("path", resource.path());
		result.add("pageDisplay", template.name$());
		result.add("pageDisplayId", shortId(template));
		result.add("pageDisplayType", typeOf(template));
		uiService.useList().forEach(use -> result.add("accessibleImport", accessibleImportFrame(use)));
		addPageDisplayOrigin(result, template);
		return result;
	}

	private void addPageDisplayOrigin(FrameBuilder builder, Display display) {
		FrameBuilder originFrame = new FrameBuilder();
		if (ElementHelper.isRoot(display)) originFrame.add("decorated", "");
		if (ElementHelper.isRoot(display) && hasAbstractClass(display, Target.Android)) originFrame.add("hasAbstract", "");
		builder.add("pageDisplayOrigin", originFrame);
	}

	private Frame accessibleImportFrame(Service.UI.Use use) {
		FrameBuilder result = new FrameBuilder("accessibleImport");
		result.add("name", use.name$());
		result.add("url", use.url());
		result.add("elements", camelCaseToSnakeCase(use.service()));
		result.add("service", use.service());
		return result.toFrame();
	}

}