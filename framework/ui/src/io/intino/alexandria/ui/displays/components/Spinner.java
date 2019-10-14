package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SpinnerNotifier;

public class Spinner<DN extends SpinnerNotifier, B extends Box> extends AbstractSpinner<B> {

    public Spinner(B box) {
        super(box);
    }

    public void showLoading() {
        notifier.refreshLoading(true);
    }

    public void hideLoading() {
        notifier.refreshLoading(false);
    }

}