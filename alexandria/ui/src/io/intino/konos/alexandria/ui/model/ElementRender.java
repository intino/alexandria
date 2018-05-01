package io.intino.konos.alexandria.ui.model;

import io.intino.konos.alexandria.ui.model.views.set.AbstractItem;

public class ElementRender {
    protected AbstractItem option;

    public AbstractItem option() {
        return option;
    }

    public ElementRender option(AbstractItem option) {
        this.option = option;
        return this;
    }
}
