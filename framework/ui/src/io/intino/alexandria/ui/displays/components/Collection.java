package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.CollectionSetup;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.CollectionItemDisplay;
import io.intino.alexandria.ui.displays.components.collection.behaviors.CollectionBehavior;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.events.collection.AddItemListener;
import io.intino.alexandria.ui.displays.events.collection.RefreshEvent;
import io.intino.alexandria.ui.displays.events.collection.RefreshListener;
import io.intino.alexandria.ui.displays.notifiers.CollectionNotifier;
import io.intino.alexandria.ui.model.Datasource;

import java.util.List;
import java.util.*;

import static java.util.stream.Collectors.toList;

public abstract class Collection<DN extends CollectionNotifier, B extends Box> extends AbstractCollection<DN, B> {
    private CollectionBehavior behavior;
    private Datasource source;
    private java.util.List<SelectionListener> selectionListeners = new ArrayList<>();
    private AddItemListener addItemListener;
    private List<RefreshListener> refreshListeners = new ArrayList<>();
    private List<Listener> readyListeners = new ArrayList<>();

    public Collection(B box) {
        super(box);
    }

    Collection<DN, B> source(Datasource source, CollectionBehavior behavior) {
        this.source = source;
        this.behavior = behavior;
        setup();
        return this;
    }

    <CB extends CollectionBehavior> CB behavior() {
        return (CB) behavior;
    }

    public void onAddItem(AddItemListener listener) {
        this.addItemListener = listener;
    }

    public void onRefresh(RefreshListener listener) {
        this.refreshListeners.add(listener);
    }

    public void onReady(Listener listener) {
        this.readyListeners.add(listener);
    }

    @Override
    public void init() {
        super.init();
        setup();
    }

    public <D extends Datasource> D source() {
        return (D) source;
    }

    public void filter(String grouping, List<String> groups) {
        behavior.filter(grouping, groups);
        notifier.refreshItemCount(behavior.itemCount());
    }

    public void filter(String condition) {
        behavior.condition(condition);
        notifier.refreshItemCount(behavior.itemCount());
    }

    public void filter(Timetag timetag) {
        behavior.timetag(timetag);
        notifier.refreshItemCount(behavior.itemCount());
    }

    public void sortings(List<String> sortings) {
        behavior.sortings(sortings);
    }

    public void addSorting(String sorting) {
        behavior.addSorting(sorting);
    }

    public void removeSorting(String sorting) {
        behavior.removeSorting(sorting);
    }

    public long itemCount() {
        return behavior.itemCount();
    }

    public void selection(String[] selection) {
        selectionListeners.forEach(l -> l.accept(new SelectionEvent(this, itemsOf(selection))));
    }

    private List<Object> itemsOf(String[] selectionArray) {
        List<String> selection = Arrays.asList(selectionArray);
        return children().stream().filter(d -> selection.contains(d.id())).map(this::itemOf).collect(toList());
    }

    protected Object itemOf(Display display) {
        return (display instanceof CollectionItemDisplay) ? ((CollectionItemDisplay)display).item() : null;
    }

    public void clear() {
        clear("rows");
    }

    protected void addSelectionListener(SelectionListener listener) {
        this.selectionListeners.add(listener);
    }

    protected abstract AddItemEvent itemEvent(Display c);

    Optional<AddItemListener> addItemListener() {
        return Optional.ofNullable(addItemListener);
    }

    void notifyRefresh() {
        if (refreshListeners.size() <= 0) return;
        List<Object> items = children().stream().map(d -> ((CollectionItemDisplay) d).item()).collect(toList());
        refreshListeners.forEach(l -> l.accept(new RefreshEvent(this, items)));
    }

    void notifyReady() {
        readyListeners.forEach(l -> l.accept(new Event(this)));
    }

    void setup() {
        if (source == null) return;
        behavior.setup(source);
        notifier.setup(new CollectionSetup().itemCount(behavior.itemCount()));
        notifyReady();
    }

}