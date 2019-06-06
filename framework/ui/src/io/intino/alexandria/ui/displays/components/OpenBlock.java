package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.operation.OpenListener;
import io.intino.alexandria.ui.displays.notifiers.OpenBlockNotifier;

public class OpenBlock<DN extends OpenBlockNotifier, B extends Box> extends AbstractOpenBlock<DN, B> {
	private BlockConditional block;
	private OpenListener openListener = null;

	public OpenBlock(B box) {
        super(box);
    }

    public OpenBlock onOpen(OpenListener listener) {
		this.openListener = listener;
		return this;
	}

	public OpenBlock bindTo(BlockConditional block) {
		this.block = block;
		return this;
	}

	public void execute() {
		this.block.show();
		if (openListener != null) openListener.accept(new Event(this));
	}
}