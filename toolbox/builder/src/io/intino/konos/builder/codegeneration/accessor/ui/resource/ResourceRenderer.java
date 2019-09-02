package io.intino.konos.builder.codegeneration.accessor.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PageTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Template;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public class ResourceRenderer extends io.intino.konos.builder.codegeneration.ui.resource.ResourceRenderer {

	public ResourceRenderer(Settings settings, UIService.Resource resource) {
		super(settings, resource, Target.Accessor);
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
		result.add("name", resource.name$());
		Template template = KonosGraph.templateFor(resource);
		result.add("pageDisplay", template.name$());
		result.add("pageDisplayId", shortId(template));
		result.add("pageDisplayType", typeOf(template));
		addPageDisplayOrigin(result, template);
		return result;
	}

	private void addPageDisplayOrigin(FrameBuilder builder, Display display) {
		FrameBuilder originFrame = new FrameBuilder();
		if (ElementHelper.isRoot(display)) originFrame.add("decorated", "");
		builder.add("pageDisplayOrigin", originFrame);
	}

}