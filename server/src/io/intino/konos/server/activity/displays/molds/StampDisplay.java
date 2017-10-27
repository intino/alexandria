package io.intino.konos.server.activity.displays.molds;

import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.DisplayNotifier;
import io.intino.konos.server.activity.displays.elements.model.Item;

public class StampDisplay<N extends DisplayNotifier> extends Display<N> {
    private Item item;

    public Item item() {
        return item;
    }

    public StampDisplay item(Item item) {
        this.item = item;
        return this;
    }
}
