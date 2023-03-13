package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.GridCollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.collection.CellClickEvent;
import io.intino.alexandria.ui.displays.events.collection.CellClickListener;
import io.intino.alexandria.ui.displays.events.collection.SortColumnEvent;
import io.intino.alexandria.ui.displays.events.collection.SortColumnListener;
import io.intino.alexandria.ui.displays.notifiers.GridNotifier;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.GridDatasource;
import io.intino.alexandria.ui.model.datasource.grid.GridGroupBy;
import io.intino.alexandria.ui.model.datasource.grid.GridItem;
import io.intino.alexandria.ui.model.datasource.grid.GridValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Grid<DN extends GridNotifier, B extends Box, Item> extends AbstractGrid<DN, B> implements Collection<Display, Item> {
    private SortColumnListener sortColumnListener;
    private CellClickListener cellClickListener;
    private List<io.intino.alexandria.ui.model.datasource.grid.GridColumn<Item>> columns = new ArrayList<>();
    private List<GridColumn> visibleColumns = new ArrayList<>();
    private ItemResolver<Item> itemResolver;
    private List<Item> items = new ArrayList<>();

    public Grid(B box) {
        super(box);
    }

    @Override
    protected AddItemEvent itemEvent(Display c, int index) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Datasource> void source(D source) {
        GridDatasource<Item> gridDatasource = (GridDatasource<Item>) source;
        notifier.refreshInfo(new GridInfo().name(((GridDatasource<?>)source).name()).columns(schemaOf(columns)).modes(schemaModesOf(gridDatasource.columnModes())));
        source(source, new GridCollectionBehavior<Item>(this));
        notifier.loadState(((GridDatasource<?>) source).name());
    }

    public void itemResolver(ItemResolver<Item> resolver) {
        this.itemResolver = resolver;
    }

    public io.intino.alexandria.ui.model.datasource.grid.GridColumn<Item> column(String name) {
        return columns().stream().filter(c -> c.name().equals(name)).findFirst().orElse(null);
    }

    public List<io.intino.alexandria.ui.model.datasource.grid.GridColumn<Item>> columns() {
        return columns;
    }

    public List<GridColumn> visibleColumns() {
        return visibleColumns.stream().filter(GridColumn::visible).collect(toList());
    }

    public Grid<DN, B, Item> visibleColumns(List<io.intino.alexandria.ui.model.datasource.grid.GridColumn<Item>> columns) {
        updateVisibleColumns(schemaOf(columns));
        return this;
    }

    public void updateVisibleColumns(List<GridColumn> visibleColumns) {
        this.visibleColumns = visibleColumns;
        notifier.refreshVisibleColumns(visibleColumns);
    }

    public Grid<DN, B, Item> onSortColumn(SortColumnListener listener) {
        this.sortColumnListener = listener;
        return this;
    }

    public Grid<DN, B, Item> onClickCell(CellClickListener listener) {
        this.cellClickListener = listener;
        return this;
    }

    public void sort(GridSortInfo info) {
        if (sortColumnListener == null) return;
        SortColumnEvent.Mode mode = info.mode() != null ? SortColumnEvent.Mode.from(info.mode()) : SortColumnEvent.Mode.None;
        notifier.refreshSort(info);
        sortColumnListener.accept(new SortColumnEvent(this, column(info.column()), mode));
    }

    public void updateState(GridState state) {
        if (state == null) return;
        if (!state.visibleColumns().isEmpty()) updateVisibleColumns(state.visibleColumns());
        if (state.sort() != null && SortColumnEvent.Mode.from(state.sort().mode()) != SortColumnEvent.Mode.None) sort(state.sort());
        if (state.groupBy() != null) {
            updateGroupByOptions(new GridGroupByOptionsInfo().column(state.groupBy().column()).mode(state.groupBy().mode()));
            groupBy(state.groupBy());
        }
    }

    @SuppressWarnings("unchecked")
    public void updateGroupByOptions(GridGroupByOptionsInfo info) {
        GridCollectionBehavior<Item> behavior = behavior();
        refreshGroupByOptions(info);
        if (behavior.groupBy() != null) behavior.groupBy(null);
    }

    public void groupBy(GridGroupByInfo info) {
        GridCollectionBehavior<Item> behavior = behavior();
        notifier.refreshGroupBy(info);
        behavior.groupBy(info.group() != null ? groupByOf(info) : null);
    }

    private void refreshGroupByOptions(GridGroupByOptionsInfo info) {
        GridCollectionBehavior<Item> behavior = behavior();
        GridDatasource<Item> source = source();
        List<String> groupByOptions = info.column() != null ? source.columnGroups(column(info.column()), info.mode(), behavior.condition(), behavior.filters()) : Collections.emptyList();
        notifier.refreshGroupByOptions(groupByOptions);
    }

    private GridGroupBy groupByOf(GridGroupByInfo info) {
        return new GridGroupBy().column(column(info.column())).group(info.group()).groupIndex(info.groupIndex()).mode(info.mode());
    }

    public void cellClick(GridCellInfo info) {
        if (cellClickListener == null) return;
        Item item = items.size() > info.rowIndex() ? items.get(info.rowIndex()) : null;
        cellClickListener.accept(new CellClickEvent(this, info.column(), info.row(), item));
    }

    protected Grid<DN, B, Item> _add(io.intino.alexandria.ui.model.datasource.grid.GridColumn<Item> column) {
        columns.add(column);
        return this;
    }

    private java.util.List<GridColumn> schemaOf(List<io.intino.alexandria.ui.model.datasource.grid.GridColumn<Item>> columns) {
        return columns.stream().map(this::schemaOf).collect(Collectors.toList());
    }

    private GridColumn schemaOf(io.intino.alexandria.ui.model.datasource.grid.GridColumn<?> column) {
        GridColumn result = new GridColumn();
        result.name(column.name());
        result.label(column.label());
        result.type(GridColumn.Type.valueOf(column.type().name()));
        result.sortable(column.sortable());
        result.fixed(column.fixed());
        result.width(column.width());
        result.visible(column.visible());
        return result;
    }

    private List<GridColumnMode> schemaModesOf(List<io.intino.alexandria.ui.model.datasource.grid.GridColumnMode> columnModes) {
        return columnModes.stream().map(this::schemaOf).collect(toList());
    }

    private GridColumnMode schemaOf(io.intino.alexandria.ui.model.datasource.grid.GridColumnMode columnMode) {
        List<GridColumnMode.AcceptedTypes> acceptedTypes = columnMode.acceptedTypes().stream().map(a -> GridColumnMode.AcceptedTypes.valueOf(a.name())).collect(toList());
        return new GridColumnMode().name(columnMode.name()).acceptedTypes(acceptedTypes);
    }

    @Override
    public Display add(Item item) {
        items.add(item);
        notifier.addRow(schemaOf(item));
        return null;
    }

    @Override
    public List<Display> add(List<Item> items) {
        this.items.addAll(items);
        notifier.addRows(items.stream().map(this::schemaOf).collect(Collectors.toList()));
        return null;
    }

    @Override
    public Display insert(Item item, int index) {
        items.add(index, item);
        notifier.addRow(schemaOf(item));
        return null;
    }

    @Override
    public void clear() {
        super.clear();
        items.clear();
    }

    @Override
    public List<Display> insert(List<Item> items, int from) {
        this.items.addAll(from, items);
        notifier.addRows(items.stream().map(this::schemaOf).collect(Collectors.toList()));
        return null;
    }

    @Override
    public Display create(Item gridItem) {
        return null;
    }

    @Override
    protected List<Object> itemsOf(List<String> selection) {
        return selection.stream().map(this::itemOf).filter(Objects::nonNull).collect(toList());
    }

    private Item itemOf(String indexValue) {
        int index = Integer.parseInt(indexValue);
        return index < items.size() ? items.get(index) : null;
    }

    private GridRow schemaOf(Item item) {
        List<GridCell> cells = new ArrayList<>();
        GridItem gridItem = itemResolver.build(item);
        for (int i=0; i<gridItem.values().size(); i++) {
            GridCell schemaOf = schemaOf(gridItem.values().get(i), i, item);
            cells.add(schemaOf);
        }
        return new GridRow().cells(cells);
    }

    @SuppressWarnings("unchecked")
    private GridCell schemaOf(GridValue value, int index, Item item) {
        io.intino.alexandria.ui.model.datasource.grid.GridColumn<Item> column = columns.get(index);
        GridCell result = new GridCell();
        result.value(column.formatter().apply(value));
        if (column.type() == io.intino.alexandria.ui.model.datasource.grid.GridColumn.Type.Link)
            result.address(itemResolver.address(column, item));
        return result;
    }

    public interface ItemResolver<Item> {
        GridItem build(Item item);
        String address(io.intino.alexandria.ui.model.datasource.grid.GridColumn<Item> column, Item item);
    }

}