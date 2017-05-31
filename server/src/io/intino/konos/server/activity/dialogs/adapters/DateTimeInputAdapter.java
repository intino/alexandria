package io.intino.konos.server.activity.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.DateTime;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;

public class DateTimeInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof DateTime)) return;
        DateTime date = (DateTime)input;
        result.addProperty("format", date.format().replace("d", "D").replace("y", "Y"));
    }

}
