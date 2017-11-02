package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.Box;
import io.intino.alexandria.foundation.activity.displays.ActivityDisplay;
import io.intino.alexandria.foundation.activity.displays.DisplayNotifier;
import io.intino.alexandria.framework.box.helpers.TimeScaleHandler;
import io.intino.alexandria.framework.box.model.TimeScale;

import java.util.List;

public abstract class AlexandriaNavigatorDisplay<DN extends DisplayNotifier> extends ActivityDisplay<DN> {
    private TimeScaleHandler timeScaleHandler;

    public AlexandriaNavigatorDisplay(Box box) {
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