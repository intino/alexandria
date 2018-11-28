package io.intino.alexandria.ui.displays.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.intino.alexandria.ui.displays.builders.ValidationLengthBuilder;
import io.intino.alexandria.ui.model.Dialog;

import static java.util.stream.Collectors.toList;

public class PasswordInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.Password)) return;
        Dialog.Tab.Password password = (Dialog.Tab.Password)input;
        result.addProperty("type", "text");
        result.add("validation", validation(password));
    }

    private static JsonObject validation(Dialog.Tab.Password password) {
        JsonObject result = new JsonObject();
        if (password.validation() == null) return result;
        Dialog.Tab.Password.Validation validation = password.validation();

        result.add("required", new Gson().toJsonTree(validation.requiredList().stream().map(Enum::toString).collect(toList())));
        result.add("length", ValidationLengthBuilder.build(validation.length()));

        return result;
    }

}
