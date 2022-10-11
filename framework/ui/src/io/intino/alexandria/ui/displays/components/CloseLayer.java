package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.actionable.CloseLayerEvent;
import io.intino.alexandria.ui.displays.events.actionable.CloseLayerListener;
import io.intino.alexandria.ui.displays.events.actionable.OpenLayerEvent;
import io.intino.alexandria.ui.displays.events.actionable.OpenLayerListener;
import io.intino.alexandria.ui.displays.notifiers.CloseLayerNotifier;

public class CloseLayer<DN extends CloseLayerNotifier, B extends Box> extends AbstractCloseLayer<DN, B> {
	private CloseLayerListener closeListener;

	public CloseLayer(B box) {
        super(box);
    }

	public CloseLayer<DN, B> onClose(CloseLayerListener listener) {
		this.closeListener = listener;
		return this;
	}

	public void execute() {
		Layer<?, ?> layer = soul().currentLayer();
		if (layer == null) return;
		layer.close();
		if (closeListener != null) closeListener.accept(new CloseLayerEvent(this, layer));
	}
}