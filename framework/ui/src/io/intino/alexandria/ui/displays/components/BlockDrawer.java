package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.operation.ToggleEvent;
import io.intino.alexandria.ui.displays.events.operation.ToggleListener;
import io.intino.alexandria.ui.displays.notifiers.BlockDrawerNotifier;

import java.util.ArrayList;

public class BlockDrawer<DN extends BlockDrawerNotifier, B extends Box> extends AbstractBlockDrawer<B> {
    private boolean opened = false;
    private java.util.List<ToggleListener> toggleListeners = new ArrayList<>();

    public BlockDrawer(B box) {
        super(box);
    }

    public BlockDrawer onToggle(ToggleListener listener) {
        this.toggleListeners.add(listener);
        return this;
    }

	public boolean isOpened() {
    	return opened;
	}

	public BlockDrawer open() {
		update(true);
		return this;
	}

	public BlockDrawer close() {
		update(false);
		return this;
	}

    public BlockDrawer toggle() {
    	update(!this.opened);
    	return this;
    }

    private void update(boolean value) {
		this.opened = value;
		notifyToggle();
	}

    private void notifyToggle() {
        notifier.refresh(opened);
        toggleListeners.forEach(l -> l.accept(new ToggleEvent(this, opened)));
		updateVisibility(opened);
    }
}