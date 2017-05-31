package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.File;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;

public class FileInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof File)) return;
        ResourceInputAdapter.adapt(result, input);
    }

}
