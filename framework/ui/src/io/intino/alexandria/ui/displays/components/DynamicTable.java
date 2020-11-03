package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.schemas.DynamicTableRowParams;
import io.intino.alexandria.schemas.DynamicTableSetup;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.DynamicTableCollectionBehavior;
import io.intino.alexandria.ui.displays.components.collection.builders.DynamicTableBuilder;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.collection.SelectRowEvent;
import io.intino.alexandria.ui.displays.events.collection.SelectRowListener;
import io.intino.alexandria.ui.displays.notifiers.DynamicTableNotifier;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.DynamicTableDatasource;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DynamicTable<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Row, Item> extends AbstractDynamicTable<DynamicTableNotifier, B> implements Collection<ItemComponent, Item> {
    private SelectRowListener selectRowListener;

    public DynamicTable(B box) {
        super(box);
    }

    @Override
    public void source(Datasource source) {
        source(source, new DynamicTableCollectionBehavior<Item>(this));
    }

    public String dimension() {
        DynamicTableCollectionBehavior behavior = behavior();
        if (behavior == null) return null;
        return behavior.dimension();
    }

    public DynamicTable dimension(String dimension) {
        DynamicTableCollectionBehavior behavior = behavior();
        if (behavior == null) return this;
        behavior.dimension(dimension);
        return this;
    }

    public String drill() {
        DynamicTableCollectionBehavior behavior = behavior();
        if (behavior == null) return null;
        return behavior.drill();
    }

    public DynamicTable drill(String drill) {
        DynamicTableCollectionBehavior behavior = behavior();
        if (behavior == null) return this;
        behavior.drill(drill);
        return this;
    }

    public DynamicTable onSelectRow(SelectRowListener listener) {
        this.selectRowListener = listener;
        return this;
    }

    @Override
    protected AddItemEvent itemEvent(Display display, int index) {
        return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item(), index);
    }

    @Override
    public void loadMoreItems(CollectionMoreItems info) {
        DynamicTableCollectionBehavior behavior = behavior();
        behavior.moreItems(info);
    }

    @Override
    public void changePage(Integer page) {
        DynamicTableCollectionBehavior behavior = behavior();
        behavior.page(page);
        notifier.refresh();
    }

    @Override
    public void changePageSize(Integer size) {
        DynamicTableCollectionBehavior behavior = behavior();
        behavior.pageSize(size);
        notifier.refresh();
    }

    @Override
    public ItemComponent add(Item item) {
        ItemComponent component = create(item);
        addPromise(component, "rows");
        return component;
    }

    @Override
    public java.util.List<ItemComponent> add(java.util.List<Item> items) {
        java.util.List<ItemComponent> components = items.stream().map(this::create).collect(Collectors.toList());
        addPromise(components, "rows");
        return components;
    }

    @Override
    public ItemComponent insert(Item item, int index) {
        ItemComponent component = create(item);
        insertPromise(component, index, "rows");
        return component;
    }

    @Override
    public java.util.List<ItemComponent> insert(java.util.List<Item> items, int from) {
        java.util.List<ItemComponent> components = items.stream().map(this::create).collect(Collectors.toList());
        insertPromise(components, from, "rows");
        return components;
    }

    public void refreshSections(List<Section> sections) {
        notifier.sections(DynamicTableBuilder.buildList(sections, language()));
        notifier.selectRowProvided(selectRowListener != null);
    }

    public void selectRow(DynamicTableRowParams params) {
        if (selectRowListener == null) return;
        SelectRowEvent event = new SelectRowEvent(this, sectionOf(params.section()), params.row());
        notifier.openRow(selectRowListener.accept(event));
    }

    public void showItems(DynamicTableRowParams params) {
        Section section = sectionOf(params.section());
        DynamicTableCollectionBehavior behavior = behavior();
        behavior.section(section);
        behavior.row(params.row());
        behavior.reload();
        notifier.setup(new DynamicTableSetup().openRowExternal(selectRowListener != null).pageSize(pageSize()).itemCount(behavior.itemCount()));
    }

    private Section sectionOf(String section) {
        DynamicTableCollectionBehavior behavior = behavior();
        List<Section> sections = behavior.sections();
        return sections.stream().filter(s -> s.label().equals(section)).findFirst().orElse(null);
    }

    @Override
    void setup() {
        DynamicTableDatasource source = source();
        if (source == null) return;
        DynamicTableCollectionBehavior behavior = behavior();
        behavior.setup(source, pageSize());
        notifier.setup(new DynamicTableSetup().openRowExternal(selectRowListener != null).pageSize(pageSize()).itemCount(behavior.itemCount()));
        notifyReady();
    }

}