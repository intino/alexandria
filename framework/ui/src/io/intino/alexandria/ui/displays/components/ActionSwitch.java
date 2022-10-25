package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.actionable.ToggleEvent;
import io.intino.alexandria.ui.displays.events.actionable.ToggleListener;
import io.intino.alexandria.ui.displays.notifiers.ActionSwitchNotifier;

public class ActionSwitch<DN extends ActionSwitchNotifier, B extends Box> extends AbstractActionSwitch<DN, B> {
    private ToggleEvent.State state;
    private ToggleListener toggleListener;

    public ActionSwitch(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        if (state != null) notifier.refreshState(state.name());
    }

    public ActionSwitch<DN, B> onToggle(ToggleListener listener) {
        this.toggleListener = listener;
        return this;
    }

    public ToggleEvent.State state() {
        return this.state;
    }

    public ActionSwitch<DN, B> state(ToggleEvent.State state) {
        _state(state);
        notifier.refreshState(state.name());
        return this;
    }

    public ActionSwitch<DN, B> focus() {
        notifier.refreshFocused(true);
        return this;
    }

    public ActionSwitch<DN, B> toggle(ToggleEvent.State state) {
        state(state);
        notifyToggle();
        return this;
    }

    public void toggle() {
        state = state == ToggleEvent.State.On ? ToggleEvent.State.Off : ToggleEvent.State.On;
        notifyToggle();
    }

    protected ActionSwitch<DN, B> _state(ToggleEvent.State state) {
        this.state = state;
        return this;
    }

    private void notifyToggle() {
        notifier.refreshState(state.name());
        if (this.toggleListener == null) return;
        this.toggleListener.accept(new ToggleEvent(this, state));
    }

}