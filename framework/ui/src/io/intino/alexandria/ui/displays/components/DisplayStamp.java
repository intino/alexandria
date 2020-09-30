package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.notifiers.DisplayStampNotifier;

import java.util.UUID;

public class DisplayStamp<DN extends DisplayStampNotifier, B extends Box> extends AbstractDisplayStamp<B> {
    private Display display;

    public DisplayStamp(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        add(this.display);
        refresh();
    }

    public <D extends Display> D get() {
        return (D) this.display;
    }

    public <D extends Display> D display() {
        return get();
    }

    public void display(Display display) {
        this.display = display;
        this.display.id(UUID.randomUUID().toString());
    }

    @Override
    public void refresh() {
        super.refresh();
        if (display != null) display.refresh();
    }
}