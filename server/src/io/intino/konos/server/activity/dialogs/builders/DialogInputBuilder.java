package io.intino.konos.server.activity.dialogs.builders;

import com.google.gson.JsonObject;
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
        result.addProperty("value", value(input));

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

        return result;
    }

    private static String value(Object value) {
        return value != null && (value instanceof String) ? (String) value : "";
    }

}
