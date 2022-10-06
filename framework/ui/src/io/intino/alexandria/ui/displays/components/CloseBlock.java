package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.CloseListener;
import io.intino.alexandria.ui.displays.notifiers.CloseBlockNotifier;

public class CloseBlock<DN extends CloseBlockNotifier, B extends Box> extends AbstractCloseBlock<DN, B> {
	private BlockConditional block;
	private CloseListener closeListener = null;

	public CloseBlock(B box) {
		super(box);
	}

	@Override
	public void didMount() {
		super.didMount();
		notifier.refreshVisibility(visible());
	}

	public CloseBlock<DN, B> onClose(CloseListener listener) {
		this.closeListener = listener;
		return this;
	}

	public CloseBlock<DN, B> bindTo(BlockConditional<?, ?> block) {
		this.block = block;
		updateState();
		return this;
	}

	public void execute() {
		if (this.block != null) this.block.hide();
		if (closeListener != null) closeListener.accept(new Event(this));
	}

	private void updateState() {
		if (block == null) return;
		block.onShow(e -> show());
		block.onHide(e -> hide());
		visible(block.isVisible());
	}

}