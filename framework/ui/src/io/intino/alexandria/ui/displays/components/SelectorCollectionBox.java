package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.notifiers.SelectorCollectionBoxNotifier;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.utils.DelayerUtil;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class SelectorCollectionBox<DN extends SelectorCollectionBoxNotifier, B extends Box> extends AbstractSelectorCollectionBox<DN, B> {
    private java.util.List<Object> selection = new ArrayList<>();
    private final java.util.List<OpenValueListener> openListeners = new ArrayList<>();
    private Collection collection;
    private ValueProvider valueProvider;
    private Listener selectOtherListener;
    private String searchCondition;
    private Datasource source;

    public SelectorCollectionBox(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        selection(selection);
    }

    public SelectorCollectionBox<DN, B> onSelectOther(Listener listener) {
        this.selectOtherListener = listener;
        return this;
    }

    public SelectorCollectionBox<DN, B> onOpen(OpenValueListener listener) {
        this.openListeners.add(listener);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> selection() {
        return selection;
    }

    public SelectorCollectionBox<DN, B> multipleSelection(boolean multipleSelection) {
        _multipleSelection(multipleSelection);
        notifier.refreshMultipleSelection(multipleSelection);
        return this;
    }

    public SelectorCollectionBox<DN, B> source(Datasource source) {
        this.source = source;
        return this;
    }

    public void addAndBindTo(Selectable collection) {
        add((Collection)collection, DefaultInstanceContainer);
        bindTo(collection);
    }

    public void bindTo(Selectable collection) {
        this.collection = (Collection) collection;
        collection().ifPresent(c -> collection.onSelect((event) -> {
            updateSelection(event);
            if (multipleSelection()) ((Collection) collection).reload();
            else notifier.close();
        }));
    }

    public SelectorCollectionBox<DN, B> valueProvider(ValueProvider valueProvider) {
        this.valueProvider = valueProvider;
        return this;
    }

    public void search(String value) {
        DelayerUtil.execute(this, x -> {
            if (value.equals(searchCondition)) return;
            collection().ifPresent(c -> c.filter(value));
            searchCondition = value;
        }, 100);
    }

    public void selection(String... selection) {
        selection(Arrays.asList(selection));
    }

    @Override
    public void init() {
        super.init();
        collection().ifPresent(Collection::init);
    }

    @Override
    public void reset() {
        select();
    }

    public void open(String option) {
        openListeners.forEach(l -> l.accept(new ValueEvent(this, option)));
    }

    public void opened() {
        collection().ifPresent(c -> {
            if (source != null) c.source(source);
            else c.reload();
        });
    }

    public <T extends Object> void selection(T... selection) {
        selection(Arrays.asList(selection));
    }

    public <T extends Object> void selection(List<T> selection) {
        this.selection = new ArrayList<>(selection);
        notifier.refreshSelection(this.selection.stream().map(this::valueOf).collect(toList()));
    }

    public <T> void select(T... options) {
        updateSelection(Arrays.asList(options));
    }

    public void selectOther() {
        if (selectOtherListener == null) return;
        selectOtherListener.accept(new Event(this));
    }

    public void clearSelection() {
        updateSelection(new ArrayList<>());
    }

    public void unSelect(String option) {
        updateSelection(selection.stream().filter(i -> !option.equals(valueOf(i))).collect(Collectors.toList()));
        collection().ifPresent(Collection::reload);
    }

    protected void reloadComponents() { // Override base selector behavior
    }

    private void updateSelection(SelectionEvent event) {
        Object selected = event.selection().size() > 0 ? event.selection().get(0) : null;
        if (multipleSelection()) {
            if (selected == null) return;
            boolean found = selection.stream().anyMatch(i -> valueOf(selected).equals(valueOf(i)));
            if (found) selection = selection.stream().filter(i -> !valueOf(selected).equals(valueOf(i))).collect(toList());
            else selection.add(selected);
        } else {
            selection = selected != null ? Collections.singletonList(selected) : Collections.emptyList();
        }
        updateSelection(selection);
    }

    private <T> void updateSelection(List<T> options) {
        selection(options);
        notifySelection(selection);
    }

    private String valueOf(Object selected) {
        if (selected == null) return null;
        return valueProvider != null ? valueProvider.valueOf(selected) : translate("Inject value provider to selector");
    }

    private Optional<Collection> collection() {
        return Optional.ofNullable(collection);
    }

    public interface ValueProvider {
        String valueOf(Object element);
    }

}