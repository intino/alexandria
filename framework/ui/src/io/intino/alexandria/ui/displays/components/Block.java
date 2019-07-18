package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BlockNotifier;

public class Block<DN extends BlockNotifier, B extends Box> extends AbstractBlock<B> {
    private String label;

    public Block(B box) {
        super(box);
    }

    public String label() {
        return label;
    }

    public Block<DN, B> label(String label) {
        this.label = label;
        return this;
    }

    public void spacing(String spacing) {
        notifier.refreshSpacing(spacing);
    }

    public void layout(String layout) {
        notifier.refreshLayout(layout);
    }
}