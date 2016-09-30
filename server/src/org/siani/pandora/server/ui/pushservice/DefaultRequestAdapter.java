package org.siani.pandora.server.ui.pushservice;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.siani.pandora.server.pushservice.RequestAdapter;

import java.util.ArrayList;
import java.util.List;

public class DefaultRequestAdapter<T> implements RequestAdapter<T> {
    private static final JsonParser parser = new JsonParser();
    private final Class<T> clazz;

    public DefaultRequestAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T adapt(String value) {
        return new Gson().fromJson(value, clazz);
    }

    @Override
    public List<T> adaptList(String value) {
        JsonArray valueList = (JsonArray) parser.parse(value);
        List<T> result = new ArrayList<>();

        for (JsonElement element : valueList)
            result.add(adapt(element.getAsString()));

        return result;
    }
}
