package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.CheckBox;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;

public class CheckBoxInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof CheckBox)) return;
        CheckBox checkBox = (CheckBox)input;
        result.addProperty("mode", checkBox.mode().toString());
        OptionBoxInputAdapter.adapt(result, input);
    }

}
