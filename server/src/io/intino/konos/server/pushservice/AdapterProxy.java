package io.intino.konos.server.pushservice;

public interface AdapterProxy {
    RequestAdapter requestAdapterOf(String name, Class clazz);
    ResponseAdapter responseAdapterOf(String name);
}
