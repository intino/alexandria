package io.intino.konos.server.activity.displays.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.ComboBox;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Input;

public class ComboBoxInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof ComboBox)) return;
        OptionBoxInputAdapter.adapt(result, input);
    }

}
