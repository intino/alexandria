package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.toolbar.SelectionOperation;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.TaskSelectionNotifier;

public abstract class TaskSelection<DN extends TaskSelectionNotifier, B extends Box> extends AbstractTaskSelection<DN, B> {
	private SelectionListener executeListener;

    public TaskSelection(B box) {
        super(box);
    }

	public void onExecute(SelectionListener listener) {
		this.executeListener = listener;
	}

	public void execute() {
		if (this.executeListener == null) return;
		this.executeListener.accept(new SelectionEvent(this, selection()));
	}
}