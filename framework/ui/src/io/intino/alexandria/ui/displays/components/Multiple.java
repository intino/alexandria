package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;

import java.util.List;

public abstract class Multiple<B extends Box, C extends Component, V extends Object> extends AbstractMultiple<B> {

    public Multiple(B box) {
        super(box);
    }

    public void addAll(List<V> values) {
        values.forEach(this::add);
    }

    public C add() {
        return add((V)null);
    }

    public abstract C add(V value);

}