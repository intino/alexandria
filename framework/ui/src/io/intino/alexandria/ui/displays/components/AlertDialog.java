package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlertDialogNotifier;

public class AlertDialog<DN extends AlertDialogNotifier, B extends Box> extends AbstractAlertDialog<DN, B> {

    public AlertDialog(B box) {
        super(box);
    }

}