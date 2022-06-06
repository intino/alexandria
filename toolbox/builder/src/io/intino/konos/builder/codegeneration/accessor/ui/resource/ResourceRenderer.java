package io.intino.konos.builder.codegeneration.accessor.ui.resource;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PageTemplate;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.Display;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Service;
import io.intino.konos.model.Template;

import java.io.File;

import static cottons.utils.StringHelper.camelCaseToSnakeCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public class ResourceRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceRenderer {

	public ResourceRenderer(CompilationContext compilationContext, Service.UI.Resource resource) {
		super(compilationContext, resource, Target.Accessor);
	}

	@Override
	public void render() {
		if (!resource.isPage()) return;
		writeHtml(buildFrame());
		writeJavascript(buildFrame());
	}

	private void writeHtml(FrameBuilder builder) {
		builder.add("html");
		File file = new File(src() + File.separator + resource.asPage().template().name$() + ".html");
		if (file.exists()) return;
		Commons.write(file.toPath(), setup(new PageTemplate()).render(builder.toFrame()));
	}

	private void writeJavascript(FrameBuilder builder) {
		builder.add("js");
		File destiny = createIfNotExists(new File(gen() + File.separator + "pages" + File.separator));
		File file = new File(destiny + File.separator + firstUpperCase(resource.asPage().template().name$()) + "Page.js");
		Commons.write(file.toPath(), setup(new PageTemplate()).render(builder.toFrame()));
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder result = super.buildFrame().add("resource");
		Service.UI uiService = resource.core$().ownerAs(Service.UI.class);
		result.add("name", resource.name$());
		Template template = KonosGraph.templateFor(resource);
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
		if (ElementHelper.isRoot(display) && hasAbstractClass(display)) originFrame.add("hasAbstract", "");
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