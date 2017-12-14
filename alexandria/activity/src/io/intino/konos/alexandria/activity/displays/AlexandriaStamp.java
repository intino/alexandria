package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.Item;

public class AlexandriaStamp<N extends AlexandriaDisplayNotifier> extends AlexandriaDisplay<N> {
    private Item item;

    public Item item() {
        return item;
    }

    public AlexandriaStamp item(Item item) {
        this.item = item;
        return this;
    }
}
