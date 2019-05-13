package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Dialog.Tab;
import io.intino.konos.model.graph.Dialog.Toolbar.Operation;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.multiple.dialog.tab.MultipleInput;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class AbstractDialogRenderer extends UIRenderer {

	private final File gen;
	private final List<Dialog> dialogs;

	AbstractDialogRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		super(boxName, packageName);
		this.gen = gen;
		this.dialogs = graph.dialogList();
	}

	public void execute() {
		dialogs.forEach(this::processDialog);
	}

	private void processDialog(Dialog dialog) {
		FrameBuilder frame = new FrameBuilder("dialogDisplay");
		frame.add("package", packageName);
		frame.add("name", dialog.name$());
		frame.add("box", box);
		final FrameBuilder dialogFrame = new FrameBuilder("dialog");
		if (!dialog.label().isEmpty()) dialogFrame.add("label", dialog.label());
		if (!dialog.description().isEmpty()) dialogFrame.add("description", dialog.description());
		frame.add("dialog", dialogFrame);
		createToolbar(dialogFrame, dialog.name$(), dialog.toolbar());
		for (Tab tab : dialog.tabList()) dialogFrame.add("tab", frameOf(tab));
		Commons.writeFrame(new File(gen, DIALOGS), "Abstract" + snakeCaseToCamelCase(dialog.name$()), template().render(frame));
	}

	private void createToolbar(FrameBuilder builder, String dialog, Dialog.Toolbar toolbar) {
		if (toolbar == null) return;
		for (Operation operation : toolbar.operationList())
			builder.add("operation", frameOf(dialog, operation.name$(), operation.label(), operation.closeAfterExecution()));
	}

	private Frame frameOf(String dialog, String operation, String operationLabel, boolean closeAfterExecution) {
		final FrameBuilder builder = new FrameBuilder("operation").add("dialog", dialog).add("execution", operation);
		if (!operation.isEmpty()) builder.add("name", operation).add("label", operationLabel);
		builder.add("closeAfterExecution", closeAfterExecution);
		return builder.toFrame();
	}

	private Frame frameOf(Tab tab) {
		final FrameBuilder tabBuilder = new FrameBuilder("tab");
		if (!tab.name$().isEmpty()) tabBuilder.add("name", tab.name$());
		if (!tab.label().isEmpty()) tabBuilder.add("label", tab.label());
		for (Tab.Input input : tab.inputList()) processInput(tabBuilder, input);
		return tabBuilder.toFrame();
	}

	private Frame frameOf(Tab.Text text) {
		final FrameBuilder builder = new FrameBuilder("text");
		if (text.edition() != null) builder.add("edition", text.edition());
		if (text.validation() != null) builder.add("validation", frameOf(text.validation()));
		addCommon(builder, text);
		return builder.toFrame();
	}

	private Frame frameOf(Tab.Text.Validation validation) {
		final FrameBuilder builder = new FrameBuilder("text", "validation");
		if (!validation.allowedValues().isEmpty())
			builder.add("allowedValues", validation.allowedValues().toArray(new String[0]));
		if (!validation.disallowedValues().isEmpty())
			builder.add("disallowedValues", validation.disallowedValues().toArray(new String[0]));
		builder.add("disallowEmptySpaces", validation.disallowEmptySpaces());
		return builder.toFrame();
	}

	private Frame frameOf(Tab.Section section) {
		final FrameBuilder builder = new FrameBuilder("section");
		final List<Tab.Input> inputs = section.inputList();
		for (Tab.Input input : inputs) processInput(builder, input);
		addCommon(builder, section);
		return builder.toFrame();
	}

	private void processInput(FrameBuilder builder, Tab.Input input) {
		if (input.i$(Tab.Text.class)) builder.add("input", frameOf((Tab.Text) input));
		else if (input.i$(Tab.Section.class)) builder.add("input", frameOf((Tab.Section) input));
		else if (input.i$(Tab.Memo.class)) builder.add("input", frameOf((Tab.Memo) input));
		else if (input.i$(Tab.RadioBox.class)) builder.add("input", frameOf((Tab.RadioBox) input));
		else if (input.i$(Tab.CheckBox.class)) builder.add("input", frameOf((Tab.CheckBox) input));
		else if (input.i$(Tab.ComboBox.class)) builder.add("input", frameOf((Tab.ComboBox) input));
		else if (input.i$(Tab.Password.class)) builder.add("input", frameOf((Tab.Password) input));
		else if (input.i$(Tab.File.class)) builder.add("input", frameOf((Tab.File) input));
		else if (input.i$(Tab.Picture.class)) builder.add("input", frameOf((Tab.Picture) input));
		else if (input.i$(Tab.Date.class)) builder.add("input", frameOf((Tab.Date) input));
		else if (input.i$(Tab.DateTime.class)) builder.add("input", frameOf((Tab.DateTime) input));
	}

	private Frame frameOf(Tab.Memo memo) {
		final FrameBuilder builder = new FrameBuilder("memo");
		builder.add("mode", memo.mode().name());
		builder.add("height", memo.height());
		addCommon(builder, memo);
		return builder.toFrame();
	}

	private Frame frameOf(Tab.Password password) {
		final FrameBuilder builder = new FrameBuilder("password");
		if (password.validation() != null) builder.add("validation", frameOf(password.validation()));
		addCommon(builder, password);
		return builder.toFrame();
	}

	private Frame frameOf(Tab.Password.Validation validation) {
		final FrameBuilder builder = new FrameBuilder("password", "validation");
		builder.add("required", validation.isRequired().stream().map(Enum::name).toArray(String[]::new));
		if (validation.length() != null)
			builder.add("min", validation.length().min()).add("max", validation.length().max());
		return builder.toFrame();
	}

	private Frame frameOf(Tab.ComboBox combo) {
		final FrameBuilder builder = new FrameBuilder("combo");
		addSources(combo, builder);
		addCommon(builder, combo);
		return builder.toFrame();
	}

	private Frame frameOf(Tab.CheckBox check) {
		final FrameBuilder builder = new FrameBuilder("check");
		addSources(check, builder);
		addCommon(builder, check);
		return builder.toFrame();
	}

	private Frame frameOf(Tab.RadioBox radio) {
		final FrameBuilder builder = new FrameBuilder("radio");
		addSources(radio, builder);
		addCommon(builder, radio);
		return builder.toFrame();
	}

	private void addSources(Tab.OptionBox comboBox, FrameBuilder builder) {
		if (comboBox.source() != null && !comboBox.source().isEmpty()) builder.add("source", sourceOf(comboBox));
		else if (comboBox.options() != null && !comboBox.options().isEmpty())
			builder.add("options", comboBox.options().toArray(new String[0]));
	}

	private Frame sourceOf(Tab.OptionBox optionBox) {
		return new FrameBuilder().add("dialog", optionBox.core$().ownerAs(Dialog.class).name$()).add("name", optionBox.source()).add("type", optionBox.getClass().getSimpleName()).toFrame();
	}

	private Frame frameOf(Tab.File file) {
		final FrameBuilder builder = new FrameBuilder("file");
		if (file.validation() != null) builder.add("validation", frameOf(file.validation()));
		addCommon(builder, file);
		return builder.toFrame();
	}

	private Frame frameOf(Tab.Picture picture) {
		final FrameBuilder builder = new FrameBuilder("picture");
		if (picture.validation() != null) builder.add("validation", frameOf(picture.validation()));
		addCommon(builder, picture);
		return builder.toFrame();
	}

	private Frame frameOf(Tab.Resource.Validation validation) {
		final FrameBuilder builder = new FrameBuilder("resource", "validation");
		if (validation.maxSize() > 0) builder.add("maxSize", validation.maxSize());
		if (!validation.allowedExtensions().isEmpty()) builder.add("allowedExtensions", validation.allowedExtensions());
		return builder.toFrame();
	}

	private Frame frameOf(Tab.Date date) {
		final FrameBuilder builder = new FrameBuilder("date");
		builder.add("format", date.format());
		addCommon(builder, date);
		return builder.toFrame();
	}

	private Frame frameOf(Tab.DateTime dateTime) {
		final FrameBuilder builder = new FrameBuilder("dateTime");
		builder.add("format", dateTime.format());
		addCommon(builder, dateTime);
		return builder.toFrame();
	}

	private void addCommon(FrameBuilder frame, Tab.Input input) {
		frame.add("owner", input.core$().owner().name());
		if (input.name$() != null && !input.name$().isEmpty()) frame.add("name", input.name$());
		frame.add("label", input.label());
		frame.add("readonly", input.isReadonly());
		frame.add("required", input.isRequired());
		frame.add("placeholder", input.placeHolder());
		frame.add("defaultValue", input.defaultValue());
		if (input.i$(MultipleInput.class)) {
			final MultipleInput multiple = input.asMultiple();
			frame.add("multiple", new FrameBuilder("multiple").add("min", multiple.min()).add("max", multiple.max()));
		}
		if (input.validator() != null && !input.validator().isEmpty()) frame.add("validator", validator(input));
	}

	private Frame validator(Tab.Input input) {
		return new FrameBuilder("validator").add("dialog", input.core$().ownerAs(Dialog.class).name$()).add("name", input.validator()).add("type", input.getClass().getSimpleName()).toFrame();
	}

	private Template template() {
		return Formatters.customize(new AbstractDialogTemplate());
	}
}
