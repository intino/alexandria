package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Dialog.Tab;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;

public class DialogRenderer extends UIRenderer {
	private final File gen;
	private final File src;
	private final List<Dialog> dialogs;
	private final Map<String, String> classes;
	private KonosGraph graph;

	public DialogRenderer(KonosGraph graph, File src, File gen, String packageName, String boxName, Map<String, String> classes) {
		super(boxName, packageName);
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.dialogs = graph.dialogList();
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
		FrameBuilder builder = frameBuilder().add("dialog").add("name", dialog.name$());
		processToolbar(builder, dialog.toolbar());
		for (Tab tab : dialog.tabList()) processTab(builder, tab);
		classes.put("Dialog#" + dialog.name$(), DIALOGS + "." + newDialog);
		Commons.writeFrame(new File(src, DIALOGS), newDialog, template().render(builder.toFrame()));
	}

	private void processToolbar(FrameBuilder builder, Dialog.Toolbar toolbar) {
		if (toolbar == null) return;
		for (Dialog.Toolbar.Operation operation : toolbar.operationList()) processExecution(builder, operation);
	}

	private void processExecution(FrameBuilder builder, Dialog.Toolbar.Operation operation) {
		builder.add("execution", new FrameBuilder().add("execution").add("box", box).add("name", operation.name$()));
	}

	private void processTab(FrameBuilder builder, Tab tab) {
		for (Tab.Input input : tab.inputList()) processInput(builder, input);
	}

	private void processInput(FrameBuilder builder, Tab.Input input) {
		processValidator(builder, input);
		if (input.i$(Tab.OptionBox.class)) processSources(builder, input.a$(Tab.OptionBox.class));
		else if (input.i$(Tab.Section.class))
			for (Tab.Input i : ((Tab.Section) input).inputList()) processInput(builder, i);
	}

	private void processSources(FrameBuilder builder, Tab.OptionBox optionBox) {
		if (optionBox.source() != null && !optionBox.source().isEmpty())
			builder.add("source", new FrameBuilder("source").add("box", box).add("name", optionBox.source()).add("field", optionBox.getClass().getSimpleName()));
	}

	private void processValidator(FrameBuilder builder, Tab.Input input) {
		if (input.validator() != null && !input.validator().isEmpty())
			builder.add("validator", new FrameBuilder("validator").add("box", box).add("name", input.validator()).add("field", input.getClass().getSimpleName()));
	}

	private void renderDialogDisplay() {
		new AbstractDialogRenderer(graph, gen, packageName, box).execute();
	}

	private Template template() {
		return customize(new DialogTemplate());
	}
}
