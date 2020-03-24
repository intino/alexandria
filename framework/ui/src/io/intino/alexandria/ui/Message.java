package io.intino.alexandria.ui;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Map;

public class Message {
    @SerializedName("n")
    private final String name;
    @SerializedName("p")
    private final Map<String, Object> parameters;

    public Message(String name) {
        this(name, Collections.emptyMap());
    }

    public Message(String name, Map<String, Object> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String name() {
        return name;
    }

    public Map<String, Object> parameters() {
        return parameters;
    }

}
