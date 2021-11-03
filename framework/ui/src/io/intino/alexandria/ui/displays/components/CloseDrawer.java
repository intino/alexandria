package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.CloseListener;
import io.intino.alexandria.ui.displays.events.actionable.ToggleEvent;
import io.intino.alexandria.ui.displays.notifiers.CloseDrawerNotifier;

public class CloseDrawer<DN extends CloseDrawerNotifier, B extends Box> extends AbstractCloseDrawer<DN, B> {
	private BlockDrawer drawer;
	private CloseListener closeListener;

	public CloseDrawer(B box) {
		super(box);
	}

	public CloseDrawer onClose(CloseListener listener) {
		this.closeListener = listener;
		return this;
	}

	public CloseDrawer bindTo(BlockDrawer drawer) {
		this.drawer = drawer;
		updateVisibility(drawer.isOpened());
		drawer.onToggle(e -> {
			if (e.state() == ToggleEvent.State.Off) hide();
			else show();
		});
		return this;
	}

	public void execute() {
		if (this.drawer == null) return;
		this.drawer.close();
		if (closeListener != null) closeListener.accept(new Event(this));
	}
}