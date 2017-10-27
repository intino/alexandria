package io.intino.konos.server.activity.displays.dialogs.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.DateTime;
import io.intino.konos.server.activity.displays.dialogs.model.Dialog.Tab.Input;

public class DateTimeInputAdapter {

    public static void adapt(JsonObject result, Input input) {
        if (!(input instanceof DateTime)) return;
        DateTime date = (DateTime)input;
        result.addProperty("format", date.format().replace("d", "D").replace("y", "Y"));
    }

}
