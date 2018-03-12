package io.intino.konos.alexandria.activity.displays.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.intino.konos.alexandria.activity.displays.adapters.*;
import io.intino.konos.alexandria.activity.displays.adapters.gson.ValueAdapter;
import io.intino.konos.alexandria.activity.model.Dialog;
import io.intino.konos.alexandria.activity.model.dialog.Value;

public class DialogInputBuilder {

    public static JsonObject build(Dialog.Tab.Input input) {
        JsonObject result = new JsonObject();

        result.addProperty("name", input.name());
        result.addProperty("label", input.label());
        result.addProperty("type", input.getClass().getSimpleName().toLowerCase());
        result.addProperty("required", input.required());
        result.addProperty("readonly", input.readonly());
        result.addProperty("visible", input.visible());
        result.addProperty("helper", input.helper());
        result.addProperty("defaultValue", value(input.defaultValue()));

        if (input.placeholder() != null)
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

    private static JsonObject multiple(Dialog.Tab.Input input) {
        JsonObject result = new JsonObject();
        result.addProperty("min", input.multiple().min());
        result.addProperty("max", input.multiple().max());
        return result;
    }

    private static String value(Object value) {
        if (value instanceof Value)
            return ValueAdapter.value(((Value) value).asObject());
        if (value instanceof String)
            return (String) value;
        return "";
    }

    private static JsonElement values(Dialog.Tab.Input input) {
        if (!input.isMultiple())
            return new JsonPrimitive(value(input.value()));

        JsonArray result = new JsonArray();
        input.values().forEach(value -> result.add(new JsonPrimitive(value(value))));

        return result;
    }

}
