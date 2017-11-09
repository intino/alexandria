package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplayNotifier;
import io.intino.konos.alexandria.framework.box.model.Item;

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
