package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.foundation.activity.displays.Display;
import io.intino.alexandria.foundation.activity.displays.DisplayNotifier;
import io.intino.alexandria.framework.box.model.Item;

public class AlexandriaStampDisplay<N extends DisplayNotifier> extends Display<N> {
    private Item item;

    public Item item() {
        return item;
    }

    public AlexandriaStampDisplay item(Item item) {
        this.item = item;
        return this;
    }
}
