package io.intino.konos.server.activity.dialogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.*;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Date;
import io.intino.konos.server.activity.dialogs.DialogValidator.Result;
import io.intino.konos.server.activity.dialogs.builders.DialogBuilder;
import io.intino.konos.server.activity.dialogs.builders.ValidationBuilder;
import io.intino.konos.server.activity.dialogs.schemas.DialogInput;
import io.intino.konos.server.activity.dialogs.schemas.DialogInputResource;
import io.intino.konos.server.activity.dialogs.schemas.DialogInputValueIdentifier;
import io.intino.konos.server.activity.displays.Display;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.codec.binary.Base64.decodeBase64;

public class DialogDisplay extends Display<DialogNotifier> {
	protected Dialog dialog;
	private Map<String, List<Object>> fieldsMap = new HashMap<>();
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
			return fieldsMap.getOrDefault(input.name(), singletonList(""));
		});
		fillDefaultValues();
	}

	@Override
    public void init() {
    	notifier.render(DialogBuilder.build(dialog));
	}

	public void addValue(DialogInput dialogInput) {
		Input input = dialog.input(dialogInput.name());
		register(input, dialogInput.value());
		refresh(input);
	}

	public void removeValue(DialogInputValueIdentifier identifier) {
		Input input = dialog.input(identifier.input());
		if (!fieldsMap.containsKey(input.name())) return;
		if (identifier.position() >= fieldsMap.get(input.name()).size()) return;
		fieldsMap.get(input.name()).remove(identifier.position());
		refresh(input);
	}

	private void register(Input input, Object value) {
		if (!input.isMultiple()) fieldsMap.remove(input.name());
		if (!fieldsMap.containsKey(input.name())) fieldsMap.put(input.name(), new ArrayList<>());
		fieldsMap.get(input.name()).add(value);
	}

	private void refresh(Input input) {
		Result result = validate(input);
		if (result == null) return;
		notifier.refresh(ValidationBuilder.build(input.name(), result));
	}

	public void uploadResource(DialogInputResource inputResource) {
		Input input = dialog.input(inputResource.input());
		register(input, inputResource.resource());
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
//		new Inl();
//		String serialize = Inl.serialize(fieldsMap);
		System.out.println("--> execute dialog");
	}

	private Result validate(Input input) {
		Result result = input.validate();
		if (result != null) return result;

		return validators.get(input.getClass()).apply(input);
	}

	private Result validateText(Input input) {
		List<Object> values = fieldsMap.get(input.name());
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
		List<Object> values = fieldsMap.get(input.name());
		Password password = (Password)input;
		return password.validateLength(values);
	}

	private Result validateOptionBox(Input input) {
		return null;
	}

	private Result validateResource(Input input) {
		Resource resource = (Resource)input;
		List<Object> resourceValues = fieldsMap.get(input.name());
		Map<String, byte[]> valuesMap = resourceValues.stream().collect(toMap(o -> ((io.intino.konos.server.activity.dialogs.schemas.Resource) o).name(),
																			  o -> Base64.decodeBase64(((io.intino.konos.server.activity.dialogs.schemas.Resource) o).value())));

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
							   .forEach(input -> fieldsMap.put(input.name(), singletonList(input.defaultValue())));
	}

	private List<Input> inputs(Dialog dialog) {
		return dialog.tabList().stream().map(Dialog.Tab::inputList).flatMap(Collection::stream).collect(toList());
	}

}