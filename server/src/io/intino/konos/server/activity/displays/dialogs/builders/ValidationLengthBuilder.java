package io.intino.konos.server.activity.displays.dialogs.builders;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Password;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Text;

public class ValidationLengthBuilder {

    public static JsonObject build(Text.Validation.Length length) {
        if (length == null) return new JsonObject();
        return build(length.min(), length.max());
    }

    public static JsonObject build(Password.Validation.Length length) {
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
