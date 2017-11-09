package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.activity.box.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaDisplayNotifier;
import io.intino.konos.alexandria.activity.box.model.Item;

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
