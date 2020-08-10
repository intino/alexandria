package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ActionListener;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.notifiers.AlertDialogNotifier;

public class AlertDialog<DN extends AlertDialogNotifier, B extends Box> extends AbstractAlertDialog<DN, B> {
    private ActionListener acceptListener;

    public AlertDialog(B box) {
        super(box);
    }

    public AlertDialog<DN, B> onAccept(ActionListener listener) {
        this.acceptListener = listener;
        return this;
    }

    public void accept() {
        if (acceptListener != null) acceptListener.accept(new Event(this));
        close();
    }
}