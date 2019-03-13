package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class Block<B extends Box> extends AbstractBlock<B> {
    private String label;

    public Block(B box) {
        super(box);
    }

    public String label() {
        return label;
    }

    public Block<B> label(String label) {
        this.label = label;
        return this;
    }

}