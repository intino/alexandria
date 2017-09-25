package io.intino.konos.server.activity.dialogs;

import com.google.gson.GsonBuilder;
import io.intino.konos.Box;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.*;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Date;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Resource;
import io.intino.konos.server.activity.dialogs.DialogValidator.Result;
import io.intino.konos.server.activity.dialogs.builders.DialogBuilder;
import io.intino.konos.server.activity.dialogs.builders.ValidationBuilder;
import io.intino.konos.server.activity.dialogs.schemas.*;
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
	private Form form;
	private Map<Class<? extends Input>, Function<Input, Result>> validators = new HashMap<>();
	protected Dialog dialog;

	public DialogDisplay(Box box, Dialog dialog) {
        super();
        this.box = box;
        this.form = new Form(dialog.context(), input -> dialog.input(input.name()).getClass().getSimpleName().toLowerCase());
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

	public void dialog(Dialog dialog) {
		this.dialog = dialog;
		this.dialog.valuesManager(new DialogValuesManager() {
			@Override
			public List<Object> values(Input input) {
				Form.Input formInput = form.input(input.name());
				return formInput != null ? formInput.values().asObject() : singletonList("");
			}

			@Override
			public void values(Input input, List<Object> values) {
				form.input(input.name()).register(values);
			}
		});
		fillDefaultValues();
	}

	@Override
    public void init() {
    	prepare(dialog);
		notifier.render(DialogBuilder.build(dialog));
	}

	public void saveValue(DialogInput dialogInput) {
		form.register(dialogInput.path(), dialogInput.value());
		refresh(dialog.input(dialogInput.path()));
	}

	public void addValue(String path) {
		form.register(path, null);
		refresh(dialog.input(path));
	}

	public void removeValue(String path) {
		form.unRegister(path);
		refresh(dialog.input(path));
	}

	private void refresh(Input input) {
		Result result = validate(input);
		if (result == null) return;
		notifier.refresh(ValidationBuilder.build(input.name(), result));
	}

	public void uploadResource(DialogInputResource dialogInput) {
		form.register(dialogInput.path(), dialogInput.resource());
		refresh(dialog.input(dialogInput.path()));
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
		Message message = new Message(assertionName());
		User user = user();

		message.ts(Instant.now().toString());
		if (user != null) message.write("user", user.username());
		message.write("context", dialog.context());
		message.write("form", new GsonBuilder().setPrettyPrinting().create().toJson(form));

		notifier.done(update(message).toString());
	}

	private User user() {
		if (session() == null) return null;
		return session().user();
	}

	public abstract void prepare(Dialog dialog);
	public abstract Modification update(Message message);

	public enum Modification {
		ItemModified, CatalogModified
	}

	protected abstract String assertionName();

	private Result validate(Input input) {
		Result result = input.validate();
		if (result != null) return result;

		return validators.get(input.getClass()).apply(input);
	}

	private Result validateText(Input input) {
		List<String> values = form.input(input.name()).values().asString();
		Text text = (Text)input;

		Result result = text.validateEmail(values);
		if (result != null) return result;

		result = text.validateAllowedValues(values);
		if (result != null) return result;

		return text.validateLength(values);
	}

	private Result validateSection(Input input) {
		return null;
	}

	private Result validateMemo(Input input) {
		return null;
	}

	private Result validatePassword(Input input) {
		List<String> values = form.input(input.name()).values().asString();
		Password password = (Password)input;
		return password.validateLength(values);
	}

	private Result validateOptionBox(Input input) {
		return null;
	}

	private Result validateResource(Input input) {
		Resource resource = (Resource)input;
		List<io.intino.konos.server.activity.dialogs.schemas.Resource> resourceValues = form.input(input.name()).values().asResource();
		Map<String, byte[]> valuesMap = resourceValues.stream().collect(toMap(io.intino.konos.server.activity.dialogs.schemas.Resource::name, o -> Base64.decodeBase64(o.value())));

		Result result = resource.validateMaxSize(valuesMap);
		if (result != null) return result;

		return resource.validateExtension(new ArrayList<>(valuesMap.keySet()));
	}

	private Result validateDate(Input input) {
		return null;
	}

	private Result validateDateTime(Input input) {
		return null;
	}

	private void fillDefaultValues() {
		inputs(dialog).stream().filter(input -> input.defaultValue() != null && (input.defaultValue() instanceof String) && !((String) input.defaultValue()).isEmpty())
							   .forEach(input -> form.register(input.name(), singletonList(input.defaultValue())));
	}

	private List<Input> inputs(Dialog dialog) {
		return dialog.tabList().stream().map(Dialog.Tab::inputList).flatMap(Collection::stream).collect(toList());
	}

}