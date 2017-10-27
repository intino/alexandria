package io.intino.konos.server.activity.displays.dialogs.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Input;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Password;
import io.intino.konos.server.activity.displays.dialogs.builders.ValidationLengthBuilder;

import static java.util.stream.Collectors.toList;

public class PasswordInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof Password)) return;
        Password password = (Password)input;
        result.addProperty("type", "text");
        result.add("validation", validation(password));
    }

    private static JsonObject validation(Password password) {
        JsonObject result = new JsonObject();
        if (password.validation() == null) return result;
        Password.Validation validation = password.validation();

        result.add("required", new Gson().toJsonTree(validation.requiredList().stream().map(Enum::toString).collect(toList())));
        result.add("length", ValidationLengthBuilder.build(validation.length()));

        return result;
    }

}
