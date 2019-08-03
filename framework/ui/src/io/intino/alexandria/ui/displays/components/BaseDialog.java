package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.BaseDialogNotifier;

import java.util.ArrayList;

public class BaseDialog<DN extends BaseDialogNotifier, B extends Box> extends AbstractBaseDialog<DN, B> {
    private String title;
    private java.util.List<Listener> beforeOpenListeners = new ArrayList<>();
    private java.util.List<Listener> openListeners = new ArrayList<>();
    private java.util.List<Listener> closeListeners = new ArrayList<>();

    public BaseDialog(B box) {
        super(box);
    }

    public BaseDialog onBeforeOpen(Listener listener) {
        beforeOpenListeners.add(listener);
        return this;
    }

    public BaseDialog<DN, B> title(String title) {
        this.title = title;
        return this;
    }

    public BaseDialog onOpen(Listener listener) {
        openListeners.add(listener);
        return this;
    }

    public BaseDialog onClose(Listener listener) {
        closeListeners.add(listener);
        return this;
    }

    public void open() {
        notifyBeforeOpen();
        notifier.open();
        notifyOpen();
    }

    public void close() {
        notifier.close();
        notifyClose();
    }

    public BaseDialog<DN, B> updateTitle(String title) {
        title(title);
        notifier.refreshTitle(title);
        return this;
    }

    private void notifyBeforeOpen() {
        beforeOpenListeners.forEach(l -> l.accept(new Event(this)));
    }

    private void notifyOpen() {
        openListeners.forEach(l -> l.accept(new Event(this)));
    }

    private void notifyClose() {
        closeListeners.forEach(l -> l.accept(new Event(this)));
    }
}