package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.items.Mold;
import io.intino.alexandria.ui.documentation.Item;

public class CollectionExamplesMold extends AbstractCollectionExamplesMold<UiFrameworkBox> {

    public CollectionExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        collection1.onAddItem(event -> {
            Mold template = event.component();
            //template.stamp.item(event.item());
            Item item = event.item();
//            template.stamp.update(item);
            template.label.update(item.label());
            template.avatar.update(item.label());
            template.refresh();
        });
    }

    @Override
    public void refresh() {
        super.refresh();
        collection1.add(new Item().label("Item 1"));
        collection1.add(new Item().label("Item 2"));
        collection1.add(new Item().label("Item 3"));
        collection1.add(new Item().label("Item 4"));
        collection1.add(new Item().label("Item 5"));
        collection1.add(new Item().label("Item 6"));
        collection1.add(new Item().label("Item 7"));
        collection1.add(new Item().label("Item 8"));
        collection1.add(new Item().label("Item 9"));
        collection1.add(new Item().label("Item 10"));
        collection1.add(new Item().label("Item 11"));
        collection1.add(new Item().label("Item 12"));
        collection1.add(new Item().label("Item 13"));
        collection1.add(new Item().label("Item 14"));
        collection1.add(new Item().label("Item 15"));
        collection1.add(new Item().label("Item 16"));
        collection1.add(new Item().label("Item 17"));
        collection1.add(new Item().label("Item 18"));
        collection1.add(new Item().label("Item 19"));
        collection1.add(new Item().label("Item 20"));
        collection1.add(new Item().label("Item 21"));
    }

}