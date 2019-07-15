package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.BlockSplitterNotifier;

import java.util.List;

public class BlockSplitter<DN extends BlockSplitterNotifier, B extends Box> extends AbstractBlockSplitter<B> {
    private Listener backListener;

    public BlockSplitter(B box) {
        super(box);
    }

    public BlockSplitter<DN, B> onBack(Listener listener) {
        this.backListener = listener;
        return this;
    }

    public BlockSplitter<DN, B> show(Component component) {
        List<Display> children = children();
        int index;
        for (index = 0; index < children.size(); index++)
            if (children.get(index) == component) break;
        if (index >= children.size()) return this;
        notifier.show(index);
        return this;
    }

    public void back() {
        notifier.show(0);
        if (backListener != null) backListener.accept(new Event(this));
    }
}