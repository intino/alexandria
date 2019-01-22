package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.UIRenderer;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public abstract class ElementRenderer extends UIRenderer {
	private final String name;

	protected ElementRenderer(Settings settings, String name) {
		super(settings);
		this.name = name;
	}

	protected final void write(Frame frame, File src, File gen, String child) {
		writeSrc(src, child, frame);
		writeGen(gen, child, frame);
	}

	private void writeSrc(File parent, String child, Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(name);
		File sourceFile = javaFile(new File(parent, child), newDisplay);
		if (!sourceFile.exists())
			writeFrame(new File(parent, child), newDisplay, srcTemplate().format(frame));
		else {
			Updater updater = updater(newDisplay, sourceFile);
			if (updater != null) updater.update();
		}
	}

	private void writeGen(File parent, String child, Frame frame) {
		Template template = genTemplate();
		if (template == null) return;
		final String newDisplay = snakeCaseToCamelCase("Abstract" + firstUpperCase(name));
		writeFrame(new File(parent, child), newDisplay, template.format(frame.addTypes("gen")));
	}

	protected abstract Template srcTemplate();

	protected abstract Template genTemplate();

	protected abstract Updater updater(String displayName, File sourceFile);
}
