package io.intino.konos.builder.codegeneration.accessor.ui.resource;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PageTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Template;

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
		writeHtml(buildFrame());
		writeJavascript(buildFrame());
	}

	private void writeHtml(FrameBuilder builder) {
		builder.add("html");
		File file = new File(src() + File.separator + resource.name$() + ".html");
		if (file.exists()) return;
		Commons.write(file.toPath(), setup(new PageTemplate()).render(builder.toFrame()));
	}

	private void writeJavascript(FrameBuilder builder) {
		builder.add("js");
		File destiny = createIfNotExists(new File(gen() + File.separator + "pages" + File.separator));
		File file = new File(destiny + File.separator + firstUpperCase(resource.name$()) + ".js");
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
		builder.add("pageDisplayOrigin", originFrame);
	}

	private Frame accessibleImportFrame(Service.UI.Use use) {
		FrameBuilder result = new FrameBuilder("accessibleImport");
		result.add("name", use.name$());
		result.add("url", use.url());
		result.add("elements", camelCaseToSnakeCase(use.className().substring(use.className().lastIndexOf(".")+1)));
		return result.toFrame();
	}

}