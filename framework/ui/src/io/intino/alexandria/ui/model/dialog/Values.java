package io.intino.alexandria.ui.model.dialog;

import io.intino.alexandria.Resource;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Values extends ArrayList<Value> {

    public List<String> asString() {
        return stream().map(Value::asString).collect(toList());
    }

    public List<Boolean> asBoolean() {
        return stream().map(Value::asBoolean).collect(toList());
    }

    public List<Integer> asInteger() {
        return stream().map(Value::asInteger).collect(toList());
    }

    public List<Double> asDouble() {
        return stream().map(Value::asDouble).collect(toList());
    }

    public List<Resource> asResource() {
        return stream().map(Value::asResource).collect(toList());
    }

    public List<Object> asObject() {
        return stream().map(Value::asObject).collect(toList());
    }

    public List<Structure> asStructure() {
        return stream().map(Value::asStructure).collect(toList());
    }
}
