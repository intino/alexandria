package io.intino.konos.alexandria.activity.box.displays.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.intino.konos.alexandria.activity.box.model.Dialog;

import java.util.List;

public class OptionBoxInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.OptionBox)) return;
        Dialog.Tab.OptionBox optionBox = (Dialog.Tab.OptionBox)input;
        List<String> options = optionBox.options();
        result.add("options", new Gson().toJsonTree(options));
    }

}
