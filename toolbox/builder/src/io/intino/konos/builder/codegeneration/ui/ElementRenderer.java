package io.intino.konos.builder.codegeneration.ui;

import com.intellij.openapi.diagnostic.Logger;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.decorated.DecoratedDisplay;
import io.intino.tara.magritte.Layer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public abstract class ElementRenderer<C extends Layer> extends UIRenderer {
	protected final C element;
	protected final TemplateProvider templateProvider;

	protected ElementRenderer(Settings settings, C element, TemplateProvider templateProvider, Target target) {
		super(settings, target);
		this.element = element;
		this.templateProvider = templateProvider;
	}

	@Override
	public void execute() {
		if (isRendered(element)) return;
		super.execute();
		saveRendered(element);
	}

	protected final void write(FrameBuilder builder) {
		writeSrc(builder);
		writeGen(builder);
	}

	private void writeSrc(FrameBuilder builder) {
		final String newDisplay = snakeCaseToCamelCase(element.name$());
		Template template = srcTemplate();
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
		Template template = genTemplate();
		String type = typeOf(element);
		if (template == null) return;
		final String newDisplay = snakeCaseToCamelCase((element.i$(DecoratedDisplay.class) ? "Abstract" : "") + firstUpperCase(element.name$()));
		writeFrame(displayFolder(gen(), type, target), newDisplay, template.render(builder.add("gen").toFrame()));
	}

	public void writeFrame(File packageFolder, String name, String text) {
		try {
			packageFolder.mkdirs();
			File file = fileOf(packageFolder, name, target);
			Files.write(file.toPath(), text.getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			Logger.getInstance("Konos: ").error(e.getMessage(), e);
		}
	}

	protected abstract Updater updater(String displayName, File sourceFile);

	private Template srcTemplate() {
		return templateProvider.srcTemplate(element);
	}

	private Template genTemplate() {
		return templateProvider.genTemplate(element);
	}

}
