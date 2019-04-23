package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.items.Collection1Mold;
import io.intino.alexandria.ui.displays.items.Collection2Mold;
import io.intino.alexandria.ui.documentation.Item;
import io.intino.alexandria.ui.model.Datasource;

import java.util.ArrayList;
import java.util.List;

public class CollectionExamplesMold extends AbstractCollectionExamplesMold<UiFrameworkBox> {

    public CollectionExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        init(collection1);
        init(collection2);
        init(collection3);
    }

    private void init(io.intino.alexandria.ui.displays.components.List list) {
        list.source(datasource());
        list.onAddItem(this::onAddItem);
    }

    private void init(io.intino.alexandria.ui.displays.components.Table table) {
        table.source(datasource());
        table.onAddItem(this::onAddItem);
    }

    private Datasource<Item> datasource() {
        return new Datasource<Item>() {
            @Override
            public int itemCount(String condition) {
                return 1000;
            }

            @Override
            public List<Item> items(int start, int count, String condition) {
                List<Item> result = new ArrayList<>();
                for (int i = start; i < start + count; i++)
                    result.add(new Item().label("Item " + i));
                return result;
            }
        };
    }

    private void onAddItem(AddItemEvent event) {
        Item item = event.item();
        if (event.component() instanceof Collection1Mold) ((Collection1Mold)event.component()).stamp.update(item);
        else if (event.component() instanceof Collection2Mold) ((Collection2Mold)event.component()).stamp.update(item);
        event.component().refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
//        collection1.add(new Item().label("Item 1"));
//        collection1.add(new Item().label("Item 2"));
//        collection1.add(new Item().label("Item 3"));
//        collection1.add(new Item().label("Item 4"));
//        collection1.add(new Item().label("Item 5"));
//        collection1.add(new Item().label("Item 6"));
//        collection1.add(new Item().label("Item 7"));
//        collection1.add(new Item().label("Item 8"));
//        collection1.add(new Item().label("Item 9"));
//        collection1.add(new Item().label("Item 10"));
//        collection1.add(new Item().label("Item 11"));
//        collection1.add(new Item().label("Item 12"));
//        collection1.add(new Item().label("Item 13"));
//        collection1.add(new Item().label("Item 14"));
//        collection1.add(new Item().label("Item 15"));
//        collection1.add(new Item().label("Item 16"));
//        collection1.add(new Item().label("Item 17"));
//        collection1.add(new Item().label("Item 18"));
//        collection1.add(new Item().label("Item 19"));
//        collection1.add(new Item().label("Item 20"));
//        collection1.add(new Item().label("Item 21"));
    }

}