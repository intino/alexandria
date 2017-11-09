package io.intino.konos.alexandria.foundation.activity.dialogs;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.foundation.activity.dialogs.builders.DialogBuilder;
import io.intino.konos.alexandria.foundation.activity.dialogs.builders.ValidationBuilder;
import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.foundation.activity.model.Dialog;
import io.intino.konos.alexandria.foundation.activity.schemas.DialogInput;
import io.intino.konos.alexandria.foundation.activity.schemas.DialogInputResource;
import io.intino.konos.alexandria.foundation.activity.services.push.User;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.codec.binary.Base64.decodeBase64;

public abstract class AlexandriaDialogDisplay extends AlexandriaDisplay<AlexandriaDialogDisplayNotifier> {
	private Box box;
	private Map<Class<? extends Dialog.Tab.Input>, Function<FormInput, DialogValidator.Result>> validators = new HashMap<>();
	private Dialog dialog;

	public AlexandriaDialogDisplay(Box box, Dialog dialog) {
		super();
		this.box = box;
		this.dialog(dialog);
		validators.put(Dialog.Tab.Text.class, this::validateText);
		validators.put(Dialog.Tab.Section.class, this::validateSection);
		validators.put(Dialog.Tab.Memo.class, this::validateMemo);
		validators.put(Dialog.Tab.Password.class, this::validatePassword);
		validators.put(Dialog.Tab.RadioBox.class, this::validateOptionBox);
		validators.put(Dialog.Tab.CheckBox.class, this::validateOptionBox);
		validators.put(Dialog.Tab.ComboBox.class, this::validateOptionBox);
		validators.put(Dialog.Tab.File.class, this::validateResource);
		validators.put(Dialog.Tab.Picture.class, this::validateResource);
		validators.put(Dialog.Tab.Date.class, this::validateDate);
		validators.put(Dialog.Tab.DateTime.class, this::validateDateTime);
	}

	public <B extends Box> B box() {
		return (B) box;
	}

	public Dialog dialog() {
		return dialog;
	}

	public void dialog(Dialog dialog) {
		this.dialog = dialog;
		fillDefaultValues();
	}

	@Override
	public void init() {
		prepare();
		notifier.render(DialogBuilder.build(dialog));
	}

	public void saveValue(DialogInput dialogInput) {
		dialog.register(dialogInput.path(), dialogInput.value());
		refresh(dialog.input(dialogInput.path()), dialogInput.path());
	}

	public void addValue(String path) {
	}

	public void removeValue(String path) {
		dialog.unRegister(path);
		refresh(dialog.input(path), path);
	}

	private void refresh(Dialog.Tab.Input input, String path) {
		DialogValidator.Result result = validate(new FormInput() {
			@Override
			public Dialog.Tab.Input input() {
				return input;
			}

			@Override
			public String path() {
				return path;
			}
		});
		if (result == null) return;
		notifier.refresh(ValidationBuilder.build(input.name(), result));
	}

	public void uploadResource(DialogInputResource dialogInput) {
		dialog.register(dialogInput.path(), dialogInput.resource());
		refresh(dialog.input(dialogInput.path()), dialogInput.path());
	}

	public io.intino.konos.alexandria.foundation.activity.spark.ActivityFile downloadResource(String path) {
		io.intino.konos.alexandria.foundation.activity.schemas.Resource resource = dialog.input(path).value().asResource();

		return new io.intino.konos.alexandria.foundation.activity.spark.ActivityFile() {
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

	public void execute(String name) {
		Dialog.Toolbar.Operation operation = dialog.operation(name);
		notifier.done(operation.execute(username()).toString());
	}

	protected String username() {
		User user = session().user();
		return user != null ? user.username() : null;
	}

	public abstract void prepare();

	private DialogValidator.Result validate(FormInput formInput) {
		DialogValidator.Result result = formInput.input().validate();
		if (result != null) return result;

		return validators.get(formInput.input().getClass()).apply(formInput);
	}

	private DialogValidator.Result validateText(FormInput formInput) {
		List<String> values = dialog.input(formInput.path()).values().asString();
		Dialog.Tab.Text text = (Dialog.Tab.Text)formInput.input();

		DialogValidator.Result result = text.validateEmail(values);
		if (result != null) return result;

		result = text.validateAllowedValues(values);
		if (result != null) return result;

		return text.validateLength(values);
	}

	private DialogValidator.Result validateSection(FormInput formInput) {
		return null;
	}

	private DialogValidator.Result validateMemo(FormInput formInput) {
		return null;
	}

	private DialogValidator.Result validatePassword(FormInput formInput) {
		List<String> values = dialog.input(formInput.path()).values().asString();
		Dialog.Tab.Password password = (Dialog.Tab.Password)formInput.input();
		return password.validateLength(values);
	}

	private DialogValidator.Result validateOptionBox(FormInput formInput) {
		return null;
	}

	private DialogValidator.Result validateResource(FormInput formInput) {
		Dialog.Tab.Resource resource = (Dialog.Tab.Resource)formInput.input();
		List<io.intino.konos.alexandria.foundation.activity.schemas.Resource> resourceValues = dialog.input(formInput.path()).values().asResource();
		Map<String, byte[]> valuesMap = resourceValues.stream().collect(toMap(io.intino.konos.alexandria.foundation.activity.schemas.Resource::name, o -> Base64.decodeBase64(o.value())));

		DialogValidator.Result result = resource.validateMaxSize(valuesMap);
		if (result != null) return result;

		return resource.validateExtension(new ArrayList<>(valuesMap.keySet()));
	}

	private DialogValidator.Result validateDate(FormInput formInput) {
		return null;
	}

	private DialogValidator.Result validateDateTime(FormInput formInput) {
		return null;
	}

	private void fillDefaultValues() {
		inputs(dialog).stream().filter(input -> input.defaultValue() != null && (input.defaultValue() instanceof String) && !((String) input.defaultValue()).isEmpty())
				.forEach(input -> dialog.register(input.name(), singletonList(input.defaultValue())));
	}

	private List<Dialog.Tab.Input> inputs(Dialog dialog) {
		return dialog.tabList().stream().map(Dialog.Tab::inputList).flatMap(Collection::stream).collect(toList());
	}

	interface FormInput {
		Dialog.Tab.Input input();
		String path();
	}
}