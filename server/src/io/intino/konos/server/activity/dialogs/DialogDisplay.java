package io.intino.konos.server.activity.dialogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.*;
import io.intino.konos.server.activity.dialogs.DialogValidator.Result;
import io.intino.konos.server.activity.dialogs.builders.DialogBuilder;
import io.intino.konos.server.activity.dialogs.builders.ValidationBuilder;
import io.intino.konos.server.activity.dialogs.schemas.DialogInput;
import io.intino.konos.server.activity.dialogs.schemas.DialogInputResource;
import io.intino.konos.server.activity.displays.Display;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.codec.binary.Base64.decodeBase64;

public class DialogDisplay extends Display<DialogNotifier> {
	protected Dialog dialog;
	private Map<String, Object> fieldsMap = new HashMap<>();
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
			return fieldsMap.getOrDefault(input.name(), "");
		});
		fillDefaultValues();
	}

	@Override
    public void init() {
    	notifier.render(DialogBuilder.build(dialog));
	}

	public void update(DialogInput dialogInput) {
		Input input = dialog.input(dialogInput.name());
		fieldsMap.put(input.name(), dialogInput.value());
		refresh(input);
	}

	private void refresh(Input input) {
		Result result = validate(input);
		if (result == null) return;
		notifier.refresh(ValidationBuilder.build(input.name(), result));
	}

	public void uploadResource(DialogInputResource inputResource) {
		Input input = dialog.input(inputResource.input());
		fieldsMap.put(input.name(), inputResource.resource());
		refresh(input);
	}

	public io.intino.konos.server.activity.spark.ActivityFile downloadResource(String name) {
		Input input = dialog.input(name);
		io.intino.konos.server.activity.dialogs.schemas.Resource resource = (io.intino.konos.server.activity.dialogs.schemas.Resource)input.value();

		return new io.intino.konos.server.activity.spark.ActivityFile() {
			@Override
			public String label() {
				return resource.name();
			}

			@Override
			public InputStream content() {
				String value = resource.value();
				return new ByteArrayInputStream(value == null || value.isEmpty() ? new byte[0] : decodeBase64(value.split(",")[1].replace(" ", "+")));
			}
		};
	}

	public void execute() {
		System.out.println("--> execute dialog");
	}

	private Result validate(Input input) {
		Result result = input.validate();
		if (result != null) return result;

		return validators.get(input.getClass()).apply(input);
	}

	private Result validateText(Input input) {
		String value = (String) fieldsMap.get(input.name());
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
		String value = (String) fieldsMap.get(input.name());
		Password password = (Password)input;
		return password.validateLength(value);
	}

	private Result validateOptionBox(Input input) {
		return null;
	}

	private Result validateResource(Input input) {
		Resource resource = (Resource)input;
		io.intino.konos.server.activity.dialogs.schemas.Resource resourceValue = (io.intino.konos.server.activity.dialogs.schemas.Resource) fieldsMap.get(input.name());
		byte[] resourceContent = Base64.decodeBase64(resourceValue.value());

		Result result = resource.validateMaxSize(resourceValue.name(), resourceContent);
		if (result != null) return result;

		return resource.validateExtension(resourceValue.name());
	}

	private Result validateDate(Input input) {
		return null;
	}

	private Result validateDateTime(Input input) {
		return null;
	}

	private void fillDefaultValues() {
		inputs(dialog).stream().filter(input -> input.defaultValue() != null)
							   .forEach(input -> fieldsMap.put(input.name(), input.defaultValue()));
	}

	private List<Input> inputs(Dialog dialog) {
		return dialog.tabList().stream().map(Dialog.Tab::inputList).flatMap(Collection::stream).collect(toList());
	}

}