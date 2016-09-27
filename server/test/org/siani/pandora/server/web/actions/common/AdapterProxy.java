package org.siani.pandora.server.web.actions.common;

import org.siani.pandora.server.web.actions.DefaultRequestAdapter;
import org.siani.pandora.server.actions.RequestAdapter;
import org.siani.pandora.server.actions.ResponseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterProxy implements org.siani.pandora.server.actions.AdapterProxy {

    private final Map<String, RequestAdapter> requestAdapters = new HashMap<>();
    private final Map<String, ResponseAdapter> responseAdapters = new HashMap<>();

    @Override
    public RequestAdapter requestAdapterOf(String name, Class clazz) {
        return requestAdapters.getOrDefault(name, defaultRequestAdapter(clazz));
    }

    @Override
    public ResponseAdapter responseAdapterOf(String name) {
        return responseAdapters.getOrDefault(name, defaultResponseAdapter());
    }

    private RequestAdapter defaultRequestAdapter(Class clazz) {
        return new DefaultRequestAdapter(clazz);
    }

    private ResponseAdapter defaultResponseAdapter() {
        return new ResponseAdapter() {
            @Override
            public String adapt(Object value) {
                return value.toString();
            }

            @Override
            public String adaptList(List value) {
                throw new UnsupportedOperationException("Not used");
            }
        };
    }

    public AdapterProxy registerAdapter(String name, RequestAdapter requestAdapter) {
        requestAdapters.put(name, requestAdapter);
        return this;
    }

    public AdapterProxy registerAdapter(String name, ResponseAdapter responseAdapter) {
        responseAdapters.put(name, responseAdapter);
        return this;
    }
}
