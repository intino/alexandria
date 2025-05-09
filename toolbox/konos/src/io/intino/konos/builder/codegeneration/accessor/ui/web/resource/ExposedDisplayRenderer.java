package io.intino.konos.builder.codegeneration.accessor.ui.web.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.accessor.ui.web.templates.PageTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.dsl.Display;

import java.io.File;

import static io.intino.konos.builder.codegeneration.Formatters.all;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.hasAbstractClass;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public class ExposedDisplayRenderer extends UIRenderer {
	private final Display.Exposed display;
	private final Target target;

	public ExposedDisplayRenderer(CompilationContext compilationContext, Display.Exposed display, Target target) {
		super(compilationContext);
		this.display = display;
		this.target = target;
	}

	@Override
	public void render() throws KonosException {
		writeHtml(buildFrame());
		writeJavascript(buildFrame());
	}

	private void writeHtml(FrameBuilder builder) {
		builder.add("html");
		File file = new File(src(target) + File.separator + display.name$() + ".html");
		if (file.exists()) return;
		Commons.write(file.toPath(), new PageTemplate().render(builder.toFrame(), all));
	}

	private void writeJavascript(FrameBuilder builder) {
		builder.add("js");
		File destiny = createIfNotExists(new File(gen(target) + File.separator + "pages" + File.separator));
		File file = new File(destiny + File.separator + firstUpperCase(display.name$()) + "Page.js");
		Commons.write(file.toPath(), new PageTemplate().render(builder.toFrame(), all));
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder result = super.buildFrame().add("exposedDisplay");
		result.add("name", display.name$());
		result.add("type", shortId(display));
		addOrigin(result);
		return result;
	}

	private void addOrigin(FrameBuilder builder) {
		FrameBuilder originFrame = new FrameBuilder();
		if (ElementHelper.isRoot(display)) originFrame.add("decorated", "");
		if (ElementHelper.isRoot(display) && hasAbstractClass(display, target)) originFrame.add("hasAbstract", "");
		builder.add("origin", originFrame);
	}

}