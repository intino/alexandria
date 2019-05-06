package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.toolbar.SelectionOperation;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.TaskSelectionNotifier;

import java.util.Collections;
import java.util.List;

public abstract class TaskSelection<DN extends TaskSelectionNotifier, B extends Box> extends AbstractTaskSelection<DN, B> implements SelectionOperation {
    private String title;
    private String icon;
    private SelectionListener executeListener;
    private List<String> selection = Collections.emptyList();

    public TaskSelection(B box) {
        super(box);
    }

    public void onExecute(SelectionListener listener) {
        this.executeListener = listener;
    }

    public TaskSelection<DN, B> title(String title) {
        this.title = title;
        return this;
    }

    public TaskSelection<DN, B> icon(String icon) {
        this.icon = icon;
        return this;
    }

    public TaskSelection<DN, B> selection(List<String> selection) {
        this.selection = selection;
        return this;
    }

    @Override
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