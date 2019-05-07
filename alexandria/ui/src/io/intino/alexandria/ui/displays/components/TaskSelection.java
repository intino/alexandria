package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.toolbar.SelectionOperation;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.TaskSelectionNotifier;

import java.util.Collections;
import java.util.List;

public abstract class TaskSelection<DN extends TaskSelectionNotifier, B extends Box> extends AbstractTaskSelection<DN, B> implements SelectionOperation {

    public TaskSelection(B box) {
        super(box);
    }

}