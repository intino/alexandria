package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.DynamicTableCollectionBehavior;
import io.intino.alexandria.ui.displays.components.collection.builders.DynamicTableBuilder;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.events.collection.OpenRowEvent;
import io.intino.alexandria.ui.displays.events.collection.OpenRowListener;
import io.intino.alexandria.ui.displays.events.collection.SelectRowsEvent;
import io.intino.alexandria.ui.displays.events.collection.SelectRowsListener;
import io.intino.alexandria.ui.displays.notifiers.DynamicTableNotifier;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.DynamicTableDatasource;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public abstract class DynamicTable<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Row, Item> extends AbstractDynamicTable<DynamicTableNotifier, B> implements Collection<ItemComponent, Item> {
    private Listener openingRowListener;
    private OpenRowListener openRowListener;
    private SelectRowsListener selectRowsListener;
    private List<DynamicTableVisibleColumn> visibleColumns = new ArrayList<>();
    private boolean showZeros = true;
    private boolean showPercentages = false;
    private View view = View.Normal;
    private Function<String, String> translator;

    public DynamicTable(B box) {
        super(box);
    }

    public enum View { Normal, Indicator }

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

    public DynamicTable onOpeningRow(Listener listener) {
        this.openingRowListener = listener;
        return this;
    }

    public DynamicTable onOpenRow(OpenRowListener listener) {
        this.openRowListener = listener;
        return this;
    }

    public DynamicTable onSelectRows(SelectRowsListener listener) {
        this.selectRowsListener = listener;
        return this;
    }

    public DynamicTable openView(View view) {
        notifier.openView(view.name());
        return this;
    }

    public DynamicTable translator(Function<String, String> translator) {
        this.translator = translator;
        return this;
    }

    @Override
    protected AddItemEvent itemEvent(Display display, int index) {
        return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item(), index);
    }

    @Override
    public String translate(String word) {
        return translator != null ? translator.apply(word) : super.translate(word);
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

    public List<DynamicTableVisibleColumn> visibleColumns() {
        return visibleColumns;
    }

    public DynamicTable<B, ItemComponent, Item> visibleColumns(List<DynamicTableVisibleColumn> visibleColumns) {
        this.visibleColumns = visibleColumns;
        notifier.refreshVisibleColumns(visibleColumns);
        return this;
    }

    public boolean showZeros() {
        return showZeros;
    }

    public DynamicTable<B, ItemComponent, Item> showZeros(Boolean value) {
        this.showZeros = value;
        notifier.refreshZeros(value);
        return this;
    }

    public boolean showPercentages() {
        return showPercentages;
    }

    public DynamicTable<B, ItemComponent, Item> showPercentages(Boolean value) {
        this.showPercentages = value;
        notifier.refreshPercentages(value);
        return this;
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
        notifier.refreshTable(table(sections));
        notifier.openRowProvided(openRowListener != null);
    }

    private DynamicTableData table(List<Section> sections) {
        DynamicTableData result = new DynamicTableData();
        result.dimension(translate(dimension()));
        result.drill(translate(drill()));
        result.sections(DynamicTableBuilder.buildList(sections, language()));
        return result;
    }

    public void openRow(DynamicTableRowParams params) {
        if (openingRowListener != null) openingRowListener.accept(new Event(this));
        if (openRowListener == null) return;
        OpenRowEvent event = new OpenRowEvent(this, sectionOf(params.section()), params.row());
        notifier.openRow(openRowListener.accept(event));
    }

    public void selectRows(List<DynamicTableRowsParams> selection) {
        if (selectRowsListener == null) return;
        SelectRowsEvent event = new SelectRowsEvent(this, toMap(selection));
        selectRowsListener.accept(event);
    }

    public void showItems(DynamicTableRowParams params) {
        Section section = sectionOf(params.section());
        DynamicTableCollectionBehavior behavior = behavior();
        behavior.section(section);
        behavior.row(params.row());
        behavior.reload();
        notifier.setup(new DynamicTableSetup().visibleColumns(visibleColumns).openRowExternal(openRowListener != null).selectRowsEnabled(selectRowsListener != null).pageSize(pageSize()).itemCount(behavior.itemCount()));
    }

    private Section sectionOf(String section) {
        DynamicTableCollectionBehavior behavior = behavior();
        List<Section> sections = behavior.sections();
        return sections.stream().filter(s -> s.label().equals(section)).findFirst().orElse(null);
    }

    @Override
    public void didMount() {
        DynamicTableCollectionBehavior behavior = behavior();
        DynamicTableDatasource source = source();
        notifier.setup(new DynamicTableSetup().visibleColumns(visibleColumns).name(source != null ? source.name() : null).openRowExternal(openRowListener != null).selectRowsEnabled(selectRowsListener != null).pageSize(pageSize()).itemCount(behavior.itemCount()));
        notifyReady();
    }

    @Override
    void setup() {
        DynamicTableDatasource source = source();
        if (source == null) return;
        DynamicTableCollectionBehavior behavior = behavior();
        behavior.setup(source, pageSize());
        notifier.setup(new DynamicTableSetup().visibleColumns(visibleColumns).name(source.name()).openRowExternal(openRowListener != null).selectRowsEnabled(selectRowsListener != null).pageSize(pageSize()).itemCount(behavior.itemCount()));
        notifyReady();
    }

    private Map<String, List<String>> toMap(List<DynamicTableRowsParams> selection) {
        Map<String, List<String>> result = new HashMap<>();
        selection.forEach(s -> result.put(s.section(), s.rows()));
        return result;
    }

}