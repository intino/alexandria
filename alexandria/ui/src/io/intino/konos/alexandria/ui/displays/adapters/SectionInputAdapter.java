package io.intino.konos.alexandria.ui.displays.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.intino.konos.alexandria.ui.displays.builders.DialogInputBuilder;
import io.intino.konos.alexandria.ui.model.Dialog;

import java.util.List;

public class SectionInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.Section)) return;
        Dialog.Tab.Section section = (Dialog.Tab.Section)input;
        result.add("inputList", jsonInputListOf(section.inputList()));
    }

    private static JsonArray jsonInputListOf(List<Dialog.Tab.Input> inputList) {
        JsonArray result = new JsonArray();
        inputList.forEach(input -> result.add(jsonInputOf(input)));
        return result;
    }

    private static JsonObject jsonInputOf(Dialog.Tab.Input input) {
        return DialogInputBuilder.build(input);
    }

}
