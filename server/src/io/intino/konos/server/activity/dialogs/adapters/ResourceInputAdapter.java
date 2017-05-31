package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Resource;
import io.intino.konos.server.activity.dialogs.builders.ValidationResourceBuilder;

public class ResourceInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof Resource)) return;
        Resource file = (Resource)input;
        result.addProperty("showPreview", file.showPreview());
        result.add("validation", ValidationResourceBuilder.build(file.validation()));
    }

}
