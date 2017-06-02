package io.intino.konos.builder.codegeneration.server.activity.dialog;

import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Dialog;
import io.intino.konos.model.Dialog.Tab;
import io.intino.konos.model.multiple.dialog.tab.MultipleInput;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class DialogDisplayRenderer {

	private static final String DIALOGS = "dialogs";
	private final File gen;
	private final String packageName;
	private final List<Dialog> dialogs;
	private final String boxName;

	public DialogDisplayRenderer(Graph graph, File gen, String packageName, String boxName) {
		this.gen = gen;
		this.packageName = packageName;
		this.dialogs = graph.find(Dialog.class);
		this.boxName = boxName;
	}

	public void execute() {
		dialogs.forEach(this::processDialog);
	}

	private void processDialog(Dialog dialog) {
		Frame frame = new Frame().addTypes("dialogDisplay");
		frame.addSlot("package", packageName);
		frame.addSlot("name", dialog.name());
		frame.addSlot("box", boxName);
		final String newDialog = snakeCaseToCamelCase(dialog.name() + "DialogDisplay");
		final Frame frameDialog = new Frame().addTypes("dialog");
		if (!dialog.label().isEmpty()) frameDialog.addSlot("label", dialog.label());
		if (!dialog.description().isEmpty()) frameDialog.addSlot("description", dialog.description());
		frame.addSlot("dialog", frameDialog);
		for (Tab tab : dialog.tabList())
			frameDialog.addSlot("tab", frameOf(tab));
		Commons.writeFrame(new File(gen, DIALOGS), newDialog, template().format(frame));
	}

	private Frame frameOf(Tab tab) {
		final Frame tabFrame = new Frame().addTypes("tab");
		if (!tab.name().isEmpty()) tabFrame.addSlot("name", tab.name());
		if (!tab.label().isEmpty()) tabFrame.addSlot("label", tab.label());
		for (Tab.Text text : tab.textList()) tabFrame.addSlot("text", frameOf(text));
		for (Tab.Section section : tab.sectionList()) tabFrame.addSlot("section", frameOf(section));
		for (Tab.Memo memo : tab.memoList()) tabFrame.addSlot("memo", frameOf(memo));
		for (Tab.RadioBox radio : tab.radioBoxList()) tabFrame.addSlot("radio", frameOf(radio));
		for (Tab.CheckBox check : tab.checkBoxList()) tabFrame.addSlot("check", frameOf(check));
		for (Tab.ComboBox combo : tab.comboBoxList()) tabFrame.addSlot("combo", frameOf(combo));
		for (Tab.Password password : tab.passwordList()) tabFrame.addSlot("password", frameOf(password));
		for (Tab.File file : tab.fileList()) tabFrame.addSlot("file", frameOf(file));
		for (Tab.Picture picture : tab.pictureList()) tabFrame.addSlot("picture", frameOf(picture));
		for (Tab.Date date : tab.dateList()) tabFrame.addSlot("date", frameOf(date));
		for (Tab.DateTime dateTime : tab.dateTimeList()) tabFrame.addSlot("dateTime", frameOf(dateTime));
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
		final Frame sectionFrame = new Frame().addTypes("section");
		if (!section.name().isEmpty()) sectionFrame.addSlot("name", section.name());
		if (!section.label().isEmpty()) sectionFrame.addSlot("label", section.label());
		final List<Tab.Input> inputs = section.inputList();
		inputs.stream().filter(i -> i.is(Tab.Text.class)).map(i -> i.as(Tab.Text.class)).forEach(i -> sectionFrame.addSlot("text", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.Section.class)).map(i -> i.as(Tab.Section.class)).forEach(i -> sectionFrame.addSlot("section", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.Memo.class)).map(i -> i.as(Tab.Memo.class)).forEach(i -> sectionFrame.addSlot("memo", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.RadioBox.class)).map(i -> i.as(Tab.RadioBox.class)).forEach(i -> sectionFrame.addSlot("radio", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.CheckBox.class)).map(i -> i.as(Tab.CheckBox.class)).forEach(i -> sectionFrame.addSlot("check", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.ComboBox.class)).map(i -> i.as(Tab.ComboBox.class)).forEach(i -> sectionFrame.addSlot("combo", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.Password.class)).map(i -> i.as(Tab.Password.class)).forEach(i -> sectionFrame.addSlot("password", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.File.class)).map(i -> i.as(Tab.File.class)).forEach(i -> sectionFrame.addSlot("file", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.Picture.class)).map(i -> i.as(Tab.Picture.class)).forEach(i -> sectionFrame.addSlot("picture", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.Date.class)).map(i -> i.as(Tab.Date.class)).forEach(i -> sectionFrame.addSlot("date", frameOf(i)));
		inputs.stream().filter(i -> i.is(Tab.DateTime.class)).map(i -> i.as(Tab.DateTime.class)).forEach(i -> sectionFrame.addSlot("dateTime", frameOf(i)));
		addCommon(sectionFrame, section);
		return sectionFrame;
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
		return new Frame().addSlot("dialog", optionBox.ownerAs(Dialog.class).name()).addSlot("name", optionBox.source()).addSlot("type", optionBox.getClass().getSimpleName());
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
		frame.addSlot("owner", input.owner().name());
		frame.addSlot("label", input.label());
		frame.addSlot("readonly", input.readonly());
		frame.addSlot("required", input.required());
		frame.addSlot("placeholder", input.placeHolder());
		frame.addSlot("defaultValue", input.defaultValue());
		if(input.is(MultipleInput.class)) {
			final MultipleInput multiple = input.asMultiple();
			frame.addSlot("multiple", new Frame().addTypes("multiple").addSlot("min", multiple.min()).addSlot("max", multiple.max()));
		}
		if (input.validator() != null && !input.validator().isEmpty()) frame.addSlot("validator", validator(input));
	}

	private Frame validator(Tab.Input input) {
		return new Frame().addTypes("validator").addSlot("dialog", input.ownerAs(Dialog.class).name()).addSlot("name", input.validator()).addSlot("type", input.getClass().getSimpleName());
	}

	private Template template() {
		Template template = DialogDisplayTemplate.create();
		addFormats(template);
		return template;
	}

	private void addFormats(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
