package io.intino.konos.alexandria.foundation.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.alexandria.foundation.activity.model.Dialog;

public class FileInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.File)) return;
        ResourceInputAdapter.adapt(result, input);
    }

}
