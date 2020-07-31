package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.Selectable;
import io.intino.alexandria.ui.displays.notifiers.SelectorCollectionBoxNotifier;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.PageDatasource;
import io.intino.alexandria.ui.utils.DelayerUtil;

import java.util.*;
import java.util.List;

public abstract class SelectorCollectionBox<DN extends SelectorCollectionBoxNotifier, B extends Box> extends AbstractSelectorCollectionBox<DN, B> {
    private boolean readonly;
    private Object selected = null;
    private Collection collection;
    private SearchBox searchBox;
    private ValueProvider valueProvider;

    public SelectorCollectionBox(B box) {
        super(box);
    }

    public boolean readonly() {
        return readonly;
    }

    public SelectorCollectionBox<DN, B> readonly(boolean readonly) {
        _readonly(readonly);
        notifier.refreshReadonly(readonly);
        return this;
    }

    public SelectorCollectionBox<DN, B> source(Datasource source) {
        collection.source(source);
        return this;
    }

    public void addAndBindTo(Selectable collection) {
        add((Collection)collection, DefaultInstanceContainer);
        bindTo(collection);
    }

    public void bindTo(Selectable collection) {
        this.collection = (Collection) collection;
        searchBox.bindTo((Collection) collection);
        collection().ifPresent(c -> collection.onSelect((event) -> updateSelection(event.selection())));
    }

    public SelectorCollectionBox<DN, B> valueProvider(ValueProvider valueProvider) {
        this.valueProvider = valueProvider;
        return this;
    }

    public void search(String value) {
        collection.filter(value);
    }

    public void selection(String... selection) {
        selection(Arrays.asList(selection));
    }

    @Override
    public void init() {
        super.init();
        createSearchBox();
        collection().ifPresent(Collection::init);
    }

    @Override
    public List<String> selection() {
        if (selected == null) return Collections.emptyList();
        return Collections.singletonList(selected.toString());
    }

    @Override
    public void reset() {
        selected = null;
        notifier.refreshSelected(null);
    }

    public void opened() {
        searchBox.search("");
        collection().ifPresent(Collection::reload);
    }

    public <T> void selection(T... selection) {
        selection(Arrays.asList(selection));
    }

    public <T> void selection(List<T> selection) {
        this.selected = selection.size() > 0 ? selection.get(0) : null;
        notifier.refreshSelected(valueOf(selected));
    }

    protected SelectorCollectionBox<DN, B> _readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    private <T> void updateSelection(List<T> selection) {
        selection(selection);
        notifySelection(selection);
    }

    private String valueOf(Object selected) {
        if (selected == null) return null;
        return valueProvider != null ? valueProvider.valueOf(selected) : translate("Inject value provider to selector");
    }

    private Optional<Collection> collection() {
        return Optional.ofNullable(collection);
    }

    private void createSearchBox() {
        searchBox = new SearchBox<>(box()).id(UUID.randomUUID().toString());
        add(searchBox, DefaultInstanceContainer);
    }

    public interface ValueProvider {
        String valueOf(Object element);
    }
}