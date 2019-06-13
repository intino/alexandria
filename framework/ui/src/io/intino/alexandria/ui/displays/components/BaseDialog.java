package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.BaseDialogNotifier;

import java.util.ArrayList;

public class BaseDialog<DN extends BaseDialogNotifier, B extends Box> extends AbstractBaseDialog<DN, B> {
    private java.util.List<Listener> beforeListeners = new ArrayList<>();

    public BaseDialog(B box) {
        super(box);
    }

    public BaseDialog onBeforeOpen(Listener listener) {
        beforeListeners.add(listener);
        return this;
    }

    public void open() {
        notifyBeforeOpen();
        notifier.open();
    }

    public void close() {
        notifier.close();
    }

    private void notifyBeforeOpen() {
        beforeListeners.forEach(l -> l.accept(new Event(this)));
    }
}