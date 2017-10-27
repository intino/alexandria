package io.intino.konos.server.activity.displays.elements.items;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.ActivityDisplay;
import io.intino.konos.server.activity.displays.schemas.PageLocation;

public class PageContainerDisplay extends ActivityDisplay<PageContainerDisplayNotifier> {
    private PageLocation location;

    public PageContainerDisplay(Box box) {
        super(box);
    }

    public PageContainerDisplay pageLocation(PageLocation location) {
        this.location = location;
        return this;
    }

    @Override
    public void refresh() {
        super.refresh();
        sendLocation();
    }

    private void sendLocation() {
        notifier.refreshLocation(location);
    }
}