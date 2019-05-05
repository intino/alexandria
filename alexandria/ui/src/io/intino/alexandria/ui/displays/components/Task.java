package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.ExecuteListener;

public class Task<B extends Box> extends AbstractTask<B> {
    private ExecuteListener executeListener;

    public Task(B box) {
        super(box);
    }

    public void onExecute(ExecuteListener listener) {
        this.executeListener = listener;
    }

    public void execute() {
        if (this.executeListener == null) return;
        this.executeListener.accept(new Event(this));
    }
}