package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Picture;

public class PictureInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof Picture)) return;
        ResourceInputAdapter.adapt(result, input);
    }

}
