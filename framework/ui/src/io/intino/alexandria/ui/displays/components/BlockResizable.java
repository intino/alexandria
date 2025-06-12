package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.BlockResizableVisibility;
import io.intino.alexandria.ui.displays.notifiers.BlockResizableNotifier;

public class BlockResizable<DN extends BlockResizableNotifier, B extends Box> extends AbstractBlockResizable<B> {

    public BlockResizable(B box) {
        super(box);
    }

    public void refreshLayout(Integer... percentages) {
        notifier.refreshLayout(java.util.List.of(percentages));
    }

    protected void refreshVisibility(String child, boolean visible) {
        notifier.refreshChildVisibility(new BlockResizableVisibility().child(child).visible(visible));
    }

}