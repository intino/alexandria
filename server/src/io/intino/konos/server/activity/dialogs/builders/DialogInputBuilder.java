package io.intino.konos.server.activity.dialogs.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;
import io.intino.konos.server.activity.dialogs.adapters.*;

public class DialogInputBuilder {

    public static JsonObject build(Input input) {
        JsonObject result = new JsonObject();

        result.addProperty("label", input.label());
        result.addProperty("type", input.getClass().getSimpleName().toLowerCase());
        result.addProperty("required", input.required());
        result.addProperty("readonly", input.readonly());
        result.addProperty("helper", input.helper());
        result.addProperty("defaultValue", value(input.defaultValue()));
        result.addProperty("placeholder", input.placeholder());
        result.add("value", values(input));

        if (input.isMultiple())
            result.add("multiple", multiple(input));

        TextInputAdapter.adapt(result, input);
        MemoInputAdapter.adapt(result, input);
        PasswordInputAdapter.adapt(result, input);
        RadioBoxInputAdapter.adapt(result, input);
        CheckBoxInputAdapter.adapt(result, input);
        ComboBoxInputAdapter.adapt(result, input);
        FileInputAdapter.adapt(result, input);
        PictureInputAdapter.adapt(result, input);
        DateInputAdapter.adapt(result, input);
        DateTimeInputAdapter.adapt(result, input);
        SectionInputAdapter.adapt(result, input);

        return result;
    }

    private static JsonObject multiple(Input input) {
        JsonObject result = new JsonObject();
        result.addProperty("min", input.multiple().min());
        result.addProperty("max", input.multiple().max());
        return result;
    }

    private static String value(Object value) {
        return value != null && (value instanceof String) ? (String) value : "";
    }

    private static JsonElement values(Input input) {
        if (!input.isMultiple())
            return new JsonPrimitive(value(input.value()));

        JsonArray result = new JsonArray();
        input.values().forEach(value -> result.add(value(value)));

        return result;
    }

}
