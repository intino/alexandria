package io.intino.konos.alexandria.foundation.pushservice;

import java.util.List;

public interface RequestAdapter<T> {
    T adapt(String value);
    List<T> adaptList(String value);
}
