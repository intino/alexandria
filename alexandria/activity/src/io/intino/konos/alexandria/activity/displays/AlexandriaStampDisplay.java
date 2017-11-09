package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.Item;

public class AlexandriaStampDisplay<N extends AlexandriaDisplayNotifier> extends AlexandriaDisplay<N> {
    private Item item;

    public Item item() {
        return item;
    }

    public AlexandriaStampDisplay item(Item item) {
        this.item = item;
        return this;
    }
}
