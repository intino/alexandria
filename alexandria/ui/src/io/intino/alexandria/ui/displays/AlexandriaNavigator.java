package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.helpers.TimeScaleHandler;
import io.intino.alexandria.ui.model.TimeScale;

import java.util.List;

public abstract class AlexandriaNavigator<DN extends AlexandriaDisplayNotifier> extends ActivityDisplay<DN, Box> {
    private TimeScaleHandler timeScaleHandler;

    public AlexandriaNavigator(Box box) {
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