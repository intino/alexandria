package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseSelectionActionNotifier;

import java.util.Collections;
import java.util.List;

public class BaseSelectionAction<DN extends BaseSelectionActionNotifier, B extends Box> extends AbstractBaseSelectionAction<DN, B> implements io.intino.alexandria.ui.displays.components.toolbar.SelectionOperation {
    private List<String> selection = Collections.emptyList();

    public BaseSelectionAction(B box) {
        super(box);
    }

    public BaseSelectionAction<DN, B> selection(List<String> selection) {
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
        notifier.refreshReadonly(selection.size() <= 0);
    }

}