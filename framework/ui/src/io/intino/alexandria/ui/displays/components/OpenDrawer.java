package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.operation.OpenListener;
import io.intino.alexandria.ui.displays.notifiers.OpenDrawerNotifier;

public class OpenDrawer<DN extends OpenDrawerNotifier, B extends Box> extends AbstractOpenDrawer<DN, B> {
	private BlockDrawer drawer;
	private OpenListener openListener = null;

	public OpenDrawer(B box) {
		super(box);
	}

	public OpenDrawer onOpen(OpenListener listener) {
		this.openListener = listener;
		return this;
	}

	public OpenDrawer bindTo(BlockDrawer drawer) {
		this.drawer = drawer;
		drawer.onShow(d -> hide());
		drawer.onHide(d -> show());
		return this;
	}

	public void execute() {
		if (this.drawer == null) return;
		this.drawer.open();
		if (openListener != null) openListener.accept(new Event(this));
	}
}