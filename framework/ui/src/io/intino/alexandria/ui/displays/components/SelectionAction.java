package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.SelectionActionNotifier;

public class SelectionAction<DN extends SelectionActionNotifier, B extends Box> extends AbstractSelectionAction<DN, B> {
    private SelectionListener executeListener;

    public SelectionAction(B box) {
        super(box);
    }

    public void onExecute(SelectionListener listener) {
        this.executeListener = listener;
    }

    public void execute() {
        if (this.executeListener == null) return;
        this.executeListener.accept(new SelectionEvent(this, selection()));
    }
}