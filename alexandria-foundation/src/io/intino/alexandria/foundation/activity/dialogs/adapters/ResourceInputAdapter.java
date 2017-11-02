package io.intino.alexandria.foundation.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.alexandria.foundation.activity.dialogs.builders.ValidationResourceBuilder;
import io.intino.alexandria.foundation.activity.model.Dialog;

public class ResourceInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.Resource)) return;
        Dialog.Tab.Resource file = (Dialog.Tab.Resource)input;
        result.addProperty("showPreview", file.showPreview());
        result.add("validation", ValidationResourceBuilder.build(file.validation()));
    }

}
