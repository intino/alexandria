package io.intino.konos.alexandria.activity.box.displays.adapters;

import com.google.gson.JsonObject;
import io.intino.konos.alexandria.activity.box.model.Dialog;

public class DateInputAdapter {

    public static void adapt(JsonObject result, Dialog.Tab.Input input) {
        if (!(input instanceof Dialog.Tab.Date)) return;
        Dialog.Tab.Date date = (Dialog.Tab.Date)input;
        result.addProperty("format", date.format().replace("d", "D").replace("y", "Y"));
    }

}
