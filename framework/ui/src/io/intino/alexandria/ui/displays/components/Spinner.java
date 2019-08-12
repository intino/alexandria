package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class Spinner<B extends Box> extends AbstractSpinner<B> {

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