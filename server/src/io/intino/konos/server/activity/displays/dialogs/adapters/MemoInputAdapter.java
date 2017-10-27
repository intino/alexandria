package io.intino.konos.server.activity.displays.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Input;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Memo;

public class MemoInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof Memo)) return;
        Memo memo = (Memo)input;
        result.addProperty("type", "text");
        result.addProperty("mode", memo.mode().toString());
        result.addProperty("height", memo.height());
    }

}
