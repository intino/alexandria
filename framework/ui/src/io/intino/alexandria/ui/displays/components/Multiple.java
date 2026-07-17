package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.events.editable.AddItemListener;
import io.intino.alexandria.ui.displays.events.editable.ChangeItemListener;
import io.intino.alexandria.ui.displays.events.editable.RemoveItemListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Multiple<B extends Box, C extends Component, V> extends AbstractMultiple<B> {
    private static final long BatchWindowMillis = 25;
    private static final ScheduledExecutorService BatchScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "alexandria-multiple-batch");
        thread.setDaemon(true);
        return thread;
    });
    private boolean readonly;
    private AddItemListener addItemListener;
    private ChangeItemListener changeItemListener;
    private RemoveItemListener removeItemListener;
    private java.util.Map<C, String> labelsMap = new HashMap<>();
    private java.util.Map<C, String> descriptionsMap = new HashMap<>();
    private ReadonlyListener readonlyListener = null;
    private final Object batchLock = new Object();
    private final List<Runnable> deferredNotifications = new ArrayList<>();
    private long batchSequence = 0;
    private boolean batchingChildren = false;

    public Multiple(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        notifier.refreshVisibility(visible());
    }

    public Multiple<B, C, V> onReadonly(ReadonlyListener listener) {
        this.readonlyListener = listener;
        return this;
    }

    public boolean readonly() {
        return readonly;
    }

    @Override
    public <D extends Display> D add(D child, String container) {
        touchBatchWindow();
        return super.add(child, container);
    }

    @Override
    public <D extends Display> D addPromise(D child, String container) {
        touchBatchWindow();
        return super.addPromise(child, container);
    }

    @Override
    public <D extends Display> List<D> addPromise(List<D> children, String container) {
        touchBatchWindow();
        return super.addPromise(children, container);
    }

    @Override
    public <D extends Display> D insertPromise(D child, int index, String container) {
        touchBatchWindow();
        return super.insertPromise(child, index, container);
    }

    @Override
    public <D extends Display> List<D> insertPromise(List<D> children, int index, String container) {
        touchBatchWindow();
        return super.insertPromise(children, index, container);
    }

    @Override
    public void clear(String container) {
        touchBatchWindow();
        super.clear(container);
    }

    public Multiple<B, C, V> readonly(boolean readonly) {
        _readonly(readonly);
        notifyReadonly(readonly);
        return this;
    }

    public C add() {
        C result = add((V) null);
        if (result instanceof Editable) ((Editable<?, ?>)result).focus();
        return result;
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

    private void notifyReadonly(boolean value) {
        if (readonlyListener != null) readonlyListener.accept(new ReadonlyEvent(this, value));
        notifier.refreshReadonly(value);
    }

    public boolean isBatchingChildren() {
        synchronized (batchLock) {
            return batchingChildren;
        }
    }

    public void deferNotification(Runnable notification) {
        boolean executeImmediately;
        synchronized (batchLock) {
            executeImmediately = !batchingChildren;
            if (!executeImmediately) deferredNotifications.add(notification);
        }
        if (executeImmediately) notification.run();
    }

    private void touchBatchWindow() {
        final long sequence;
        synchronized (batchLock) {
            batchingChildren = true;
            sequence = ++batchSequence;
        }
        BatchScheduler.schedule(() -> flushDeferredNotifications(sequence), BatchWindowMillis, TimeUnit.MILLISECONDS);
    }

    private void flushDeferredNotifications(long sequence) {
        List<Runnable> notifications;
        synchronized (batchLock) {
            if (sequence != batchSequence) return;
            batchingChildren = false;
            notifications = new ArrayList<>(deferredNotifications);
            deferredNotifications.clear();
        }
        notifications.forEach(Runnable::run);
    }

}
