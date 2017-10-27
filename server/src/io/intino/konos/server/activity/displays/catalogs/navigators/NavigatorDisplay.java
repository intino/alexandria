package io.intino.konos.server.activity.displays.catalogs.navigators;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.ActivityDisplay;
import io.intino.konos.server.activity.displays.DisplayNotifier;
import io.intino.konos.server.activity.displays.elements.model.TimeScale;
import io.intino.konos.server.activity.helpers.TimeScaleHandler;

import java.util.List;

public abstract class NavigatorDisplay<DN extends DisplayNotifier> extends ActivityDisplay<DN> {
    private TimeScaleHandler timeScaleHandler;

    public NavigatorDisplay(Box box) {
        super(box);
    }

    public List<TimeScale> scales() {
        return timeScaleHandler.availableScales();
    }

    public TimeScaleHandler timeScaleHandler() {
        return timeScaleHandler;
    }

    public void timeScaleHandler(TimeScaleHandler timeScaleHandler) {
        this.timeScaleHandler = timeScaleHandler;
        addListeners(timeScaleHandler);
    }

    protected abstract void addListeners(TimeScaleHandler timeScaleHandler);

}