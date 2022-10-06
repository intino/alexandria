package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.OpenListener;
import io.intino.alexandria.ui.displays.notifiers.OpenBlockNotifier;

public class OpenBlock<DN extends OpenBlockNotifier, B extends Box> extends AbstractOpenBlock<DN, B> {
	private BlockConditional block;
	private OpenListener openListener = null;

	@Override
	public void didMount() {
		super.didMount();
		notifier.refreshVisibility(visible());
	}

	public OpenBlock(B box) {
        super(box);
    }

    public OpenBlock<DN, B> onOpen(OpenListener listener) {
		this.openListener = listener;
		return this;
	}

	public OpenBlock<DN, B> bindTo(BlockConditional<?, ?> block) {
		this.block = block;
		updateState();
		return this;
	}

	public void execute() {
		if (this.block != null) this.block.show();
		if (openListener != null) openListener.accept(new Event(this));
	}

	private void updateState() {
		if (block == null) return;
		block.onShow(e -> hide());
		block.onHide(e -> show());
		visible(!block.isVisible());
	}

}