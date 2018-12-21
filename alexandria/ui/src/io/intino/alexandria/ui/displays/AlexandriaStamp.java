package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.displays.providers.AlexandriaStampProvider;
import io.intino.alexandria.ui.model.Item;

import java.net.MalformedURLException;
import java.net.URL;

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

    public AlexandriaAbstractCatalog embeddedCatalog(String stamp) {
        return provider.embeddedCatalog(stamp);
    }

    public URL baseAssetUrl() {
        try {
            return new URL(session().browser().baseAssetUrl());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public void refreshItem() {
        provider.refreshItem(true);
    }

    public void refreshItem(boolean highlight) {
        provider.refreshItem(highlight);
    }

    public void refreshElement() {
        provider.refreshElement();
    }

    public void resizeItem() { provider.resizeItem(); }

}
