package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Dialog.Tab;
import io.intino.konos.model.graph.Dialog.Toolbar.Operation;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.multiple.dialog.tab.MultipleInput;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class DialogGenRenderer {

	private static final String DIALOGS = "dialogs";
	private final File gen;
	private final String packageName;
	private final List<Dialog> dialogs;
	private final String boxName;

	public DialogGenRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		this.gen = gen;
		this.packageName = packageName;
		this.dialogs = graph.dialogList();
		this.boxName = boxName;
	}

	public void execute() {
		dialogs.forEach(this::processDialog);
	}

	private void processDialog(Dialog dialog) {
		Frame frame = new Frame().addTypes("dialogDisplay");
		frame.addSlot("package", packageName);
		frame.addSlot("name", dialog.name$());
		frame.addSlot("box", boxName);
		final Frame dialogFrame = new Frame().addTypes("dialog");
		if (!dialog.label().isEmpty()) dialogFrame.addSlot("label", dialog.label());
		if (!dialog.description().isEmpty()) dialogFrame.addSlot("description", dialog.description());
		frame.addSlot("dialog", dialogFrame);
		createToolbar(dialogFrame, dialog.name$(), dialog.toolbar());
		for (Tab tab : dialog.tabList()) dialogFrame.addSlot("tab", frameOf(tab));
		Commons.writeFrame(new File(gen, DIALOGS), "Abstract" + snakeCaseToCamelCase(dialog.name$()), template().format(frame));
	}

	private void createToolbar(Frame frame, String dialog, Dialog.Toolbar toolbar) {
		if (toolbar != null) customToolbar(frame, dialog, toolbar);
		else defaultToolbar(frame, dialog);
	}

	private void defaultToolbar(Frame frame, String dialog) {
		frame.addSlot("operation", frameOf(dialog, "send", "send"));
	}

	private void customToolbar(Frame frame, String dialog, Dialog.Toolbar toolbar) {
		for (Operation operation : toolbar.operationList())
			frame.addSlot("operation", frameOf(dialog, operation.name$(), operation.label()));
	}

	private Frame frameOf(String dialog, String operation, String operationLabel) {
		final Frame operationFrame = new Frame().addTypes("operation").addSlot("dialog", dialog).addSlot("execution", operation);
		if (!operation.isEmpty()) operationFrame.addSlot("name", operation).addSlot("label", operationLabel);
		return operationFrame;
	}

	private Frame frameOf(Tab tab) {
		final Frame tabFrame = new Frame().addTypes("tab");
		if (!tab.name$().isEmpty()) tabFrame.addSlot("name", tab.name$());
		if (!tab.label().isEmpty()) tabFrame.addSlot("label", tab.label());
		for (Tab.Input input : tab.inputList()) processInput(tabFrame, input);
		return tabFrame;
	}

	private Frame frameOf(Tab.Text text) {
		final Frame frame = new Frame().addTypes("text");
		if (text.edition() != null) frame.addSlot("edition", text.edition());
		if (text.validation() != null) frame.addSlot("validation", frameOf(text.validation()));
		addCommon(frame, text);
		return frame;
	}

	private Frame frameOf(Tab.Text.Validation validation) {
		final Frame frame = new Frame().addTypes("text", "validation");
		if (!validation.allowedValues().isEmpty())
			frame.addSlot("allowedValues", validation.allowedValues().toArray(new String[0]));
		if (!validation.disallowedValues().isEmpty())
			frame.addSlot("disallowedValues", validation.disallowedValues().toArray(new String[0]));
		frame.addSlot("disallowEmptySpaces", validation.disallowEmptySpaces());
		return frame;
	}

	private Frame frameOf(Tab.Section section) {
		final Frame frame = new Frame().addTypes("section");
		if (!section.name$().isEmpty()) frame.addSlot("name", section.name$());
		final List<Tab.Input> inputs = section.inputList();
		for (Tab.Input input : inputs) processInput(frame, input);
		addCommon(frame, section);
		return frame;
	}

	private void processInput(Frame sectionFrame, Tab.Input input) {
		if (input.i$(Tab.Text.class)) sectionFrame.addSlot("input", frameOf((Tab.Text) input));
		else if (input.i$(Tab.Section.class)) sectionFrame.addSlot("input", frameOf((Tab.Section) input));
		else if (input.i$(Tab.Memo.class)) sectionFrame.addSlot("input", frameOf((Tab.Memo) input));
		else if (input.i$(Tab.RadioBox.class)) sectionFrame.addSlot("input", frameOf((Tab.RadioBox) input));
		else if (input.i$(Tab.CheckBox.class)) sectionFrame.addSlot("input", frameOf((Tab.CheckBox) input));
		else if (input.i$(Tab.ComboBox.class)) sectionFrame.addSlot("input", frameOf((Tab.ComboBox) input));
		else if (input.i$(Tab.Password.class)) sectionFrame.addSlot("input", frameOf((Tab.Password) input));
		else if (input.i$(Tab.File.class)) sectionFrame.addSlot("input", frameOf((Tab.File) input));
		else if (input.i$(Tab.Picture.class)) sectionFrame.addSlot("input", frameOf((Tab.Picture) input));
		else if (input.i$(Tab.Date.class)) sectionFrame.addSlot("input", frameOf((Tab.Date) input));
		else if (input.i$(Tab.DateTime.class)) sectionFrame.addSlot("input", frameOf((Tab.DateTime) input));
	}

	private Frame frameOf(Tab.Memo memo) {
		final Frame frame = new Frame().addTypes("memo");
		frame.addSlot("mode", memo.mode().name());
		frame.addSlot("height", memo.height());
		addCommon(frame, memo);
		return frame;
	}

	private Frame frameOf(Tab.Password password) {
		final Frame frame = new Frame().addTypes("password");
		if (password.validation() != null) frame.addSlot("validation", frameOf(password.validation()));
		addCommon(frame, password);
		return frame;
	}

	private Frame frameOf(Tab.Password.Validation validation) {
		final Frame frame = new Frame().addTypes("password", "validation");
		frame.addSlot("required", validation.required().stream().map(Enum::name).toArray(String[]::new));
		if (validation.length() != null)
			frame.addSlot("min", validation.length().min()).addSlot("max", validation.length().max());
		return frame;
	}

	private Frame frameOf(Tab.ComboBox combo) {
		final Frame frame = new Frame().addTypes("combo");
		addSources(combo, frame);
		addCommon(frame, combo);
		return frame;
	}

	private Frame frameOf(Tab.CheckBox check) {
		final Frame frame = new Frame().addTypes("check");
		addSources(check, frame);
		addCommon(frame, check);
		return frame;
	}

	private Frame frameOf(Tab.RadioBox radio) {
		final Frame frame = new Frame().addTypes("radio");
		addSources(radio, frame);
		addCommon(frame, radio);
		return frame;
	}

	private void addSources(Tab.OptionBox comboBox, Frame frame) {
		if (comboBox.source() != null && !comboBox.source().isEmpty()) frame.addSlot("source", sourceOf(comboBox));
		else if (comboBox.options() != null && !comboBox.options().isEmpty())
			frame.addSlot("options", comboBox.options().toArray(new String[0]));
	}

	private Frame sourceOf(Tab.OptionBox optionBox) {
		return new Frame().addSlot("dialog", optionBox.core$().ownerAs(Dialog.class).name$()).addSlot("name", optionBox.source()).addSlot("type", optionBox.getClass().getSimpleName());
	}

	private Frame frameOf(Tab.File file) {
		final Frame frame = new Frame().addTypes("file");
		if (file.validation() != null) frame.addSlot("validation", frameOf(file.validation()));
		addCommon(frame, file);
		return frame;
	}

	private Frame frameOf(Tab.Picture picture) {
		final Frame frame = new Frame().addTypes("picture");
		if (picture.validation() != null) frame.addSlot("validation", frameOf(picture.validation()));
		addCommon(frame, picture);
		return frame;
	}

	private AbstractFrame frameOf(Tab.Resource.Validation validation) {
		final Frame frame = new Frame().addTypes("resource", "validation");
		if (validation.maxSize() > 0) frame.addSlot("maxSize", validation.maxSize());
		if (!validation.allowedExtensions().isEmpty()) frame.addSlot("allowedExtensions", validation.allowedExtensions());
		return frame;
	}

	private Frame frameOf(Tab.Date date) {
		final Frame frame = new Frame().addTypes("date");
		frame.addSlot("format", date.format());
		addCommon(frame, date);
		return frame;
	}

	private Frame frameOf(Tab.DateTime dateTime) {
		final Frame frame = new Frame().addTypes("dateTime");
		frame.addSlot("format", dateTime.format());
		addCommon(frame, dateTime);
		return frame;
	}

	private void addCommon(Frame frame, Tab.Input input) {
		frame.addSlot("owner", input.core$().owner().name());
		if (input.name$() != null) frame.addSlot("name", input.name$());
		frame.addSlot("label", input.label());
		frame.addSlot("readonly", input.readonly());
		frame.addSlot("required", input.required());
		frame.addSlot("placeholder", input.placeHolder());
		frame.addSlot("defaultValue", input.defaultValue());
		if (input.i$(MultipleInput.class)) {
			final MultipleInput multiple = input.asMultiple();
			frame.addSlot("multiple", new Frame().addTypes("multiple").addSlot("min", multiple.min()).addSlot("max", multiple.max()));
		}
		if (input.validator() != null && !input.validator().isEmpty()) frame.addSlot("validator", validator(input));
	}

	private Frame validator(Tab.Input input) {
		return new Frame().addTypes("validator").addSlot("dialog", input.core$().ownerAs(Dialog.class).name$()).addSlot("name", input.validator()).addSlot("type", input.getClass().getSimpleName());
	}

	private Template template() {
		Template template = DialogGenTemplate.create();
		Formatters.customize(template);
		return template;
	}

}
