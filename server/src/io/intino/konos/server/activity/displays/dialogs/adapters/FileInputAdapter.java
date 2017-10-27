package io.intino.konos.server.activity.displays.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.File;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Input;

public class FileInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof File)) return;
        ResourceInputAdapter.adapt(result, input);
    }

}
