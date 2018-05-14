package io.intino.konos.alexandria.ui.model;

import io.intino.konos.alexandria.ui.model.view.set.AbstractItem;

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
