package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.ExecuteListener;
import io.intino.alexandria.ui.displays.notifiers.TaskNotifier;

public class Task<DN extends TaskNotifier, B extends Box> extends AbstractTask<DN, B> {
    private String title;
    private String icon;
    private ExecuteListener executeListener;

    public Task(B box) {
        super(box);
    }

    public Task<DN, B> title(String title) {
        this.title = title;
        return this;
    }

    public Task<DN, B> icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void onExecute(ExecuteListener listener) {
        this.executeListener = listener;
    }

    public void execute() {
        if (this.executeListener == null) return;
        this.executeListener.accept(new Event(this));
    }
}