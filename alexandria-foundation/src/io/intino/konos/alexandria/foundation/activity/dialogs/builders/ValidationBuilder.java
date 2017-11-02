package io.intino.konos.alexandria.foundation.activity.dialogs.builders;

import com.google.gson.JsonArray;
import io.intino.konos.alexandria.foundation.activity.dialogs.DialogValidator;
import io.intino.konos.alexandria.foundation.activity.model.Dialog;
import io.intino.konos.alexandria.foundation.activity.schemas.Validation;

import java.util.List;

public class ValidationBuilder {

    public static Validation build(String input, DialogValidator.Result result) {
        return new Validation().input(input)
                               .status(result.status())
                               .message(result.message())
                               .modifiedInputs(modifiedInputs(result.modifiedInputs()));
    }

    private static String modifiedInputs(List<Dialog.Tab.Input> inputs) {
        JsonArray result = new JsonArray();
        inputs.forEach(input -> result.add(DialogInputBuilder.build(input)));
        return result.toString();
    }

}
