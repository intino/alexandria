package io.intino.konos.alexandria.ui.displays.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.alexandria.ui.model.Dialog;

public class RadioBoxInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.RadioBox)) return;
        OptionBoxInputAdapter.adapt(result, input);
    }

}
