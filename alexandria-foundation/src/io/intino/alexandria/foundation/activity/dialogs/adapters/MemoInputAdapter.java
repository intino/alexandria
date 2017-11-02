package io.intino.alexandria.foundation.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.alexandria.foundation.activity.model.Dialog;

public class MemoInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.Memo)) return;
        Dialog.Tab.Memo memo = (Dialog.Tab.Memo)input;
        result.addProperty("type", "text");
        result.addProperty("mode", memo.mode().toString());
        result.addProperty("height", memo.height());
    }

}
