package io.intino.konos.builder.codegeneration.server.activity.dialog;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Dialog;
import io.intino.konos.model.Dialog.Tab;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class DialogRenderer {

	private static final String DIALOGS = "dialogs";
	private final Project project;
	private Graph graph;
	private final File gen;
	private final File src;
	private final String packageName;
	private final List<Dialog> dialogs;
	private final String boxName;

	public DialogRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.dialogs = graph.find(Dialog.class);
		this.boxName = boxName;
	}

	public void execute() {
		dialogs.forEach(this::processDialog);
	}

	private void processDialog(Dialog dialog) {
		renderDialog(dialog);
		renderDialogDisplay();
	}

	private void renderDialog(Dialog dialog) {
		Frame frame = new Frame().addTypes("dialog");
		frame.addSlot("package", packageName);
		frame.addSlot("name", dialog.name());
		frame.addSlot("box", boxName);
		for (Tab tab : dialog.tabList()) processTab(frame, tab);
		final String newDialog = snakeCaseToCamelCase(dialog.name() + "Dialog");
		if (!Commons.javaFile(new File(src, DIALOGS), newDialog).exists())
			Commons.writeFrame(new File(src, DIALOGS), newDialog, template().format(frame));
	}

	private void processTab(Frame frame, Tab tab) {
		for (Tab.Input input : tab.inputList()) processInput(frame, input);
	}

	private void processInput(Frame frame, Tab.Input input) {
		processValidator(frame, input);
		if (input.is(Tab.OptionBox.class)) processSources(frame, input.as(Tab.OptionBox.class));
		else if (input.is(Tab.Section.class))
			for (Tab.Input i : ((Tab.Section) input).inputList()) processInput(frame, i);
	}

	private void processSources(Frame frame, Tab.OptionBox optionBox) {
		if (optionBox.source() != null && !optionBox.source().isEmpty())
			frame.addSlot("source", new Frame().addTypes("source").addSlot("name", optionBox.source()).addSlot("field", optionBox.getClass().getSimpleName()));
	}

	private void processValidator(Frame frame, Tab.Input input) {
		if (input.validator() != null && !input.validator().isEmpty())
			frame.addSlot("validator", new Frame().addTypes("validator").addSlot("name", input.validator()).addSlot("field", input.getClass().getSimpleName()));
	}

	private void renderDialogDisplay() {
		new DialogDisplayRenderer(graph, gen, packageName, boxName).execute();
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
