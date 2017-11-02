package io.intino.konos.alexandria.foundation.pushservice;

import java.util.List;

public interface ResponseAdapter<T> {
    String adapt(T value);
    String adaptList(List<T> value);
}
