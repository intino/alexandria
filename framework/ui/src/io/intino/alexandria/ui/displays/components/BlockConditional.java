package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.components.selector.Selection;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.events.actionable.ToggleEvent;
import io.intino.alexandria.ui.displays.notifiers.BlockConditionalNotifier;

public abstract class BlockConditional<DN extends BlockConditionalNotifier, B extends Box> extends AbstractBlockConditional<B> implements Selection {
    private boolean initialized = false;
    private Listener initListener = null;

    public BlockConditional(B box) {
        super(box);
        updateVisible(false);
    }

//    @Override
//    public void didMount() {
//        super.didMount();
//        if (isVisible()) notifyVisibility();
//    }

    @Override
    public void add(Component container) {
        super.add(container);
    }

    public BlockConditional<DN, B> onInit(Listener listener) {
        this.initListener = listener;
        return this;
    }

    public BlockConditional<DN, B> focus() {
        notifier.refreshFocused(true);
        return this;
    }

    @Override
    public void bindTo(Selector selector, String option) {
        updateVisibility(selector, option);
        selector.onSelect(e -> updateVisibility(e.selection().contains(option)));
    }

    @Override
    public void bindTo(ActionToggle action, String option) {
        updateVisibility(action, option);
        action.onToggle(e -> updateVisibility(e.state() == ToggleEvent.State.On));
    }

    public abstract void initConditional();

    @Override
    protected void updateVisibility(boolean value) {
        updateVisible(value);
        notifier.refreshVisibility(value);
        if (isVisible()) initComponent();
        notifyVisibility();
    }

    private void updateVisibility(Selector selector, String option) {
        if (selector == null) return;
        java.util.List<String> selection = selector.selection();
        if (selection.size() <= 0 || !selection.contains(option)) return;
        updateVisibility(true);
    }

    private void updateVisibility(ActionToggle action, String option) {
        if (action == null) return;
        ToggleEvent.State state = action.state();
        updateVisibility(state == ToggleEvent.State.On);
    }

    protected void initComponent() {
        if (initialized) return;
        initConditional();
        if (initListener != null) initListener.accept(new Event(this));
        initialized = true;
    }
}