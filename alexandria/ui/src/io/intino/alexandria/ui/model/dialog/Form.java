package io.intino.alexandria.ui.model.dialog;

import io.intino.alexandria.ui.model.Dialog;
import io.intino.alexandria.ui.utils.NumberUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Form {
    private transient final TypeResolver typeResolver;
    private Map<String, List<Input>> inputsMap = new HashMap<>();

    private static final String AlphaAndDigits = "[^a-zA-Z0-9]+";

    public Form(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    public Map<String, List<Input>> inputs() {
        return inputsMap;
    }

    public List<Input> inputs(String path) {
        String[] names = path.split(Dialog.PathSeparatorRegExp);
        String name = names[names.length-1];

        if (NumberUtil.isNumber(name))
            return singletonList(input(path));

        Input parent = find(String.join(Dialog.PathSeparator, Arrays.copyOfRange(names, 0, names.length-1)));
        if (parent == null) return inputsMap.get(normalizeName(path));
        return parent instanceof Section ? ((Section)parent).inputs(names[names.length-1]) : emptyList();
    }

    public Input input(String path) {
        return find(path);
    }

    public Input input(String name, int pos) {
        name = normalizeName(name);
        if (!inputsMap.containsKey(name)) return null;
        if (inputsMap.get(name).size() <= pos) return null;
        return inputsMap.get(name).get(pos);
    }

    private boolean exists(String name, int pos) {
        name = normalizeName(name);
        return inputsMap.containsKey(name) && inputsMap.get(name).size() > pos && inputsMap.get(name).get(pos) != null;
    }

    private Input find(String path) {
        String[] names = path.split(Dialog.PathSeparatorRegExp);
        int position = position(path);
        Input input = input(names[0], position);
        return input != null ? input.find(Arrays.copyOfRange(names, isMultiple(names) ? 2 : 1, names.length)) : null;
    }

    public Input register(String path, Object value) {
        String[] names = path.split(Dialog.PathSeparatorRegExp);
        if (names.length == 0) return null;

        int position = position(path);
        if (!exists(names[0], position)) register(names[0], position, Input.create(path, typeResolver));

        Input input = input(names[0], position);
        return input.register(Arrays.copyOfRange(names, isMultiple(names) ? 2 : 1, names.length), value);
    }

    public void unRegister(String path) {
        String[] pathArray = path.split(Dialog.PathSeparatorRegExp);

        if (pathArray.length <= 1) {
            remove(path, position(path));
            return;
        }

        String parentPath = String.join(Dialog.PathSeparator, Arrays.copyOfRange(pathArray, 0, pathArray.length-1));
        if (inputsMap.containsKey(parentPath)) {
            if (isMultiple(path)) {
                inputsMap.get(parentPath).remove(position(path));
                if (inputsMap.get(parentPath).size() <= 0)
                    inputsMap.remove(parentPath);
            }
            else inputsMap.remove(parentPath);
            return;
        }

        Input input = find(parentPath);
        if (input != null) input.remove(pathArray[pathArray.length-1], position(path));
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

    public static Form fromMap(Map<String, Object> paths, TypeResolver resolver) {
        Form form = new Form(resolver);
        paths.entrySet().forEach(entry -> form.register(entry.getKey(), entry.getValue()));
        return form;
    }

    public static class Input {
        private transient String name;
        private Value value;
        protected transient TypeResolver typeResolver;

        public Input(String name, TypeResolver typeResolver) {
            this.name = name;
            this.typeResolver = typeResolver;
        }

        public Input register(String[] path, Object value) {
            this.value = new Value(value);
            return this;
        }

        protected void register(Object value) {
            this.value = new Value(value);
        }

        public void remove(String name, int pos) {
            this.value = null;
        }

        public String name() {
            return name;
        }

        public String type() {
            return typeResolver.type(this);
        }

        public Value value() {
            return value;
        }

        public Input value(Object value) {
            this.value = new Value(value);
            return this;
        }

        private static Input create(String path, TypeResolver resolver) {
            String[] names = path.split(Dialog.PathSeparatorRegExp);
            String type = resolver.type(names[0]);
            return type.toLowerCase().equals("section") ? new Section(names[0], resolver) : new Input(names[0], resolver);
        }

        private static Input create(String[] path, TypeResolver resolver) {
            return create(String.join(Dialog.PathSeparator, path), resolver);
        }

        public Input find(String[] path) {
            return this;
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
            return find(path.split(Dialog.PathSeparatorRegExp));
        }

        public Input input(String name, int pos) {
            name = normalizeName(name);
            if (!inputsMap.containsKey(name)) return null;
            if (inputsMap.get(name).size() <= pos) return null;
            return inputsMap.get(name).get(pos);
        }

        @Override
        public Value value() {
            Structure structure = structure();
            return structure.size() > 0 ? new Value(structure) : null;
        }

        private Structure structure() {
            Structure result = new Structure();
            inputsMap.values().forEach(inputs -> result.put(inputs.get(0).name(), inputs.get(0).value()));
            return result;
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
            if (path.length == 0) return this;

            int position = position(path);
            if (!exists(path[0], position)) register(path[0], position, Input.create(path, typeResolver));

            Input input = input(path[0], position);
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
        //if (!isSection(path)) return 0;

        Matcher matcher = Pattern.compile("^[^\\.]*\\.([0-9]+)").matcher(path);
        if (!matcher.find()) return 0;

        return Integer.valueOf(matcher.group(1));
    }

    private static int position(String[] path) {
        return position(String.join(Dialog.PathSeparator, path));
    }

    private static boolean isMultiple(String path) {
        return Pattern.compile("^[^\\.]*\\.[0-9]+").matcher(path).find();
    }

    private static boolean isMultiple(String[] path) {
        return isMultiple(String.join(Dialog.PathSeparator, path));
    }

    private static boolean isSection(String path) {
        return Pattern.compile("\\.[^0-9]+").matcher(path).find();
    }

    private static boolean isSection(String[] path) {
        return isSection(String.join(Dialog.PathSeparator, path));
    }

    private static void fillInputList(List<Input> inputs, int pos) {
        IntStream.range(0, pos+1).forEach(i -> {
            if (inputs.size() <= i)
                inputs.add(null);
        });
    }

    public interface TypeResolver {
        String type(Input input);
        String type(String inputName);
    }
}
