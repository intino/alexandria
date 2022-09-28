package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.Grid;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.GridDatasource;
import io.intino.alexandria.ui.model.datasource.Group;
import io.intino.alexandria.ui.model.datasource.grid.GridColumn;
import io.intino.alexandria.ui.model.datasource.grid.GridGroupBy;
import io.intino.alexandria.ui.model.datasource.grid.GridItem;
import io.intino.alexandria.ui.model.datasource.grid.GridValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GridExamplesMold extends AbstractGridExamplesMold<UiFrameworkBox> {

    public GridExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        grid.itemResolver(new Grid.ItemResolver<>() {
            @Override
            public GridItem build(GridItem gridItem) {
                return gridItem;
            }

            @Override
            public String address(GridColumn<GridItem> column, GridItem gridItem) {
                return column.address().replace(":id", gridItem.values().get(0).asText());
            }
        });
        grid.allowMultiSelection(true);
        grid.onSelect(e -> {
            List<GridItem> selection = e.selection();
            String message = "Selected rows: " + selection.stream().map(e1 -> e1.values().get(0).asText()).collect(Collectors.joining(", "));
            notifyUser(message, UserMessage.Type.Info);
        });
        grid.onSortColumn(e -> notifyUser("Sort by " + e.column() + " with mode " + e.mode().name(), UserMessage.Type.Info));
        grid.source(new ExampleDatasource());
    }

    private static class ExampleDatasource extends GridDatasource<GridItem> {

        private static final int ColumnCount = 20;
        private static final int RowCount = 1000;

        @Override
        public String name() {
            return "grid1";
        }

        @Override
        public List<GridItem> items(int start, int count, String condition, List<Filter> filters, List<String> sortings, GridGroupBy groupBy) {
            List<GridItem> items = groupBy(load(condition, filters));
            int from = Math.min(start, items.size());
            int end = Math.min(start + count, items.size());
            return items.subList(from, end);
        }

        @Override
        public long itemCount(String condition, List<Filter> filters) {
            return load(condition, filters).size();
        }

        @Override
        public List<Group> groups(String key) {
            return new ArrayList<>() {{
                add(new Group().name("Lorem ipsum dolor sit amet lorem ipsum").label("Lorem ipsum dolor sit amet lorem ipsum"));
                add(new Group().name("ipsum dolor sit amet lorem ipsum").label("Lorem ipsum dolor sit amet lorem ipsum"));
                add(new Group().name("dolor sit amet lorem ipsum").label("Lorem ipsum dolor sit amet lorem ipsum"));
                add(new Group().name("sit amet lorem ipsum").label("Lorem ipsum dolor sit amet lorem ipsum"));
            }};
        }

        private List<GridItem> load(String condition, List<Filter> filters) {
            List<GridItem> result = new ArrayList<>();
            for (int i=0; i<RowCount; i++) {
                GridItem item = new GridItem();
                for (int j=0; j<ColumnCount; j++) {
                    item.add(i + "." + j);
                }
                result.add(item);
            }
            return result;
        }

        private List<GridItem> groupBy(List<GridItem> items) {
            return items;
        }

    }
}