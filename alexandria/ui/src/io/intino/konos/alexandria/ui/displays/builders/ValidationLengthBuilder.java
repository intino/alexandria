package io.intino.konos.alexandria.ui.displays.builders;

import com.google.gson.JsonObject;
import io.intino.konos.alexandria.ui.model.Dialog;

public class ValidationLengthBuilder {

    public static JsonObject build(Dialog.Tab.Text.Validation.Length length) {
        if (length == null) return new JsonObject();
        return build(length.min(), length.max());
    }

    public static JsonObject build(Dialog.Tab.Password.Validation.Length length) {
        if (length == null) return new JsonObject();
        return build(length.min(), length.max());
    }

    private static JsonObject build(int min, int max) {
        JsonObject result = new JsonObject();
        result.addProperty("min", min);
        result.addProperty("max", max);
        return result;
    }
}
