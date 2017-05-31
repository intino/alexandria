package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Date;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;

public class DateInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof Date)) return;
        Date date = (Date)input;
        result.addProperty("format", date.format().replace("d", "D").replace("y", "Y"));
    }

}
