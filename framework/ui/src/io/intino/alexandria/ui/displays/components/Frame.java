package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.notifiers.FrameNotifier;

import java.util.UUID;

public class Frame<DN extends FrameNotifier, B extends Box> extends AbstractFrame<B> {
    private Display display;

    public Frame(B box) {
        super(box);
    }

    public <D extends Display> D get() {
        return (D) this.display;
    }

    public void display(Display display) {
        this.display = display;
        this.display.id(UUID.randomUUID().toString());
    }

    @Override
    public void refresh() {
        super.refresh();
        if (display != null) add(display);
    }

}