package io.intino.konos.alexandria.ui.model.dialog;

import io.intino.konos.alexandria.schema.Resource;
import io.intino.konos.alexandria.ui.schemas.DialogInputResource;
import io.intino.konos.alexandria.ui.schemas.DialogInputResourceFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

public class Value {
    private Object value;

    public Value(Object value) {
        this.value = value;
    }

    public String asString() {
        if (value == null) return "";
        return (String) value;
    }

    public boolean asBoolean() {
        if (value == null) return false;
        if (value instanceof Boolean) return (boolean) value;
        return Boolean.valueOf((String) value);
    }

    public int asInteger() {
        if (value == null) return -1;
        if (value instanceof Integer) return (Integer) value;
        return Integer.valueOf((String) value);
    }

    public double asDouble() {
        if (value == null) return -1;
        if (value instanceof Double) return (Double) value;
        return Double.valueOf((String) value);
    }

    public Resource asResource() {
        if (value == null) return null;
        if (value instanceof Resource) return (Resource) value;
        DialogInputResourceFile fileValue = (DialogInputResourceFile)value;
        String file = fileValue.data();
        InputStream data = new ByteArrayInputStream(decodeBase64(file.split(",")[1].replace(" ", "+")));
        return new Resource(fileValue.name()).data(data);
    }

    public Object asObject() {
        return value;
    }

    public Structure asStructure() {
        if (value == null) return null;
        return (Structure) value;
    }

    public String toString() {
        if (value == null) return "";
        if (value instanceof String) return (String) value;
        if (value instanceof DialogInputResource) return ((DialogInputResource) value).file().data();
        return String.valueOf(value);
    }

}
