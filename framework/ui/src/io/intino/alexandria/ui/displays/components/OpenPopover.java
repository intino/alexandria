package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.OpenListener;
import io.intino.alexandria.ui.displays.notifiers.OpenPopoverNotifier;

import java.util.UUID;

public class OpenPopover<DN extends OpenPopoverNotifier, B extends Box> extends AbstractOpenPopover<DN, B> {
	private BlockPopover popover;
	private OpenListener openListener = null;
	private String triggerId = "t-" + UUID.randomUUID().toString();
	private TriggerEvent triggerEvent;

	public enum TriggerEvent { MouseClick, MouseOver }

	public OpenPopover(B box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		notifier.refreshTriggerId(triggerId);
	}

	public OpenPopover onOpen(OpenListener listener) {
		this.openListener = listener;
		return this;
	}

	public OpenPopover bindTo(BlockPopover popover) {
		this.popover = popover;
		return this;
	}

	public void closePopover() {
		if (this.popover == null) return;
		this.popover.close();
	}

	protected void _triggerEvent(TriggerEvent triggerEvent) {
		this.triggerEvent = triggerEvent;
	}

	public void execute() {
		if (this.popover == null) return;
		this.popover.interactionsEnabled(triggerEvent == TriggerEvent.MouseClick);
		this.popover.open(triggerId);
		if (openListener != null) openListener.accept(new Event(this));
	}
}