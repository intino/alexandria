package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPageContainerNotifier;
import io.intino.konos.alexandria.activity.schemas.PageLocation;

public class AlexandriaPageContainer extends ActivityDisplay<AlexandriaPageContainerNotifier> {
    private PageLocation location;

    public AlexandriaPageContainer(Box box) {
        super(box);
    }

    public AlexandriaPageContainer pageLocation(PageLocation location) {
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