package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.ChangeItemEvent;
import io.intino.alexandria.ui.displays.events.RemoveItemEvent;
import io.intino.alexandria.ui.displays.events.editable.AddItemListener;
import io.intino.alexandria.ui.displays.events.editable.ChangeItemListener;
import io.intino.alexandria.ui.displays.events.editable.RemoveItemListener;
import io.intino.alexandria.ui.displays.notifiers.MultipleNotifier;

import java.util.HashMap;
import java.util.List;

public abstract class Multiple<B extends Box, C extends Component, V> extends AbstractMultiple<B> {
    private boolean readonly;
    private AddItemListener addItemListener;
    private ChangeItemListener changeItemListener;
    private RemoveItemListener removeItemListener;
    private java.util.Map<C, String> labelsMap = new HashMap<>();
    private java.util.Map<C, String> descriptionsMap = new HashMap<>();

    public Multiple(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        notifier.refreshVisibility(visible());
    }

    public boolean readonly() {
        return readonly;
    }

    public Multiple<B, C, V> readonly(boolean readonly) {
        _readonly(readonly);
        notifier.refreshReadonly(readonly);
        return this;
    }

    public C add() {
        return add((V) null);
    }

    protected abstract C add(V value);
    public abstract void remove(C component);

    public void remove(int index) {
        List<Display> children = children();
        if (children.size() <= index) return;
        notifyRemove(index);
        remove((C) children.get(index));
        children().stream().filter(d -> d instanceof Editable).forEach(d -> ((Editable<?,?>)d).reload());
    }

    public void select(String id) {
        children().stream().filter(d -> d.id().equals(id)).findFirst().ifPresent(Display::refresh);
    }

    protected Multiple<B, C, V> notifyAdd(C child) {
        notifyAdd(child, null);
        return this;
    }

    protected Multiple<B, C, V> _readonly(boolean readonly) {
        this.readonly = readonly;
        children().stream().filter(c -> c instanceof Editable).forEach(c -> _readonly(c, readonly));
        return this;
    }

    protected Multiple<B, C, V> addItemListener(AddItemListener listener) {
        this.addItemListener = listener;
        return this;
    }

    protected Multiple<B, C, V> changeItemListener(ChangeItemListener listener) {
        this.changeItemListener = listener;
        return this;
    }

    protected Multiple<B, C, V> removeItemListener(RemoveItemListener listener) {
        this.removeItemListener = listener;
        return this;
    }

    protected Multiple<B, C, V> notifyAdd(C child, V value) {
        int index = children().size()-1;
        if (addItemListener != null) addItemListener.accept(new AddItemEvent(this, child, value, index));
        addListeners(child, value, index);
        _readonly(child, readonly);
        return this;
    }

    protected Multiple<B, C, V> notifyRemove(int index) {
        if (removeItemListener != null) removeItemListener.accept(new RemoveItemEvent(this, index));
        return this;
    }

    private void addListeners(C child, V value, int index) {
        addSelectListener(child, value, index);
        addChangeListener(child, value, index);
    }

    private void addSelectListener(C child, V value, int index) {
        if (!(child instanceof Selectable)) return;
        ((Selectable)child).onSelect(e -> notifySelect(child, e.selection(), index));
    }

    private void addChangeListener(C child, V value, int index) {
        if (!(child instanceof Editable)) return;
        ((Editable)child).onChange(e -> notifyChange(child, e.value(), index));
    }

    private void notifySelect(C child, List<V> selection, int index) {
        if (changeItemListener == null) return;
        changeItemListener.accept(new ChangeItemEvent(this, child, selection, index));
    }

    private void notifyChange(C child, V value, int index) {
        if (changeItemListener == null) return;
        changeItemListener.accept(new ChangeItemEvent(this, child, value, index));
    }

    private void _readonly(Display c, boolean readonly) {
        if (!(c instanceof Editable)) return;
        ((Editable)c).readonly(readonly);
    }

}