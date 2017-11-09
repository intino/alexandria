package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.box.displays.ActivityDisplay;
import io.intino.konos.alexandria.activity.box.displays.notifiers.AlexandriaPageContainerDisplayNotifier;
import io.intino.konos.alexandria.activity.box.schemas.PageLocation;

public class AlexandriaPageContainerDisplay extends ActivityDisplay<AlexandriaPageContainerDisplayNotifier> {
    private PageLocation location;

    public AlexandriaPageContainerDisplay(Box box) {
        super(box);
    }

    public AlexandriaPageContainerDisplay pageLocation(PageLocation location) {
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