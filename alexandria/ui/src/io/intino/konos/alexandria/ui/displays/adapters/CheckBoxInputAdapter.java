package io.intino.konos.alexandria.ui.displays.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.alexandria.ui.model.Dialog;

public class CheckBoxInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.CheckBox)) return;
        Dialog.Tab.CheckBox checkBox = (Dialog.Tab.CheckBox)input;
        result.addProperty("mode", checkBox.mode().toString());
        OptionBoxInputAdapter.adapt(result, input);
    }

}
