package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.BlockResizableVisibility;
import io.intino.alexandria.ui.displays.events.LayoutChangedEvent;
import io.intino.alexandria.ui.displays.events.LayoutChangedListener;
import io.intino.alexandria.ui.displays.notifiers.BlockResizableNotifier;

public class BlockResizable<DN extends BlockResizableNotifier, B extends Box> extends AbstractBlockResizable<B> {
    private LayoutChangedListener layoutChangedListener;

    public BlockResizable(B box) {
        super(box);
    }

    public void onLayoutChanged(LayoutChangedListener listener) {
        this.layoutChangedListener = listener;
    }

    public void refreshLayout(Double... percentages) {
        notifier.refreshLayout(java.util.List.of(percentages));
    }

    public void layoutModified(java.util.List<Double> percentages) {
        if (layoutChangedListener == null) return;
        layoutChangedListener.accept(new LayoutChangedEvent(this, percentages));
    }

    protected void refreshVisibility(String child, boolean visible) {
        notifier.refreshChildVisibility(new BlockResizableVisibility().child(child).visible(visible));
    }

}