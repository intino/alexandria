package io.intino.konos.server.activity.dialogs.builders;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Resource.Validation;

public class ValidationResourceBuilder {

    public static JsonObject build(Validation validation) {
        if (validation == null) return new JsonObject();
        JsonObject result = new JsonObject();
        result.addProperty("maxSize", validation.maxSize());
        result.add("allowedExtensions", new Gson().toJsonTree(validation.allowedExtensions()));
        return result;
    }

}
