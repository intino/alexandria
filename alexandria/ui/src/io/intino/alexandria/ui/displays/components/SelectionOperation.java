package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SelectionOperationNotifier;

import java.util.Collections;
import java.util.List;

public class SelectionOperation<DN extends SelectionOperationNotifier, B extends Box> extends AbstractSelectionOperation<DN, B> implements io.intino.alexandria.ui.displays.components.toolbar.SelectionOperation {
    private List<String> selection = Collections.emptyList();

    public SelectionOperation(B box) {
        super(box);
    }

    public SelectionOperation<DN, B> selection(List<String> selection) {
        this.selection = selection;
        return this;
    }

    public List<String> selection() {
        return selection;
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

}