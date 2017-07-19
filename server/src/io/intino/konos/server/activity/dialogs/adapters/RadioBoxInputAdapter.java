package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.RadioBox;

public class RadioBoxInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof RadioBox)) return;
        OptionBoxInputAdapter.adapt(result, input);
    }

}
