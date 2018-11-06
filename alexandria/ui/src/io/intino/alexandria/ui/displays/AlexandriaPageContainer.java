package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaPageContainerNotifier;
import io.intino.alexandria.ui.schemas.PageLocation;

public class AlexandriaPageContainer extends ActivityDisplay<AlexandriaPageContainerNotifier, Box> {
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