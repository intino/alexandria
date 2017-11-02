package io.intino.konos.alexandria.foundation.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.alexandria.foundation.activity.model.Dialog;

public class DateTimeInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.DateTime)) return;
        Dialog.Tab.DateTime date = (Dialog.Tab.DateTime)input;
        result.addProperty("format", date.format().replace("d", "D").replace("y", "Y"));
    }

}
