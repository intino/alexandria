package io.intino.konos.alexandria.foundation.pushservice;

public interface AdapterProxy {
    RequestAdapter requestAdapterOf(String name, Class clazz);
    ResponseAdapter responseAdapterOf(String name);
}
