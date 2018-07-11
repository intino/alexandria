package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Dialog.Tab;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class DialogRenderer {

	private static final String DIALOGS = "dialogs";
	private KonosGraph graph;
	private final File gen;
	private final File src;
	private final String packageName;
	private final List<Dialog> dialogs;
	private final String boxName;
	private final Map<String, String> classes;

	public DialogRenderer(KonosGraph graph, File src, File gen, String packageName, String boxName, Map<String, String> classes) {
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.dialogs = graph.dialogList();
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		dialogs.forEach(this::processDialog);
	}

	private void processDialog(Dialog dialog) {
		renderDialog(dialog);
		renderDialogDisplay();
	}

	private void renderDialog(Dialog dialog) {
		final String newDialog = snakeCaseToCamelCase(dialog.name$());
		if (Commons.javaFile(new File(src, DIALOGS), newDialog).exists()) return;
		Frame frame = new Frame().addTypes("dialog");
		frame.addSlot("package", packageName);
		frame.addSlot("name", dialog.name$());
		frame.addSlot("box", boxName);
		processToolbar(frame, dialog.toolbar());
		for (Tab tab : dialog.tabList()) processTab(frame, tab);
		classes.put("Dialog#" + dialog.name$(), DIALOGS + "." + newDialog);
		Commons.writeFrame(new File(src, DIALOGS), newDialog, template().format(frame));
	}

	private void processToolbar(Frame frame, Dialog.Toolbar toolbar) {
		if (toolbar == null) return;
		for (Dialog.Toolbar.Operation operation : toolbar.operationList()) processOperation(frame, operation);
	}

	private void processOperation(Frame frame, Dialog.Toolbar.Operation operation) {
		processExecution(frame, operation);
	}

	private void processExecution(Frame frame, Dialog.Toolbar.Operation operation) {
		frame.addSlot("execution", new Frame().addTypes("execution").addSlot("box", boxName).addSlot("name", operation.name$()));
	}

	private void processTab(Frame frame, Tab tab) {
		for (Tab.Input input : tab.inputList()) processInput(frame, input);
	}

	private void processInput(Frame frame, Tab.Input input) {
		processValidator(frame, input);
		if (input.i$(Tab.OptionBox.class)) processSources(frame, input.a$(Tab.OptionBox.class));
		else if (input.i$(Tab.Section.class))
			for (Tab.Input i : ((Tab.Section) input).inputList()) processInput(frame, i);
	}

	private void processSources(Frame frame, Tab.OptionBox optionBox) {
		if (optionBox.source() != null && !optionBox.source().isEmpty())
			frame.addSlot("source", new Frame().addTypes("source").addSlot("box", boxName).addSlot("name", optionBox.source()).addSlot("field", optionBox.getClass().getSimpleName()));
	}

	private void processValidator(Frame frame, Tab.Input input) {
		if (input.validator() != null && !input.validator().isEmpty())
			frame.addSlot("validator", new Frame().addTypes("validator").addSlot("box", boxName).addSlot("name", input.validator()).addSlot("field", input.getClass().getSimpleName()));
	}

	private void renderDialogDisplay() {
		new AbstractDialogRenderer(graph, gen, packageName, boxName).execute();
	}

	private Template template() {
		Template template = DialogTemplate.create();
		Formatters.customize(template);
		return template;
	}
}
