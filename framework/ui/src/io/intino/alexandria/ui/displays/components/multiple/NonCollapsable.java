package io.intino.alexandria.ui.displays.components.multiple;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;

import java.util.List;

public interface NonCollapsable<B extends Box, C extends Component, V extends Object> {
    C add(V value);

    default C add() {
        return add(null);
    }

    default void addAll(List<V> values) {
        values.forEach(this::add);
    }
}
