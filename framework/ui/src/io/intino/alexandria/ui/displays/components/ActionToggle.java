package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.actionable.ToggleEvent;
import io.intino.alexandria.ui.displays.events.actionable.ToggleListener;
import io.intino.alexandria.ui.displays.notifiers.ActionToggleNotifier;

public class ActionToggle<DN extends ActionToggleNotifier, B extends Box> extends AbstractActionToggle<DN, B> {
    private ToggleEvent.State state;
    private ToggleListener toggleListener;

    public ActionToggle(B box) {
        super(box);
    }

    public ActionToggle<DN, B> onToggle(ToggleListener listener) {
        this.toggleListener = listener;
        return this;
    }

    public ToggleEvent.State state() {
        return this.state;
    }

    public ActionToggle<DN, B> state(ToggleEvent.State state) {
        _state(state);
        notifyToggle();
        return this;
    }

    public void toggle() {
        state = state == ToggleEvent.State.On ? ToggleEvent.State.Off : ToggleEvent.State.On;
        notifyToggle();
    }

    protected ActionToggle<DN, B> _state(ToggleEvent.State state) {
        this.state = state;
        return this;
    }

    private void notifyToggle() {
        notifier.refreshState(state.name());
        if (this.toggleListener == null) return;
        this.toggleListener.accept(new ToggleEvent(this, state));
    }

}