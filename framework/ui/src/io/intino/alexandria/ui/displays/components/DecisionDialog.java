package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.DecisionDialogNotifier;

public class DecisionDialog<DN extends DecisionDialogNotifier, B extends Box> extends AbstractDecisionDialog<DN, B> {
    private Selector selector;
    private SelectionListener selectionListener = null;

    public DecisionDialog(B box) {
        super(box);
    }

    public void bindTo(Selector selector) {
        this.selector = selector;
        selector.onSelect(event -> {
        	if (event.selection().size() == 0) return;
            close();
            notifySelect(event);
        });
    }

    public DecisionDialog onSelect(SelectionListener listener) {
        this.selectionListener = listener;
        return this;
    }

    @Override
    public void open() {
        selector.reset();
        super.open();
    }

    private void notifySelect(SelectionEvent event) {
        if (selectionListener == null) return;
        selectionListener.accept(new SelectionEvent(this, event.selection()));
    }
}