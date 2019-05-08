package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.GroupBoxNotifier;
import io.intino.alexandria.ui.model.Datasource;

import java.util.Arrays;
import java.util.List;

public class GroupBox<DN extends GroupBoxNotifier, B extends Box> extends AbstractGroupBox<B> {
    private Collection collection;
    private List<String> selection;

    public GroupBox(B box) {
        super(box);
    }

    public GroupBox<DN, B> bindTo(Collection collection) {
        this.collection = collection;
        this.collection.onRefresh((event) -> refresh(event.items()));
        return this;
    }

    public void select(String[] groups) {
        this.selection = Arrays.asList(groups);
        collection.filter(name(), selection);
    }

    public void refresh(List<Object> items) {
        super.refresh();
        Datasource source = collection.source();
        List groups = source.groups(name(), items);
        notifier.refreshGroups(groups);
    }
}