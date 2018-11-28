package io.intino.alexandria.ui.displays.adapters;

import com.google.gson.JsonObject;
import io.intino.alexandria.ui.model.Dialog;

public class ComboBoxInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.ComboBox)) return;
        OptionBoxInputAdapter.adapt(result, input);
    }

}
