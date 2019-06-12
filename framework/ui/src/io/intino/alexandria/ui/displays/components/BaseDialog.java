package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseDialogNotifier;

public class BaseDialog<DN extends BaseDialogNotifier, B extends Box> extends AbstractBaseDialog<DN, B> {

    public BaseDialog(B box) {
        super(box);
    }

    public void open() {
        notifier.open();
    }

    public void close() {
        notifier.close();
    }
}