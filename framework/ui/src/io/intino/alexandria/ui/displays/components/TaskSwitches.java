package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.operation.ToggleEvent;
import io.intino.alexandria.ui.displays.events.operation.ToggleEvent.State;
import io.intino.alexandria.ui.displays.events.operation.ToggleListener;
import io.intino.alexandria.ui.displays.notifiers.TaskSwitchesNotifier;

public class TaskSwitches<DN extends TaskSwitchesNotifier, B extends Box> extends AbstractTaskSwitches<DN, B> {
    private State state;
    private ToggleListener toggleListener;

    public TaskSwitches(B box) {
        super(box);
    }

    public TaskSwitches onToggle(ToggleListener listener) {
        this.toggleListener = listener;
        return this;
    }

    public State state() {
        return this.state;
    }

    public TaskSwitches state(State state) {
        _state(state);
        notifyToggle();
        return this;
    }

    public void toggle() {
        state = state == State.On ? State.Off : State.On;
        notifyToggle();
    }

    protected TaskSwitches _state(State state) {
        this.state = state;
        return this;
    }

    private void notifyToggle() {
        notifier.refreshState(state.name());
        if (this.toggleListener == null) return;
        this.toggleListener.accept(new ToggleEvent(this, state));
    }

}