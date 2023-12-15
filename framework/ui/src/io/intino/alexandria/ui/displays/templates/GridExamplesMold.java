package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.Grid;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.GridDatasource;
import io.intino.alexandria.ui.model.datasource.Group;
import io.intino.alexandria.ui.model.datasource.grid.GridColumn;
import io.intino.alexandria.ui.model.datasource.grid.GridColumnMode;
import io.intino.alexandria.ui.model.datasource.grid.GridGroupBy;
import io.intino.alexandria.ui.model.datasource.grid.GridItem;
import io.intino.alexandria.ui.services.push.UISession;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
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
                return column.address() + "/" + gridItem.values().get(0).asText();
            }
        });
        grid.allowMultiSelection(true);
        grid.onSelect(e -> {
            List<GridItem> selection = e.selection();
            String message = "Selected rows: " + selection.stream().map(e1 -> e1.values().get(0).asText()).collect(Collectors.joining(", "));
            notifyUser(message, UserMessage.Type.Info);
        });
        grid.onSortColumn(e -> {
            grid.sortings(Collections.emptyList());
            notifyUser("Sort by " + e.column().label() + " with mode " + e.mode().name(), UserMessage.Type.Info);
        });
        grid.source(new ExampleDatasource(session()));
    }

    private static class ExampleDatasource extends GridDatasource<GridItem> {
        private final UISession session;

        private static final int ColumnCount = 22;
        private static final int RowCount = 1000;

        private ExampleDatasource(UISession session) {
            this.session = session;
        }

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
        public long itemCount(String condition, List<Filter> filters, GridGroupBy groupBy) {
            return groupBy(load(condition, filters)).size();
        }

        @Override
        public List<String> columnGroups(GridColumn<GridItem> column, String mode, String condition, List<Filter> filters) {
            if (mode == null || mode.equals("Distinct values")) return List.of("Lorem ipsum dolor sit amet lorem ipsum", "ipsum dolor sit amet lorem ipsum", "dolor sit amet lorem ipsum", "sit amet lorem ipsum");
            return List.of("lorem", "ipsum", "dolor", "sit");
        }

        @Override
        public List<GridColumnMode> columnModes() {
            return List.of(
                new GridColumnMode("Distinct values"),
                new GridColumnMode("First letter", GridColumn.Type.Link, GridColumn.Type.Text),
                new GridColumnMode("Year", GridColumn.Type.Date),
                new GridColumnMode("Year and month", GridColumn.Type.Date)
            );
        }

        @Override
        public List<Group> groups(String key) {
            return Collections.emptyList();
        }

        private List<GridItem> load(String condition, List<Filter> filters) {
            List<GridItem> result = new ArrayList<>();
            for (int i=0; i<RowCount; i++) {
                GridItem item = new GridItem();
                for (int j=0; j<ColumnCount; j++) {
                    if (j == 0) item.add("Check", "blue");
                    else if (j == 1) item.add(Asset.toResource(urlOf(session.browser().baseAssetUrl()), randomIcon()).toUrl().toString());
                    else if (j == 3) item.add(Instant.now());
                    else item.add(i + "." + j);
                }
                result.add(item);
            }
            return result;
        }

        private String randomIcon() {
            double random = Math.random();
            return "/icons/" + (random < 0.5 ? "process.ico" : "task.ico");
        }

        private URL urlOf(String url) {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        private List<GridItem> groupBy(List<GridItem> items) {
            return items;
        }

    }
}