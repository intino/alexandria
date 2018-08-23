package io.intino.konos.builder.codegeneration.services.ui;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public abstract class UIPrototypeRenderer extends UIRenderer {
	private final String name;

	protected UIPrototypeRenderer(String name, String box, String packageName) {
		super(box, packageName);
		this.name = name;
	}

	public Frame buildFrame() {
		return new Frame()
				.addSlot("box", box)
				.addSlot("name", name)
				.addSlot("package", packageName);
	}

	public final void write(File src, File gen) {
		Frame frame = buildFrame();
		writeSrc(src, frame);
		writeGen(gen, frame);
	}

	private void writeSrc(File file, Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(name);
		File sourceFile = javaFile(new File(file, DISPLAYS), newDisplay);
		if (!sourceFile.exists())
			writeFrame(new File(file, DISPLAYS), newDisplay, srcTemplate().format(frame));
		else {
			Updater updater = updater(newDisplay, sourceFile);
			if (updater != null) updater.update();
		}
	}

	private void writeGen(File file, Frame frame) {
		final String newDisplay = snakeCaseToCamelCase("Abstract" + firstUpperCase(name));
		writeFrame(new File(file, DISPLAYS), newDisplay, genTemplate().format(frame.addTypes("gen")));
	}

	protected abstract Template srcTemplate();

	protected abstract Template genTemplate();

	protected abstract Updater updater(String displayName, File sourceFile);
}
