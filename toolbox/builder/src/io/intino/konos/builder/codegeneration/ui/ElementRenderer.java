package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.Display;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Logger;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public abstract class ElementRenderer<C extends Layer> extends UIRenderer {
	protected final C element;
	protected final TemplateProvider templateProvider;

	protected ElementRenderer(CompilationContext compilationContext, C element, TemplateProvider templateProvider, Target target) {
		super(compilationContext, target);
		this.element = element;
		this.templateProvider = templateProvider;
	}

	@Override
	public void execute() throws KonosException {
		File displayFile = javaFile(displayFolder(gen(), typeOf(element), target), displayName(false));
		if (isRendered(element) && displayFile.exists()) return;
		super.execute();
	}

	protected final void write(FrameBuilder builder) {
		if (hasAbstractClass(element)) writeSrc(builder);
		writeGen(builder);
	}

	private void writeSrc(FrameBuilder builder) {
		final String newDisplay = displayFilename(element.name$(), builder.is("accessible") ? "Proxy" : "");
		Template template = srcTemplate(builder);
		String type = typeOf(element);
		if (template == null) return;
		File sourceFile = displayFile(src(), newDisplay, type, target);
		if (!sourceFile.exists())
			writeFrame(displayFolder(src(), type, target), newDisplay, template.render(builder.toFrame()));
		else {
			Updater updater = updater(newDisplay, sourceFile);
			if (updater != null) updater.update();
		}
	}

	private void writeGen(FrameBuilder builder) {
		Template template = genTemplate(builder);
		String type = typeOf(element);
		if (template == null) return;
		final String newDisplay = displayName(builder.is("accessible"));
		writeFrame(displayFolder(gen(), type, target), newDisplay, template.render(builder.add("gen").toFrame()));
	}

	private String displayName(boolean accessible) {
		final String suffix = accessible ? "Proxy" : "";
		final String abstractValue = accessible ? "" : (ElementHelper.isRoot(element) && hasAbstractClass(element) ? "Abstract" : "");
		return displayFilename(snakeCaseToCamelCase(abstractValue + firstUpperCase(element.name$())), suffix);
	}

	public void writeFrame(File packageFolder, String name, String text) {
		try {
			packageFolder.mkdirs();
			File file = fileOf(packageFolder, name, target);
			Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));
			if (!target.equals(Target.Owner)) return;
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(element), javaFile(packageFolder, name).getAbsolutePath()));
		} catch (IOException e) {
			Logger.getGlobal().severe(e.getMessage());
		}
	}

	protected abstract Updater updater(String displayName, File sourceFile);

	private Template srcTemplate(FrameBuilder builder) {
		return templateProvider.srcTemplate(element, builder);
	}

	private Template genTemplate(FrameBuilder builder) {
		return templateProvider.genTemplate(element, builder);
	}

}
