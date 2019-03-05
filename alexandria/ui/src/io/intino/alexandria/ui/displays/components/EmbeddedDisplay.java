package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;

public class EmbeddedDisplay<B extends Box> extends AbstractEmbeddedDisplay<B> {
    private Display display;

    public EmbeddedDisplay(B box) {
        super(box);
    }

    public void set(Display display) {
        this.display = display;
    }

    @Override
    public void init() {
        super.init();
        notifier.add(display);
    }
}