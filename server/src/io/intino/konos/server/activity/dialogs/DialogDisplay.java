package io.intino.konos.server.activity.dialogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.*;
import io.intino.konos.server.activity.dialogs.DialogValidator.Result;
import io.intino.konos.server.activity.dialogs.builders.DialogBuilder;
import io.intino.konos.server.activity.dialogs.builders.ValidationBuilder;
import io.intino.konos.server.activity.dialogs.schemas.DialogInput;
import io.intino.konos.server.activity.displays.Display;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class DialogDisplay extends Display<DialogNotifier> {
	protected Dialog dialog;
	private Map<String, String> fieldsMap = new HashMap<>();
	private Map<Class<? extends Input>, Function<Input, Result>> validators = new HashMap<>();

	public DialogDisplay(Box box, Dialog dialog) {
        super();
        this.dialog(dialog);
        validators.put(Text.class, this::validateText);
        validators.put(Section.class, this::validateSection);
        validators.put(Memo.class, this::validateMemo);
		validators.put(Password.class, this::validatePassword);
		validators.put(RadioBox.class, this::validateOptionBox);
		validators.put(CheckBox.class, this::validateOptionBox);
		validators.put(ComboBox.class, this::validateOptionBox);
        validators.put(File.class, this::validateResource);
        validators.put(Picture.class, this::validateResource);
        validators.put(Date.class, this::validateDate);
        validators.put(DateTime.class, this::validateDateTime);
    }

	public void dialog(Dialog dialog) {
		this.dialog = dialog;
		this.dialog.valuesLoader(input -> {
			if (fieldsMap.containsKey(input.label()))
				return fieldsMap.get(input.label());
			return fieldsMap.containsKey(input.name()) ? fieldsMap.get(input.name()) : "";
		});
		fillDefaultValues();
	}

	@Override
    public void init() {
    	notifier.render(DialogBuilder.build(dialog));
	}

	public void update(DialogInput dialogInput) {
		register(dialogInput);
		Result result = validate(dialogInput);

		if (result != null)
			notifier.refresh(ValidationBuilder.build(dialogInput.name(), result));
	}

	public void execute() {
		System.out.println("--> execute dialog");
	}

	private void register(DialogInput dialogInput) {
		fieldsMap.put(dialogInput.name(), dialogInput.value());
	}

	private Result validate(DialogInput dialogInput) {
		Input input = dialog.input(dialogInput.name());

		Result result = input.validate();
		if (result != null) return result;

		return validators.get(input.getClass()).apply(input);
	}

	private Result validateText(Input input) {
		String value = fieldsMap.get(input.name());
		Text text = (Text)input;

		Result result = text.validateEmail(value);
		if (result != null) return result;

		result = text.validateAllowedValues(value);
		if (result != null) return result;

		return text.validateLength(value);
	}

	private Result validateSection(Input input) {
		return null;
	}

	private Result validateMemo(Input input) {
		return null;
	}

	private Result validatePassword(Input input) {
		String value = fieldsMap.get(input.name());
		Password password = (Password)input;
		return password.validateLength(value);
	}

	private Result validateOptionBox(Input input) {
		return null;
	}

	private Result validateResource(Input input) {
		String value = fieldsMap.get(input.name());
		Resource resource = (Resource)input;

		Result result = resource.validateMaxSize(value);
		if (result != null) return result;

		return resource.validateExtension(value);
	}

	private Result validateDate(Input input) {
		return null;
	}

	private Result validateDateTime(Input input) {
		return null;
	}

	private void fillDefaultValues() {
		inputs(dialog).stream().filter(input -> input.defaultValue() != null && !input.defaultValue().isEmpty())
							   .forEach(input -> fieldsMap.put(input.name(), input.defaultValue()));
	}

	private List<Input> inputs(Dialog dialog) {
		return dialog.tabList().stream().map(Dialog.Tab::inputList).flatMap(Collection::stream).collect(toList());
	}

}