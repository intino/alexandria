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
import io.intino.alexandria.ui.displays.events.collection.*;
import io.intino.alexandria.ui.displays.notifiers.CollectionNotifier;
import io.intino.alexandria.ui.model.Datasource;

import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class Collection<DN extends CollectionNotifier, B extends Box> extends AbstractCollection<DN, B> {
    private CollectionBehavior behavior;
    private Datasource source;
    private java.util.List<SelectionListener> selectionListeners = new ArrayList<>();
    private AddItemListener addItemListener;
    private List<RefreshListener> refreshListeners = new ArrayList<>();
    private List<RefreshCountListener> refreshItemCountListeners = new ArrayList<>();
    private List<Listener> readyListeners = new ArrayList<>();
    private boolean ready = false;
    private List<String> selection;

    public Collection(B box) {
        super(box);
    }

    public abstract <D extends Datasource> void source(D source);

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

    public void onRefreshItemCount(RefreshCountListener listener) {
        this.refreshItemCountListeners.add(listener);
    }

    public void unRefresh(RefreshListener listener) {
        this.refreshListeners.remove(listener);
    }

    public void onReady(Listener listener) {
        this.readyListeners.add(listener);
    }

    @Override
    public void init() {
        super.init();
        setup();
    }

    public void reload() {
        if (behavior == null) return;
        behavior.reload();
        notifyRefreshItemCount();
    }

    public boolean ready() {
        return ready;
    }

    public <D extends Datasource> D source() {
        return (D) source;
    }

    public void clearFilters() {
        behavior.clearFilters();
        notifyRefreshItemCount();
    }

    public void filter(String grouping, List<String> groups) {
        if (behavior == null) return;
        behavior.filter(grouping, groups);
        notifyRefreshItemCount();
    }

    public void filter(String grouping, Instant from, Instant to) {
        if (behavior == null) return;
        behavior.filter(grouping, from, to);
        notifyRefreshItemCount();
    }

    public void filter(String condition) {
        if (behavior == null) return;
        behavior.condition(condition);
        notifyRefreshItemCount();
    }

    public void filter(Timetag timetag) {
        if (behavior == null) return;
        behavior.timetag(timetag);
        notifyRefreshItemCount();
    }

    public void removeFilter(String grouping) {
        behavior.removeFilter(grouping);
        notifyRefreshItemCount();
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

    @SuppressWarnings("unchecked")
    public <T> List<T> items(String... sortings) {
        return behavior.items(sortings);
    }

    public long itemCount() {
        return behavior.itemCount();
    }

    public void selection(List<String> selection) {
        this.selection = selection;
        new ArrayList<>(selectionListeners).forEach(l -> l.accept(new SelectionEvent(this, itemsOf(selection))));
        notifier.refreshSelection(selection);
    }

    public int findItem(Function<Object, Boolean> checker) {
        List<Display> children = children();
        int index;
        for (index=0; index<children.size(); index++) {
            if (checker.apply(itemOf(children.get(index)))) return index;
        }
        return -1;
    }

    public void select(int index) {
        List<Display> children = children();
        if (children.size() <= 0) return;
        if (index < 0) return;
        if (index > children.size()-1) return;
        selection(Collections.singletonList(children.get(index).id()));
    }

    public void refresh(int index, Object item) {
        List<Display> children = children();
        if (children.size() <= 0) return;
        if (index < 0) return;
        if (index > children.size()-1) return;
        ((CollectionItemDisplay)children.get(index)).update(item);
        addItemListener().ifPresent(l -> l.accept(itemEvent(children.get(index), index)));
    }

    public void selectAll() {
        selection(children().stream().map(Display::id).collect(Collectors.toList()));
    }

    public boolean canSelectPreviousItem() {
        List<Display> children = children();
        if (children.size() <= 0) return false;
        int index = indexOfSelection();
        return index > 0;
    }

    public Object selectPreviousItem() {
        List<Display> children = children();
        if (children.size() <= 0) return null;
        int index = indexOfSelection()-1;
        if (index < 0) index = 0;
        selection(Collections.singletonList(children.get(index).id()));
        return itemOf(children.get(index));
    }

    public boolean canSelectNextItem() {
        List<Display> children = children();
        if (children.size() <= 0) return false;
        int index = indexOfSelection();
        return index < children.size()-1;
    }

    public Object selectNextItem() {
        List<Display> children = children();
        if (children.size() <= 0) return null;
        int index = indexOfSelection()+1;
        if (index >= children.size()) index = children.size()-1;
        selection(Collections.singletonList(children.get(index).id()));
        return itemOf(children.get(index));
    }

    private List<Object> itemsOf(List<String> selection) {
        return children().stream().filter(d -> selection.contains(d.id())).map(this::itemOf).collect(toList());
    }

    @Override
    public List<Display> children() {
        return super.children().stream().filter(c -> c instanceof CollectionItemDisplay).collect(Collectors.toList());
    }

    protected Object itemOf(Display display) {
        return (display instanceof CollectionItemDisplay) ? ((CollectionItemDisplay)display).item() : null;
    }

    public void clear() {
        clear("rows");
    }

	public void loading(boolean value) {
		notifier.refreshLoading(value);
	}

    protected void addSelectionListener(SelectionListener listener) {
        this.selectionListeners.add(listener);
    }

    protected void removeSelectionListener(SelectionListener listener) {
        this.selectionListeners.remove(listener);
    }

    protected abstract AddItemEvent itemEvent(Display c, int index);

    Optional<AddItemListener> addItemListener() {
        return Optional.ofNullable(addItemListener);
    }

    void notifyRefresh() {
        if (refreshListeners.size() <= 0) return;
        List<Object> items = children().stream().filter(c -> c instanceof CollectionItemDisplay).map(d -> ((CollectionItemDisplay) d).item()).collect(toList());
        refreshListeners.forEach(l -> l.accept(new RefreshEvent(this, items)));
    }

    void notifyRefreshItemCount() {
        if (refreshItemCountListeners.size() <= 0) return;
        long count = behavior.itemCount();
        notifier.refreshItemCount(count);
        refreshItemCountListeners.forEach(l -> l.accept(new RefreshCountEvent(this, count)));
    }

    void notifyReady() {
        readyListeners.forEach(l -> l.accept(new Event(this)));
        ready = true;
    }

    void setup() {
        if (source == null) return;
        behavior.setup(source);
        notifier.setup(new CollectionSetup().itemCount(behavior.itemCount()));
        notifyReady();
    }

    private int indexOfSelection() {
        if (selection == null || selection.size() <= 0) return -1;
        List<Display> children = children();
        int index;
        for (index=0; index<children.size(); index++) {
            if (selection.contains(children.get(index).id())) return index;
        }
        return -1;
    }

}