package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.ComboBox;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;

public class ComboBoxInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof ComboBox)) return;
        OptionBoxInputAdapter.adapt(result, input);
    }

}
