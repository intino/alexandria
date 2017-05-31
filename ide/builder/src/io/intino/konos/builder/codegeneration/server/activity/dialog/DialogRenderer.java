package io.intino.konos.builder.codegeneration.server.activity.dialog;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Dialog;
import io.intino.konos.model.Schema;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class DialogRenderer {

	private static final String DIALOGS = "dialogs";
	private final Project project;
	private final File gen;
	private final File src;
	private final String packageName;
	private final List<Dialog> dialogs;
	private final String boxName;

	public DialogRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.dialogs = graph.find(Dialog.class);
		this.boxName = boxName;
	}

	public void execute() {
		dialogs.forEach(this::processDialog);
	}

	private void processDialog(Dialog display) {
		Frame frame = new Frame().addTypes("display");
		frame.addSlot("package", packageName);
		frame.addSlot("name", display.name());
		if (!display.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		frame.addSlot("box", boxName);
		final String newDisplay = snakeCaseToCamelCase(display.name() + "Display");
		if (!Commons.javaFile(new File(src, DIALOGS), newDisplay).exists())
			Commons.writeFrame(new File(src, DIALOGS), newDisplay, template().format(frame));
	}

	private Template template() {
		Template template = DialogTemplate.create();
		addFormats(template);
		return template;
	}

	private void addFormats(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
