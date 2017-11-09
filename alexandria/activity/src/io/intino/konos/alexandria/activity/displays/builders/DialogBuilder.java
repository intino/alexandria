package io.intino.konos.alexandria.activity.displays.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

public class DialogBuilder {

    public static Dialog build(Dialog dialog) {
        return new Dialog().label(dialog.label())
                           .description(dialog.description())
                           .definition(jsonDefinitionOf(dialog).toString());
    }

    public static JsonObject jsonDefinitionOf(Dialog dialog) {
        JsonObject result = new JsonObject();
        result.add("label", new JsonPrimitive(dialog.label()));
        result.add("description", new JsonPrimitive(dialog.description()));
        result.addProperty("readonly", dialog.readonly());
        result.add("toolbar", jsonToolbar(dialog.toolbar()));
        result.add("tabList", jsonTabListOf(dialog.tabList()));
        return result;
    }

    public static JsonObject jsonToolbar(Dialog.Toolbar toolbar) {
        JsonObject result = new JsonObject();
        result.add("operationList", jsonOperationList(toolbar.operationList()));
        return result;
    }

    public static JsonArray jsonOperationList(List<Dialog.Toolbar.Operation> operationList) {
        JsonArray result = new JsonArray();
        operationList.forEach(operation -> result.add(jsonOperationOf(operation)));
        return result;
    }

    public static JsonObject jsonOperationOf(Dialog.Toolbar.Operation operation) {
        JsonObject result = new JsonObject();
        result.addProperty("name", operation.name());
        result.addProperty("label", operation.label());
        return result;
    }

    public static JsonArray jsonTabListOf(List<Dialog.Tab> tabList) {
        JsonArray result = new JsonArray();
        tabList.forEach(tab -> result.add(jsonTabOf(tab)));
        return result;
    }

    public static JsonObject jsonTabOf(Dialog.Tab tab) {
        JsonObject result = new JsonObject();
        result.addProperty("label", tab.label());
        result.add("inputList", jsonInputListOf(tab.inputList()));
        return result;
    }

    public static JsonArray jsonInputListOf(List<Dialog.Tab.Input> inputList) {
        JsonArray result = new JsonArray();
        inputList.forEach(input -> result.add(jsonInputOf(input)));
        return result;
    }

    public static JsonObject jsonInputOf(Dialog.Tab.Input input) {
        return DialogInputBuilder.build(input);
    }

}
