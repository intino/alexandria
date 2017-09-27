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
        return Boolean.valueOf((String) value);
    }

    public int asInteger() {
        return Integer.valueOf((String) value);
    }

    public double asDouble() {
        return Double.valueOf((String) value);
    }

    public Resource asResource() {
        return (Resource) value;
    }

    public Object asObject() {
        return value;
    }
}
