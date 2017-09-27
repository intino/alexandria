package io.intino.konos.server.activity.dialogs;

import io.intino.konos.server.activity.dialogs.schemas.Resource;

public class Value {
    private Object value;

    public Value(Object value) {
        this.value = value;
    }

    public String asString() {
        return (String) value;
    }


    public boolean asBoolean() {
        if (value instanceof Boolean) return (boolean) value;
        return Boolean.valueOf((String) value);
    }

    public int asInteger() {
        if (value instanceof Integer) return (Integer) value;
        return Integer.valueOf((String) value);
    }

    public double asDouble() {
        if (value instanceof Double) return (Double) value;
        return Double.valueOf((String) value);
    }

    public Resource asResource() {
        if (value instanceof Resource) return (Resource) value;
        return (Resource) value;
    }

    public Object asObject() {
        return value;
    }

    public String toString() {
        if (value instanceof String) return (String) value;
        if (value instanceof Resource) return ((Resource) value).value();
        return String.valueOf(value);
    }
}
