package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.model.Geometry;

public class LocationExamplesMold extends AbstractLocationExamplesMold<UiFrameworkBox> {

    public LocationExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        location3.onChange(this::notifyChange);
        location4.onChange(this::notifyChange);
    }

    private void notifyChange(ChangeEvent event) {
        Geometry value = event.value();
        String message = value != null ? "Location modified to: " + value.toWkt() : "Location removed";
        notifyUser(message, UserMessage.Type.Info);
    }
}