package io.intino.alexandria.ui.displays.builders;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.intino.alexandria.ui.model.Dialog;

public class ValidationResourceBuilder {

    public static JsonObject build(Dialog.Tab.Resource.Validation validation) {
        if (validation == null) return new JsonObject();
        JsonObject result = new JsonObject();
        result.addProperty("maxSize", validation.maxSize());
        result.add("allowedExtensions", new Gson().toJsonTree(validation.allowedExtensions()));
        return result;
    }

}
