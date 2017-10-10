package io.intino.konos.server.activity.dialogs.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.intino.konos.server.activity.dialogs.Dialog.Tab;
import io.intino.konos.server.activity.dialogs.Dialog.Tab.Input;
import io.intino.konos.server.activity.dialogs.schemas.Dialog;

import java.util.List;

public class DialogBuilder {

    public static Dialog build(io.intino.konos.server.activity.dialogs.Dialog dialog) {
        return new Dialog().label(dialog.label())
                           .description(dialog.description())
                           .definition(jsonDefinitionOf(dialog).toString());
    }

    public static JsonObject jsonDefinitionOf(io.intino.konos.server.activity.dialogs.Dialog dialog) {
        JsonObject result = new JsonObject();
        result.add("label", new JsonPrimitive(dialog.label()));
        result.add("description", new JsonPrimitive(dialog.description()));
        result.addProperty("readonly", dialog.readonly());
        result.add("toolbar", jsonToolbar(dialog.toolbar()));
        result.add("tabList", jsonTabListOf(dialog.tabList()));
        return result;
    }

    public static JsonObject jsonToolbar(io.intino.konos.server.activity.dialogs.Dialog.Toolbar toolbar) {
        JsonObject result = new JsonObject();
        result.add("operationList", jsonOperationList(toolbar.operationList()));
        return result;
    }

    public static JsonArray jsonOperationList(List<io.intino.konos.server.activity.dialogs.Dialog.Toolbar.Operation> operationList) {
        JsonArray result = new JsonArray();
        operationList.forEach(operation -> result.add(jsonOperationOf(operation)));
        return result;
    }

    public static JsonObject jsonOperationOf(io.intino.konos.server.activity.dialogs.Dialog.Toolbar.Operation operation) {
        JsonObject result = new JsonObject();
        result.addProperty("name", operation.name());
        result.addProperty("label", operation.label());
        return result;
    }

    public static JsonArray jsonTabListOf(List<Tab> tabList) {
        JsonArray result = new JsonArray();
        tabList.forEach(tab -> result.add(jsonTabOf(tab)));
        return result;
    }

    public static JsonObject jsonTabOf(Tab tab) {
        JsonObject result = new JsonObject();
        result.addProperty("label", tab.label());
        result.add("inputList", jsonInputListOf(tab.inputList()));
        return result;
    }

    public static JsonArray jsonInputListOf(List<Input> inputList) {
        JsonArray result = new JsonArray();
        inputList.forEach(input -> result.add(jsonInputOf(input)));
        return result;
    }

    public static JsonObject jsonInputOf(Input input) {
        return DialogInputBuilder.build(input);
    }

}
