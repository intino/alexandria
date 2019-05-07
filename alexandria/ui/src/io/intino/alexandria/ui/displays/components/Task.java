package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.TaskListener;
import io.intino.alexandria.ui.displays.notifiers.TaskNotifier;

public class Task<DN extends TaskNotifier, B extends Box> extends AbstractTask<DN, B> {
    private TaskListener taskListener;

    public Task(B box) {
        super(box);
    }

    public void onExecute(TaskListener listener) {
        this.taskListener = listener;
    }

    public void execute() {
        if (this.taskListener == null) return;
        this.taskListener.accept(new Event(this));
    }

}