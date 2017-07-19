package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;
import io.intino.konos.server.activity.dialogs.builders.DialogInputBuilder;

import java.util.List;

public class SectionInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof Dialog.Tab.Section)) return;
        Dialog.Tab.Section section = (Dialog.Tab.Section)input;
        result.add("inputList", jsonInputListOf(section.inputList()));
    }

    private static JsonArray jsonInputListOf(List<Input> inputList) {
        JsonArray result = new JsonArray();
        inputList.forEach(input -> result.add(jsonInputOf(input)));
        return result;
    }

    private static JsonObject jsonInputOf(Input input) {
        return DialogInputBuilder.build(input);
    }

}
