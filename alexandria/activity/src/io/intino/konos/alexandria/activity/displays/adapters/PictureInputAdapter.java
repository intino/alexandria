package io.intino.konos.alexandria.activity.displays.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.alexandria.activity.model.Dialog;

public class PictureInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.Picture)) return;
        ResourceInputAdapter.adapt(result, input);
    }

}
