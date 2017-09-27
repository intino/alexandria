package io.intino.konos.server.activity.dialogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.*;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Date;
import io.intino.konos.server.activity.dialogs.DialogValidator.Result;
import io.intino.konos.server.activity.dialogs.builders.DialogBuilder;
import io.intino.konos.server.activity.dialogs.builders.ValidationBuilder;
import io.intino.konos.server.activity.dialogs.schemas.DialogInput;
import io.intino.konos.server.activity.dialogs.schemas.DialogInputResource;
import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.services.push.User;
import io.intino.ness.inl.Message;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.codec.binary.Base64.decodeBase64;

public abstract class DialogDisplay extends Display<DialogNotifier> {
	private Box box;
	private Map<Class<? extends Input>, Function<FormInput, Result>> validators = new HashMap<>();
	private Dialog dialog;

	public DialogDisplay(Box box, Dialog dialog) {
        super();
        this.box = box;
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

	private void refresh(Input input, String path) {
		Result result = validate(new FormInput() {
			@Override
			public Input input() {
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

	public io.intino.konos.server.activity.spark.ActivityFile downloadResource(String path) {
		io.intino.konos.server.activity.dialogs.schemas.Resource resource = dialog.input(path).value().asResource();

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
		Message message = new Message(assertionName());
		User user = user();

		message.ts(Instant.now().toString());
		if (user != null) message.write("user", user.username());
		message.write("context", dialog.context());
		message.write("form", dialog.serialize());

		notifier.done(update(message).toString());
	}

	private User user() {
		if (session() == null) return null;
		return session().user();
	}

	public abstract void prepare();
	public abstract Modification update(Message message);

	public enum Modification {
		ItemModified, CatalogModified
	}

	protected abstract String assertionName();

	private Result validate(FormInput formInput) {
		Result result = formInput.input().validate();
		if (result != null) return result;

		return validators.get(formInput.input().getClass()).apply(formInput);
	}

	private Result validateText(FormInput formInput) {
		List<String> values = dialog.input(formInput.path()).values().asString();
		Text text = (Text)formInput.input();

		Result result = text.validateEmail(values);
		if (result != null) return result;

		result = text.validateAllowedValues(values);
		if (result != null) return result;

		return text.validateLength(values);
	}

	private Result validateSection(FormInput formInput) {
		return null;
	}

	private Result validateMemo(FormInput formInput) {
		return null;
	}

	private Result validatePassword(FormInput formInput) {
		List<String> values = dialog.input(formInput.path()).values().asString();
		Password password = (Password)formInput.input();
		return password.validateLength(values);
	}

	private Result validateOptionBox(FormInput formInput) {
		return null;
	}

	private Result validateResource(FormInput formInput) {
		Resource resource = (Resource)formInput.input();
		List<io.intino.konos.server.activity.dialogs.schemas.Resource> resourceValues = dialog.input(formInput.path()).values().asResource();
		Map<String, byte[]> valuesMap = resourceValues.stream().collect(toMap(io.intino.konos.server.activity.dialogs.schemas.Resource::name, o -> Base64.decodeBase64(o.value())));

		Result result = resource.validateMaxSize(valuesMap);
		if (result != null) return result;

		return resource.validateExtension(new ArrayList<>(valuesMap.keySet()));
	}

	private Result validateDate(FormInput formInput) {
		return null;
	}

	private Result validateDateTime(FormInput formInput) {
		return null;
	}

	private void fillDefaultValues() {
		inputs(dialog).stream().filter(input -> input.defaultValue() != null && (input.defaultValue() instanceof String) && !((String) input.defaultValue()).isEmpty())
							   .forEach(input -> dialog.register(input.name(), singletonList(input.defaultValue())));
	}

	private List<Input> inputs(Dialog dialog) {
		return dialog.tabList().stream().map(Dialog.Tab::inputList).flatMap(Collection::stream).collect(toList());
	}

	interface FormInput {
		Input input();
		String path();
	}
}