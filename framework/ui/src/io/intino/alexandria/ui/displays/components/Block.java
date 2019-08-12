package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BlockNotifier;

public class Block<DN extends BlockNotifier, B extends Box> extends AbstractBlock<B> {

    public Block(B box) {
        super(box);
    }

    public void spacing(String spacing) {
        notifier.refreshSpacing(spacing);
    }

    public void layout(String layout) {
        notifier.refreshLayout(layout);
    }
}