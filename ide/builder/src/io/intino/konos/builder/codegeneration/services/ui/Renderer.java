package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.model.graph.Display;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public abstract class Renderer {
	protected static final String DISPLAYS = "displays";
	protected final Display display;
	protected final String box;
	protected final String packageName;
	protected final File src;
	private final File gen;
	private boolean buildingGen = false;

	protected Renderer(Display display, String box, String packageName, File src, File gen) {
		this.display = display;
		this.box = box;
		this.packageName = packageName;
		this.src = src;
		this.gen = gen;
	}

	public final void render() {
		writeSrc();
		writeGen();
	}

	private void writeSrc() {
		buildingGen = false;

		final String newDisplay = snakeCaseToCamelCase(display.name$());
		File sourceFile = javaFile(new File(src, DISPLAYS), newDisplay);
		if (!sourceFile.exists())
			writeFrame(new File(src, DISPLAYS), newDisplay, srcTemplate().format(createFrame()));
		else {
			Updater updater = updater(newDisplay, sourceFile);
			if (updater != null) updater.update();
		}
	}

	private void writeGen() {
		buildingGen = true;
		final String newDisplay = snakeCaseToCamelCase("Abstract" + firstUpperCase(display.name$()));
		writeFrame(new File(gen, DISPLAYS), newDisplay, genTemplate().format(createFrame().addTypes("gen")));
	}

	protected boolean buildingSrc() {
		return !buildingGen;
	}

	protected abstract Template srcTemplate();

	protected abstract Template genTemplate();

	protected abstract Updater updater(String displayName, File sourceFile);

	protected Frame createFrame() {
		return new Frame(display.getClass().getSimpleName().toLowerCase())
				.addSlot("box", box)
				.addSlot("package", packageName)
				.addSlot("name", display.name$());
	}
}
