package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.documentation.Item;

public class CollectionExamplesMold extends AbstractCollectionExamplesMold<UiFrameworkBox> {

    public CollectionExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
        collection1.add(new Item().label("Item 1"));
        collection1.add(new Item().label("Item 2"));
    }
}