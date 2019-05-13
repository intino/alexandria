package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public abstract class UIPrototypeRenderer extends UIRenderer {
	private final String name;

	protected UIPrototypeRenderer(String name, String box, String packageName) {
		super(box, packageName);
		this.name = name;
	}

	public FrameBuilder frameBuilder() {
		return super.frameBuilder().add("name", name);
	}

	public final void write(File src, File gen) {
		FrameBuilder frameBuilder = frameBuilder();
		writeSrc(src, frameBuilder);
		writeGen(gen, frameBuilder);
	}

	private void writeSrc(File file, FrameBuilder builder) {
		final String newDisplay = snakeCaseToCamelCase(name);
		File sourceFile = javaFile(new File(file, DISPLAYS), newDisplay);
		if (!sourceFile.exists()) writeFrame(new File(file, DISPLAYS), newDisplay, srcTemplate().render(builder.toFrame()));
		else {
			Updater updater = updater(newDisplay, sourceFile);
			if (updater != null) updater.update();
		}
	}

	private void writeGen(File file, FrameBuilder builder) {
		final String name = snakeCaseToCamelCase("Abstract" + firstUpperCase(this.name));
		writeFrame(new File(file, DISPLAYS), name, genTemplate().render(builder.add("gen").toFrame()));
	}

	protected abstract Template srcTemplate();

	protected abstract Template genTemplate();

	protected abstract Updater updater(String displayName, File sourceFile);
}
