package io.intino.konos.server.activity.displays.dialogs.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Input;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Text;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.TextEdition;
import io.intino.konos.server.activity.displays.dialogs.builders.ValidationLengthBuilder;

public class TextInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof Text)) return;
        Text text = (Text)input;
        result.addProperty("edition", editionOf(text));
        result.add("validation", validation(text));
    }

    private static String editionOf(Text text) {
        TextEdition edition = text.edition();
        if (edition == TextEdition.Normal) return "text";
        return edition.toString().toLowerCase();
    }

    private static JsonObject validation(Text text) {
        JsonObject result = new JsonObject();
        if (text.validation() == null) return result;
        Text.Validation validation = text.validation();

        result.addProperty("mask", validation.mask());
        result.add("allowedValues", new Gson().toJsonTree(validation.allowedValues()));
        result.add("disallowedValues", new Gson().toJsonTree(validation.disallowedValues()));
        result.addProperty("disallowEmptySpaces", validation.disallowEmptySpaces());
        result.add("length", ValidationLengthBuilder.build(validation.length()));

        return result;
    }

}
