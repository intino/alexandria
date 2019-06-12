package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.notifiers.DecisionDialogNotifier;

public class DecisionDialog<DN extends DecisionDialogNotifier, B extends Box> extends AbstractDecisionDialog<DN, B> {
    private Selector selector;

    public DecisionDialog(B box) {
        super(box);
    }

    public void bindTo(Selector selector) {
        this.selector = selector;
        selector.onSelect(event -> close());
    }

    @Override
    public void open() {
        super.open();
        selector.reset();
    }
}