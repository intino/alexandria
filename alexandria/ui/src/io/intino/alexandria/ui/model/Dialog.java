package io.intino.alexandria.ui.model;

import com.google.gson.GsonBuilder;
import io.intino.alexandria.ui.displays.adapters.gson.FormAdapter;
import io.intino.alexandria.ui.displays.DialogExecution;
import io.intino.alexandria.ui.displays.DialogSource;
import io.intino.alexandria.ui.displays.DialogValidator;
import io.intino.alexandria.ui.model.dialog.DialogResult;
import io.intino.alexandria.ui.model.dialog.Form;
import io.intino.alexandria.ui.model.dialog.Value;
import io.intino.alexandria.ui.model.dialog.Values;
import io.intino.alexandria.ui.services.push.UISession;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class Dialog {
    private String url;
    private String label;
    private String description;
    private TabsMode mode = TabsMode.Tabs;
    private boolean readonly;
    private List<Tab> tabList = new ArrayList<>();
    private Toolbar toolbar = new Toolbar();
    private Object target = null;
    private final Form form;

    public Dialog() {
        this.label = "";
        this.description = "";
        this.form = new Form(new Form.TypeResolver() {
            @Override
            public String type(Form.Input input) {
                return type(input.name());
            }

            @Override
            public String type(String inputName) {
                Tab.Input input = inputs().stream().filter(i -> i.name().equals(inputName) || i.label().equals(inputName)).findFirst().orElse(null);
                return input != null ? input.getClass().getSimpleName().toLowerCase() : null;
            }
        });
    }

    protected Dialog(Form form) {
        this.label = "";
        this.description = "";
        this.form = form;
    }

    public enum TabsMode { Tabs, Wizard }
    public enum TextEdition { Normal, Uppercase, Lowercase, Email, Url }
    public enum MemoMode { Raw, Rich }
    public enum PasswordRequired { Letter, Number, Symbol }
    public enum CheckBoxMode { Boolean, List }

    public static final String PathSeparatorRegExp = "\\.";
    public static final String PathSeparator = ".";

    public String url() {
        return url;
    }

    public Dialog url(String url) {
        this.url = url;
        return this;
    }

    public String label() {
        return label;
    }

    public Dialog label(String label) {
        this.label = label;
        return this;
    }

    public String description() {
        return description;
    }

    public Dialog description(String description) {
        this.description = description;
        return this;
    }

    public TabsMode mode() {
        return this.mode;
    }

    public Dialog mode(TabsMode mode) {
        this.mode = mode;
        return this;
    }

    public boolean readonly() {
        return readonly;
    }

    public Dialog readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public <T extends Object> T target() {
        return (T) this.target;
    }

    public Dialog target(Object target) {
        this.target = target;
        return this;
    }

    public Toolbar toolbar() {
        return this.toolbar;
    }

    public Tab createTab(String label) {
        Tab tab = new Tab(label);
        this.tabList().add(tab);
        return tab;
    }

    public Toolbar.Operation operation(String label) {
        return toolbar.operation(label);
    }

    public List<Tab> tabList() {
        return tabList;
    }

    public <I extends Tab.Input> I input(String path) {
        String key = formInput(path).name();
        Tab.Input result = inputs().stream()
                               .filter(input -> input.name().equals(key) || input.label().equals(key))
                               .findFirst().orElse(null);
        if (result == null) return null;
        result.path(path);
        return (I)result;
    }

    private List<Tab.Input> inputs() {
        return tabList().stream().map(Tab::inputList)
                        .flatMap(Collection::stream).map(this::inputs)
                        .flatMap(Collection::stream).collect(toList());
    }

    private List<Tab.Input> inputs(Tab.Input input) {
        if (!(input instanceof Tab.Section)) return singletonList(input);
        List<Tab.Input> result = new ArrayList<>();
        result.add(input);
        ((Tab.Section)input).inputList.forEach(child -> result.addAll(inputs(child)));
        return result;
    }

    private Form.Input formInput(String path) {
        Form.Input input = form.input(path);
        if (input == null) input = form.register(path, null);
        return input;
    }

    public void register(String path, Object value) {
        form.register(path, value);
    }

    public void unRegister(String path) {
        form.unRegister(path);
    }

    public String serialize() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Form.class, new FormAdapter());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create().toJson(form);
    }

    public class Toolbar {
        private List<Operation> operationList = new ArrayList<>();

        public List<Operation> operationList() {
            return operationList;
        }

        public Operation createOperation() {
            return add(new Operation());
        }

        public Operation operation(String key) {
            return operationList.stream().filter(o -> o.name().equals(key) || o.label().equals(key)).findFirst().orElse(null);
        }

        public class Operation {
            private String name;
            private String label;
            private boolean closeAfterExecution = true;
            private DialogExecution launcher = null;

            public String name() {
                return name;
            }

            public Operation name(String name) {
                this.name = name;
                return this;
            }

            public boolean closeAfterExecution() {
                return closeAfterExecution;
            }

            public Operation closeAfterExecution(boolean value) {
                this.closeAfterExecution = value;
                return this;
            }

            public String label() {
                return label;
            }

            public Operation label(String label) {
                this.label = label;
                return this;
            }

            public Operation execute(DialogExecution launcher) {
                this.launcher = launcher;
                return this;
            }

            public DialogResult execute(UISession session) {
                if (launcher == null) return DialogResult.item();
                return launcher.execute(this, session);
            }
        }

        private Operation add(Operation input) {
            operationList.add(input);
            return input;
        }
    }

    public class Tab {
        private String label;
        private List<Input> inputList = new ArrayList<>();

        public Tab(String label) {
            this.label(label);
        }

        public String label() {
            return label;
        }

        public Tab label(String label) {
            this.label = label;
            return this;
        }

        public List<Input> inputList() {
            return inputList;
        }

        public Text createText() {
            return add(new Text());
        }

        public Section createSection() {
            return add(new Section());
        }

        public Memo createMemo() {
            return add(new Memo());
        }

        public Password createPassword() {
            return add(new Password());
        }

        public RadioBox createRadioBox() {
            return add(new RadioBox());
        }

        public CheckBox createCheckBox() {
            return add(new CheckBox());
        }

        public ComboBox createComboBox() {
            return add(new ComboBox());
        }

        public File createFile() {
            return add(new File());
        }

        public Picture createPicture() {
            return add(new Picture());
        }

        public Date createDate() {
            return add(new Date());
        }

        public DateTime createDateTime() {
            return add(new DateTime());
        }

        private <I extends Input> I add(I input) {
            inputList.add(input);
            return input;
        }

        public class Input {
            private String path;
            private String name = null;
            private String label;
            private boolean required;
            private boolean readonly;
            private boolean visible = true;
            private String placeholder = null;
            private String helper;
            private String defaultValue = null;
            private Multiple multiple = null;
            private DialogValidator validator = null;

            private static final String AlphaAndDigits = "[^a-zA-Z0-9]+";

            public String name() {
                return name != null ? name : clean(label);
            }

            public Input name(String name) {
                this.name = name;
                return this;
            }

            public String path() {
                return path != null ? path : name();
            }

            public Input path(String path) {
                this.path = path;
                return this;
            }

            public String label() {
                return label;
            }

            public Input label(String label) {
                this.label = label;
                return this;
            }

            public boolean required() {
                return required;
            }

            public Input required(boolean required) {
                this.required = required;
                return this;
            }

            public boolean readonly() {
                return readonly;
            }

            public Input readonly(boolean readonly) {
                this.readonly = readonly;
                return this;
            }

            public boolean visible() {
                return visible;
            }

            public Input visible(boolean visible) {
                this.visible = visible;
                return this;
            }

            public String placeholder() {
                return placeholder;
            }

            public Input placeholder(String placeholder) {
                this.placeholder = placeholder;
                return this;
            }

            public String helper() {
                return helper;
            }

            public Input helper(String helper) {
                this.helper = helper;
                return this;
            }

            public <T extends Object> T defaultValue() {
                return (T) defaultValue;
            }

            public Input defaultValue(String defaultValue) {
                this.defaultValue = defaultValue;
                return this;
            }

            public boolean isMultiple() {
                return this.multiple != null;
            }

            public Multiple multiple() {
                return multiple;
            }

            public Input multiple(int min, int max) {
                this.multiple = new Multiple().min(min).max(max);
                return this;
            }

            public Input validator(DialogValidator validator) {
                this.validator = validator;
                return this;
            }

            public DialogValidator.Result validate() {
                if (validator == null) return null;
                return validator.validate(this);
            }

            public Value value() {
                return values().size() > 0 ? values().get(0) : new Value(null);
            }

            public Values values() {
                List<Form.Input> formInputs = form.inputs(path());

                if (formInputs == null || formInputs.size() == 0)
                    return new Values() {{ if (defaultValue() != null) add(new Value(defaultValue())); }};

                Values values = new Values();
                formInputs.stream().map(Form.Input::value).filter(Objects::nonNull).forEach(values::add);

                return values;
            }

            public Input value(Object value) {
                return values(singletonList(value));
            }

            public Input values(List<Object> values) {
                for (int i=0; i<values.size(); i++)
                    form.register(path() + "." + i, values.get(i));
                return this;
            }

            public <I extends Input> I input(String path) {
                return Dialog.this.input(path);
            }

            protected DialogValidator.Result validateLength(String value, int min, int max) {
                if (value == null) return null;
                int length = value.length();

                if (min > 0 && length < min) return new DialogValidator.Result(false, "Value length is lower than " + min);
                if (max > 0 && length > max) return new DialogValidator.Result(false, "Value length is greater than " + max);

                return null;
            }

            private String clean(String value) {
                return value.replaceAll(AlphaAndDigits,"");
            }

            public class Multiple {
                private int min = -1;
                private int max = -1;

                public int min() {
                    return this.min;
                }

                public Multiple min(int min) {
                    this.min = min;
                    return this;
                }

                public int max() {
                    return this.max;
                }

                public Multiple max(int max) {
                    this.max = max;
                    return this;
                }
            }
        }

        public class Text extends Input {
            private TextEdition edition = TextEdition.Normal;
            private Validation validation;

            public TextEdition edition() {
                return edition;
            }

            public Text edition(TextEdition edition) {
                this.edition = edition;
                return this;
            }

            public Validation validation() {
                return validation;
            }

            public Text validation(Validation validation) {
                this.validation = validation;
                return this;
            }

            public DialogValidator.Result validateEmail(List<String> values) {
                for (Object value : values) {
                    if (value == null) continue;
                    DialogValidator.Result result = validateEmail((String)value);
                    if (result != null) return result;
                }
                return null;
            }

            public DialogValidator.Result validateEmail(String value) {
                if (value == null) return null;
                if (edition != TextEdition.Email) return null;
                String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
                java.util.regex.Matcher m = p.matcher(value);
                return m.matches() ? null : new DialogValidator.Result(false, "Email not valid");
            }

            public DialogValidator.Result validateAllowedValues(List<String> values) {
                for (String value : values) {
                    if (value == null) continue;
                    DialogValidator.Result result = validateAllowedValues(value);
                    if (result != null) return result;
                }
                return null;
            }

            public DialogValidator.Result validateAllowedValues(String value) {
                if (value == null) return null;
                if (validation == null) return null;
                if (validation.allowedValues.size() <= 0 || validation.allowedValues.contains(value)) return null;
                return new DialogValidator.Result(false, "Value not allowed");
            }

            public DialogValidator.Result validateLength(List<String> values) {
                for (String value : values) {
                    if (value == null) continue;
                    DialogValidator.Result result = validateLength(value);
                    if (result != null) return result;
                }
                return null;
            }

            public DialogValidator.Result validateLength(String value) {
                if (value == null) return null;
                if (validation == null) return null;
                if (validation.length() == null) return null;
                return validateLength(value, validation.length.min(), validation.length.max());
            }

            public class Validation {
                private List<String> allowedValues = new ArrayList<>();
                private List<String> disallowedValues = new ArrayList<>();
                private boolean disallowEmptySpaces;
                private Length length;
                private String mask;
                //a - Represents an alpha character (A-Z,a-z)
                //9 - Represents a numeric character (0-9)
                //* - Represents an alphanumeric character (A-Z,a-z,0-9)

                public List<String> allowedValues() {
                    return allowedValues;
                }

                public Validation allowedValues(List<String> allowedValues) {
                    this.allowedValues.addAll(allowedValues);
                    return this;
                }

                public List<String> disallowedValues() {
                    return disallowedValues;
                }

                public Validation disallowedValues(List<String> disallowedValues) {
                    this.disallowedValues = disallowedValues;
                    return this;
                }

                public boolean disallowEmptySpaces() {
                    return disallowEmptySpaces;
                }

                public Validation disallowEmptySpaces(boolean disallowEmptySpaces) {
                    this.disallowEmptySpaces = disallowEmptySpaces;
                    return this;
                }

                public Length length() {
                    return length;
                }

                public Validation length(Length length) {
                    this.length = length;
                    return this;
                }

                public String mask() {
                    return mask;
                }

                public Validation mask(String mask) {
                    this.mask = mask;
                    return this;
                }

                public class Length {
                    private int min;
                    private int max;

                    public int min() {
                        return min;
                    }

                    public Length min(int min) {
                        this.min = min;
                        return this;
                    }

                    public int max() {
                        return max;
                    }

                    public Length max(int max) {
                        this.max = max;
                        return this;
                    }
                }

            }
        }

        public class Section extends Input {
            private List<Input> inputList = new ArrayList<>();

            public List<Input> inputList() {
                return inputList;
            }

            public Text createText() {
                return add(new Text());
            }

            public Section createSection() {
                return add(new Section());
            }

            public Memo createMemo() {
                return add(new Memo());
            }

            public Password createPassword() {
                return add(new Password());
            }

            public RadioBox createRadioBox() {
                return add(new RadioBox());
            }

            public CheckBox createCheckBox() {
                return add(new CheckBox());
            }

            public ComboBox createComboBox() {
                return add(new ComboBox());
            }

            public File createFile() {
                return add(new File());
            }

            public Picture createPicture() {
                return add(new Picture());
            }

            public Date createDate() {
                return add(new Date());
            }

            public DateTime createDateTime() {
                return add(new DateTime());
            }

            private <I extends Input> I add(I input) {
                inputList.add(input);
                return input;
            }
        }

        public class Memo extends Input {
            private MemoMode mode = MemoMode.Raw;
            private int height;

            public MemoMode mode() {
                return mode;
            }

            public Memo mode(MemoMode mode) {
                this.mode = mode;
                return this;
            }

            public int height() {
                return height;
            }

            public Memo height(int height) {
                this.height = height;
                return this;
            }
        }

        public class Password extends Input {
            private Validation validation;

            public Validation validation() {
                return validation;
            }

            public Password validation(Validation validation) {
                this.validation = validation;
                return this;
            }

            public DialogValidator.Result validateLength(List<String> values) {
                for (String value : values) {
                    if (value == null) continue;
                    DialogValidator.Result result = validateLength(value);
                    if (result != null) return result;
                }
                return null;
            }

            public DialogValidator.Result validateLength(String value) {
                if (value == null) return null;
                if (validation == null) return null;
                if (validation.length() == null) return null;
                return validateLength(value, validation.length.min(), validation.length.max());
            }

            public class Validation {
                private List<PasswordRequired> requiredList = new ArrayList<>();
                private Length length;

                public List<PasswordRequired> requiredList() {
                    return requiredList;
                }

                public Validation requiredList(List<PasswordRequired> requiredList) {
                    this.requiredList.addAll(requiredList);
                    return this;
                }

                public Length length() {
                    return length;
                }

                public Validation length(Length length) {
                    this.length = length;
                    return this;
                }

                public class Length {
                    private int min;
                    private int max;

                    public int min() {
                        return min;
                    }

                    public Length min(int min) {
                        this.min = min;
                        return this;
                    }

                    public int max() {
                        return max;
                    }

                    public Length max(int max) {
                        this.max = max;
                        return this;
                    }
                }
            }
        }

        public class OptionBox extends Input {
            private DialogSource source = null;

            public List<String> options() {
                if (source == null) return emptyList();
                return source.options(this);
            }

            public OptionBox source(DialogSource source) {
                this.source = source;
                return this;
            }
        }

        public class RadioBox extends OptionBox {
        }

        public class CheckBox extends OptionBox {
            private CheckBoxMode mode = CheckBoxMode.Boolean;

            public CheckBoxMode mode() {
                return mode;
            }

            public CheckBox mode(CheckBoxMode mode) {
                this.mode = mode;
                return this;
            }
        }

        public class ComboBox extends OptionBox {
        }

        public class Resource extends Input {
            private boolean showPreview;
            private Validation validation;

            public boolean showPreview() {
                return showPreview;
            }

            public Resource showPreview(boolean showPreview) {
                this.showPreview = showPreview;
                return this;
            }

            public Validation validation() {
                return validation;
            }

            public Resource validation(Validation validation) {
                this.validation = validation;
                return this;
            }

            public DialogValidator.Result validateMaxSize(Map<String, byte[]> values) {
                for (Map.Entry<String, byte[]> value : values.entrySet()) {
                    if (value == null) continue;
                    DialogValidator.Result result = validateMaxSize(value.getKey(), value.getValue());
                    if (result != null) return result;
                }
                return null;
            }

            public DialogValidator.Result validateMaxSize(String filename, byte[] content) {
                if (filename == null || content == null) return null;
                if (validation == null) return null;
                if (content.length <= validation.maxSize) return null;
                return new DialogValidator.Result(false, "File is too long. Max size: " + validation.maxSize);
            }

            public DialogValidator.Result validateExtension(List<Object> values) {
                for (Object value : values) {
                    if (value == null) continue;
                    DialogValidator.Result result = validateExtension((String)value);
                    if (result != null) return result;
                }
                return null;
            }

            public DialogValidator.Result validateExtension(String value) {
                if (value == null) return null;
                if (validation == null) return null;
                List<String> allowedExtensions = validation.allowedExtensions();
                if (allowedExtensions.size() <= 0 || allowedExtensions.contains(value)) return null;
                return new DialogValidator.Result(false, "File extension not allowed. Options: " + String.join(", ", allowedExtensions));
            }

            public class Validation {
                private int maxSize;
                private List<String> allowedExtensions = new ArrayList<>();

                public int maxSize() {
                    return maxSize;
                }

                public Validation maxSize(int maxSize) {
                    this.maxSize = maxSize;
                    return this;
                }

                public List<String> allowedExtensions() {
                    return allowedExtensions;
                }

                public void allowedExtensions(List<String> allowedExtensions) {
                    this.allowedExtensions.addAll(allowedExtensions);
                }
            }
        }

        public class File extends Resource {
        }

        public class Picture extends Resource {
        }

        public class Date extends Input {
            private String format = "dd/MM/yyyy";

            public String format() {
                return format;
            }

            public Date format(String format) {
                this.format = format;
                return this;
            }
        }

        public class DateTime extends Input {
            private String format;

            public String format() {
                return format;
            }

            public DateTime format(String format) {
                this.format = format;
                return this;
            }
        }
    }

}
