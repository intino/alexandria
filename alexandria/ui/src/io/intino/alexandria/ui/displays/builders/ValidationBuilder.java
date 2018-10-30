package io.intino.alexandria.ui.displays.builders;

import com.google.gson.JsonArray;
import io.intino.alexandria.ui.displays.DialogValidator;
import io.intino.alexandria.ui.model.Dialog;
import io.intino.alexandria.ui.schemas.Validation;

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
