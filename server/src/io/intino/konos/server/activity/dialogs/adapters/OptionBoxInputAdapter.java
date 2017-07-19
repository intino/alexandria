package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.OptionBox;

import java.util.List;

public class OptionBoxInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof OptionBox)) return;
        OptionBox optionBox = (OptionBox)input;
        List<String> options = optionBox.options();
        result.add("options", new Gson().toJsonTree(options));
    }

}
