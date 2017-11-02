package io.intino.konos.alexandria.foundation.activity.dialogs.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.intino.konos.alexandria.foundation.activity.dialogs.builders.ValidationLengthBuilder;
import io.intino.konos.alexandria.foundation.activity.model.Dialog;

public class TextInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.Text)) return;
        Dialog.Tab.Text text = (Dialog.Tab.Text)input;
        result.addProperty("edition", editionOf(text));
        result.add("validation", validation(text));
    }

    private static String editionOf(Dialog.Tab.Text text) {
        Dialog.TextEdition edition = text.edition();
        if (edition == Dialog.TextEdition.Normal) return "text";
        return edition.toString().toLowerCase();
    }

    private static JsonObject validation(Dialog.Tab.Text text) {
        JsonObject result = new JsonObject();
        if (text.validation() == null) return result;
        Dialog.Tab.Text.Validation validation = text.validation();

        result.addProperty("mask", validation.mask());
        result.add("allowedValues", new Gson().toJsonTree(validation.allowedValues()));
        result.add("disallowedValues", new Gson().toJsonTree(validation.disallowedValues()));
        result.addProperty("disallowEmptySpaces", validation.disallowEmptySpaces());
        result.add("length", ValidationLengthBuilder.build(validation.length()));

        return result;
    }

}
