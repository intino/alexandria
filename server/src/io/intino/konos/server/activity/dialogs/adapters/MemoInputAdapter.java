package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Memo;

public class MemoInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof Memo)) return;
        Memo memo = (Memo)input;
        result.addProperty("type", "text");
        result.addProperty("mode", memo.mode().toString());
        result.addProperty("height", memo.height());
    }

}
