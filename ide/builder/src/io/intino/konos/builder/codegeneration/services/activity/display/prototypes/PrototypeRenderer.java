package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import io.intino.konos.model.graph.Display;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public abstract class PrototypeRenderer {
	private static final String DISPLAYS = "displays";
	protected final Display display;
	protected final String box;
	private final String packageName;
	private final File src;
	private final File gen;

	PrototypeRenderer(Display display, String box, String packageName, File src, File gen) {
		this.display = display;
		this.box = box;
		this.packageName = packageName;
		this.src = src;
		this.gen = gen;
	}


	void writeAbstract(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase("Abstract" + firstUpperCase(display.name$()) + display.getClass().getSimpleName());
		writeFrame(new File(gen, DISPLAYS), newDisplay, template().format(frame));
	}

	void writeSrc(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(display.name$() + display.getClass().getSimpleName());
		if (!javaFile(new File(src, DISPLAYS), newDisplay).exists())
			writeFrame(new File(src, DISPLAYS), newDisplay, template().format(frame));
	}

	protected abstract Template template();

	protected Template customize(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	protected Frame createFrame() {
		return new Frame(display.getClass().getSimpleName())
				.addSlot("box", box)
				.addSlot("package", packageName)
				.addSlot("name", display.name$());
	}
}
