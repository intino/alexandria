package io.intino.pandora.restful.exceptions;

import io.intino.pandora.Error;

import java.util.Map;

public class RestfulFailure extends Exception implements Error {
    private final String message;

    public RestfulFailure(String message) {
        this.message = message;
    }

    @Override
    public String code() {
        return "err:rff";
    }

    @Override
    public String label() {
        return message;
    }

    @Override
    public Map<String, String> parameters() {
        return null;
    }
}
