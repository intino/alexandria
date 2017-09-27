package io.intino.konos.server.activity.dialogs;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static io.intino.konos.server.activity.dialogs.Dialog.PathSeparator;
import static io.intino.konos.server.activity.dialogs.Dialog.PathSeparatorRegExp;
import static java.util.Collections.emptyList;

public class Form {
    private transient final TypeResolver typeResolver;
    private transient String context = null;
    private Map<String, List<Input>> inputsMap = new HashMap<>();

    private static final String AlphaAndDigits = "[^a-zA-Z0-9]+";

    public Form(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    public String context() {
        return context;
    }

    public Form context(String context) {
        this.context = context;
        return this;
    }

    public Map<String, List<Input>> inputs() {
        return inputsMap;
    }

    public Input input(String path) {
        return find(path);
    }

    public Input input(String name, int pos) {
        name = normalizeName(name);
        return inputsMap.containsKey(name) ? inputsMap.get(name).get(pos) : null;
    }

    public List<Input> inputs(String name) {
        name = normalizeName(name);
        return inputsMap.containsKey(name) ? inputsMap.get(name) : emptyList();
    }

    private boolean exists(String name) {
        return exists(name, 0);
    }

    private boolean exists(String name, int pos) {
        name = normalizeName(name);
        return inputsMap.containsKey(name) && inputsMap.get(name).size() > pos && inputsMap.get(name).get(pos) != null;
    }

    private Input find(String path) {
        String[] names = path.split(PathSeparatorRegExp);
        int position = position(path);
        Input input = input(names[0], position);
        return input != null ? input.find(Arrays.copyOfRange(names, isMultiple(names) ? 2 : 1, names.length)) : null;
    }

    public Input register(String path, Object value) {
        String[] names = path.split(PathSeparatorRegExp);
        if (names.length == 0) return null;

        int position = position(path);
        if (!exists(names[0], position)) register(names[0], position, Input.create(path, typeResolver));

        Input input = input(names[0], position);
        if (!isMultiple(path)) input.clear();
        return input.register(Arrays.copyOfRange(names, isMultiple(names) ? 2 : 1, names.length), value);
    }

    public void unRegister(String path) {
        String[] pathArray = path.split(PathSeparatorRegExp);

        if (pathArray.length <= 1) {
            remove(path, position(path));
            return;
        }

        String parentPath = String.join(PathSeparator, Arrays.copyOfRange(pathArray, 0, pathArray.length-1));
        find(parentPath).remove(pathArray[pathArray.length-1], position(path));
    }

    private void remove(String name, int pos) {
        name = normalizeName(name);
        this.inputsMap.get(name).remove(pos);
        if (this.inputsMap.get(name).size() > 0) return;
        this.inputsMap.remove(name);
    }

    private void register(String name, int pos, Input input) {
        name = normalizeName(name);
        if (!inputsMap.containsKey(name)) inputsMap.put(name, new ArrayList<>());
        fillInputList(inputsMap.get(name), pos);
        inputsMap.get(name).set(pos, input);
    }

    public static Form fromMap(String context, Map<String, Object> paths, TypeResolver resolver) {
        Form form = new Form(resolver);
        form.context(context);
        paths.entrySet().forEach(entry -> form.register(entry.getKey(), entry.getValue()));
        return form;
    }

    public static class Input {
        private transient String name;
        private Values values = new Values();
        protected transient TypeResolver typeResolver;

        public Input(String name, TypeResolver typeResolver) {
            this.name = name;
            this.typeResolver = typeResolver;
        }

        public Input clear() {
            this.values.clear();
            return this;
        }

        public Input register(String[] path, Object value) {
            this.values.add(new Value(value));
            return this;
        }

        protected void register(List<Object> values) {
            this.values.clear();
            values.forEach(v -> values.add(new Value(v)));
        }

        public String name() {
            return name;
        }

        public String type() {
            return typeResolver.type(this);
        }

        public Value value() {
            return values.size() > 0 ? values.get(0) : null;
        }

        public Values values() {
            return values;
        }

        public Input value(Object value) {
            this.values.clear();
            this.values.add(new Value(value));
            return this;
        }

        public Input values(List<Object> values) {
            register(values);
            return this;
        }

        private static Input create(String path, TypeResolver resolver) {
            String[] names = path.split(PathSeparatorRegExp);
            return isSection(path) ? new Section(names[0], resolver) : new Input(names[0], resolver);
        }

        private static Input create(String[] path, TypeResolver resolver) {
            return isSection(path) ? new Section(path[0], resolver) : new Input(path[0], resolver);
        }

        public Input find(String[] path) {
            return this;
        }

        public void remove(String name, int pos) {
            values.remove(pos);
        }
    }

    public static class Section extends Input {
        private Map<String, List<Input>> inputsMap = new HashMap<>();

        public Section(String name, TypeResolver typeResolver) {
            super(name, typeResolver);
        }

        private boolean exists(String name, int pos) {
            name = normalizeName(name);
            return inputsMap.containsKey(name) && inputsMap.get(name).size() > pos && inputsMap.get(name).get(pos) != null;
        }

        public Map<String, List<Input>> inputs() {
            return inputsMap;
        }

        public List<Input> inputs(String name) {
            name = normalizeName(name);
            return inputsMap.get(name);
        }

        public Input input(String path) {
            return find(path.split(PathSeparatorRegExp));
        }

        public Input input(String name, int pos) {
            name = normalizeName(name);
            return inputsMap.containsKey(name) ? inputsMap.get(name).get(pos) : null;
        }

        @Override
        public Input find(String[] path) {
            if (path.length == 0) return this;
            int position = position(path);
            Input input = input(path[0], position);
            return input != null ? input.find(Arrays.copyOfRange(path, isMultiple(path) ? 2 : 1, path.length)) : null;
        }

        @Override
        public Input register(String[] path, Object value) {
            if (path.length == 0) return null;

            int position = position(path);
            if (!exists(path[0], position)) register(path[0], position, Input.create(path, typeResolver));

            Input input = input(path[0], position);
            if (!isMultiple(path)) input.clear();
            return input.register(Arrays.copyOfRange(path, isMultiple(path) ? 2 : 1, path.length), value);
        }

        @Override
        public void remove(String name, int pos) {
            name = normalizeName(name);
            inputsMap.get(name).remove(pos);
            if (inputsMap.get(name).size() > 0) return;
            inputsMap.remove(name);
        }

        private void register(String name, int pos, Input input) {
            name = normalizeName(name);
            if (!inputsMap.containsKey(name)) inputsMap.put(name, new ArrayList<>());
            fillInputList(inputsMap.get(name), pos);
            inputsMap.get(name).set(pos, input);
        }

    }

    private static String normalizeName(String name) {
        return name.replaceAll(AlphaAndDigits, "");
    }

    private static int position(String path) {
        if (!isMultiple(path)) return 0;
        if (!isSection(path)) return 0;

        Matcher matcher = Pattern.compile("^[^\\.]*\\.([0-9]+)").matcher(path);
        if (!matcher.find()) return 0;

        return Integer.valueOf(matcher.group(1));
    }

    private static int position(String[] path) {
        return isSection(path) ? position(String.join(PathSeparator, path)) : 0;
    }

    private static boolean isMultiple(String path) {
        return Pattern.compile("^[^\\.]*\\.[0-9]+").matcher(path).find();
    }

    private static boolean isMultiple(String[] path) {
        return isMultiple(String.join(PathSeparator, path));
    }

    private static boolean isSection(String path) {
        return Pattern.compile("\\.[^0-9]+").matcher(path).find();
    }

    private static boolean isSection(String[] path) {
        return isSection(String.join(PathSeparator, path));
    }

    private static void fillInputList(List<Input> inputs, int pos) {
        IntStream.range(0, pos+1).forEach(i -> {
            if (inputs.size() <= i)
                inputs.add(null);
        });
    }

    public interface TypeResolver {
        String type(Input input);
    }
}
