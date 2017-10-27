package io.intino.konos.server.activity.displays.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Input;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.RadioBox;

public class RadioBoxInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof RadioBox)) return;
        OptionBoxInputAdapter.adapt(result, input);
    }

}
