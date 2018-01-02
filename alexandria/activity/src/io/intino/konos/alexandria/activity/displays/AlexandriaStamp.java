package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.displays.providers.AlexandriaStampProvider;
import io.intino.konos.alexandria.activity.model.Item;

public class AlexandriaStamp<N extends AlexandriaDisplayNotifier> extends AlexandriaDisplay<N> {
    private Item item;
    private AlexandriaStampProvider provider;

    public Item item() {
        return item;
    }

    public AlexandriaStamp item(Item item) {
        this.item = item;
        return this;
    }

    public AlexandriaStamp provider(AlexandriaStampProvider provider) {
        this.provider = provider;
        return this;
    }

    public AlexandriaStamp embeddedDisplay(String stamp) {
        return provider.embeddedDisplay(stamp);
    }

    public AlexandriaCatalog embeddedCatalog(String stamp) {
        return provider.embeddedCatalog(stamp);
    }
}
