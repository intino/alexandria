package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.Box;
import io.intino.alexandria.foundation.activity.displays.ActivityDisplay;
import io.intino.alexandria.framework.box.displays.notifiers.AlexandriaPageContainerDisplayNotifier;
import io.intino.alexandria.framework.box.schemas.PageLocation;

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