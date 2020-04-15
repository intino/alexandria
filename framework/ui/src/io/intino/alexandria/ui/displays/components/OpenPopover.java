package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.OpenListener;
import io.intino.alexandria.ui.displays.notifiers.OpenPopoverNotifier;

public class OpenPopover<DN extends OpenPopoverNotifier, B extends Box> extends AbstractOpenPopover<DN, B> {
	private BlockPopover popover;
	private OpenListener openListener = null;

	public OpenPopover(B box) {
		super(box);
	}

	public OpenPopover onOpen(OpenListener listener) {
		this.openListener = listener;
		return this;
	}

	public OpenPopover bindTo(BlockPopover popover) {
		this.popover = popover;
		return this;
	}

	public void execute() {
		if (this.popover == null) return;
		this.popover.open(id());
		if (openListener != null) openListener.accept(new Event(this));
	}
}