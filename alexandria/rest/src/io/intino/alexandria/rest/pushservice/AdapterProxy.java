package io.intino.alexandria.rest.pushservice;

public interface AdapterProxy {
    RequestAdapter requestAdapterOf(String name, Class clazz);
    ResponseAdapter responseAdapterOf(String name);
}
