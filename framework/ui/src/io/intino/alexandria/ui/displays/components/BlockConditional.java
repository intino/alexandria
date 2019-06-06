package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.selector.Selection;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.HideListener;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.events.ShowListener;
import io.intino.alexandria.ui.displays.notifiers.BlockConditionalNotifier;

public class BlockConditional<DN extends BlockConditionalNotifier, B extends Box> extends AbstractBlockConditional<B> implements Selection {
    private boolean visible;
    private boolean readyNotified = false;
    private ShowListener showListener = null;
    private HideListener hideListener = null;
    private Listener readyListener = null;

    public BlockConditional(B box) {
        super(box);
    }

    @Override
    public void add(Component container) {
        super.add(container);
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isHidden() {
        return !visible;
    }

    public void visible(boolean value) {
        updateVisibility(value);
    }

    public void hidden(boolean value) {
        updateVisibility(!value);
    }

    public void show() {
        this.updateVisibility(true);
    }

    public void hide() {
        this.updateVisibility(false);
    }

    public BlockConditional<DN, B> onReady(Listener listener) {
        this.readyListener = listener;
        return this;
    }

    public BlockConditional<DN, B> onShow(ShowListener listener) {
        this.showListener = listener;
        return this;
    }

    public BlockConditional<DN, B> onHide(HideListener listener) {
        this.hideListener = listener;
        return this;
    }

    @Override
    public void bindTo(Selector selector, String option) {
        updateVisibility(selector, option);
        selector.onSelect(e -> updateVisibility(e.option().equals(option)));
    }

    private void updateVisibility(boolean value) {
        this.visible = value;
        notifier.refreshVisibility(visible);
        if (visible) {
            refresh();
            notifyReady();
            children().forEach(Display::refresh);
        }
        if (showListener != null && visible) showListener.accept(new Event(this));
        if (hideListener != null && !visible) hideListener.accept(new Event(this));
    }

    private void updateVisibility(Selector selector, String option) {
        if (selector == null) return;
        String selectedOption = selector.selectedOption();
        if (selectedOption == null || !selectedOption.equals(option)) return;
        updateVisibility(true);
    }

    protected void notifyReady() {
        if (readyNotified) return;
        if (readyListener != null) readyListener.accept(new Event(this));
        readyNotified = true;
    }
}