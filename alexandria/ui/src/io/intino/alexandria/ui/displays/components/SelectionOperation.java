package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.SelectionOperationNotifier;

import java.util.Collections;
import java.util.List;

public class SelectionOperation<DN extends SelectionOperationNotifier, B extends Box> extends AbstractSelectionOperation<DN, B> {
    private SelectionListener executeListener;
    private List<String> selection = Collections.emptyList();

    public SelectionOperation(B box) {
        super(box);
    }

    public void onExecute(SelectionListener listener) {
        this.executeListener = listener;
    }

    public SelectionOperation<DN, B> selection(List<String> selection) {
        this.selection = selection;
        return this;
    }

    public void selectedItems(List<String> items) {
        selection(items);
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        notifier.refreshDisabled(selection.size() <= 0);
    }

    public void execute() {
        if (this.executeListener == null) return;
        this.executeListener.accept(new SelectionEvent(this, selection));
    }

}