package io.intino.konos.alexandria.ui.displays.adapters.gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.intino.konos.alexandria.ui.model.dialog.Structure;
import io.intino.konos.alexandria.ui.schemas.DialogInputResource;

public class ValueAdapter {

    public static String value(Object value) {
        if (value == null) return "";
        if (value instanceof String) return (String) value;
        if (value instanceof DialogInputResource) return ((DialogInputResource)value).file().data();
        if (value instanceof Structure) return structureValue((Structure)value);
        return String.valueOf(value);
    }

    private static String structureValue(Structure value) {
        JsonObject result = new JsonObject();
        value.forEach((key, itemValue) -> result.add(key, new JsonPrimitive(value(itemValue != null ? itemValue.asObject() : null))));
        return result.toString();
    }
}
