package io.intino.konos.builder.codegeneration.ui;

import com.intellij.openapi.diagnostic.Logger;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.decorated.DecoratedDisplay;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public abstract class ElementRenderer<C extends Layer> extends UIRenderer {
	protected final C element;
	protected final TemplateProvider templateProvider;

	protected ElementRenderer(Settings settings, C element, TemplateProvider templateProvider, Target target) {
		super(settings, target);
		this.element = element;
		this.templateProvider = templateProvider;
	}

	protected final void write(Frame frame, File src, File gen, String child) {
		writeSrc(src, child, frame);
		writeGen(gen, child, frame);
	}

	private void writeSrc(File parent, String child, Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(element.name$());
		Template template = srcTemplate();
		if (template == null) return;
		File sourceFile = fileOf(new File(parent, child), newDisplay);
		if (!sourceFile.exists())
			writeFrame(new File(parent, child), newDisplay, template.format(frame));
		else {
			Updater updater = updater(newDisplay, sourceFile);
			if (updater != null) updater.update();
		}
	}

	private void writeGen(File parent, String child, Frame frame) {
		Template template = genTemplate();
		if (template == null) return;
		final String newDisplay = snakeCaseToCamelCase((element.i$(DecoratedDisplay.class) ? "Abstract" : "") + firstUpperCase(element.name$()));
		writeFrame(new File(parent, child), newDisplay, template.format(frame.addTypes("gen")));
	}

	protected File fileOf(File file, String name) {
		if (target == Target.Service) return javaFile(file, name);
		return javascriptFile(file, name);
	}

	public void writeFrame(File packageFolder, String name, String text) {
		try {
			packageFolder.mkdirs();
			File file = fileOf(packageFolder, name);
			Files.write(file.toPath(), text.getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			Logger.getInstance("Konos: ").error(e.getMessage(), e);
		}
	}

	protected boolean isRoot(Layer element) {
		return element.core$().owner() == null;
	}

	protected abstract Updater updater(String displayName, File sourceFile);

	private Template srcTemplate() {
		return templateProvider.srcTemplate(element);
	}

	private Template genTemplate() {
		return templateProvider.genTemplate(element);
	}

}
