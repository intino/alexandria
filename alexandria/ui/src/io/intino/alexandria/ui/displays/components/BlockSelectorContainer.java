package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.components.selector.SelectorContainer;

public class BlockSelectorContainer<B extends Box> extends AbstractBlockSelectorContainer<B> implements SelectorContainer {
    private int selected;

    public BlockSelectorContainer(B box) {
        super(box);
    }

    @Override
    public void add(Component container) {
        super.add(container);
    }

    @Override
    public void bindTo(Selector selector) {
        selector.onSelect(e -> refreshSelected(e.position()));
    }

    private Component component(String name) {
        return this.children(Component.class).stream().filter(p -> p.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private void refreshSelected(int position) {
        Component component = child(position);
        if (selected == position) return;
        if (component == null) return;
        this.selected = position;
        notifier.refreshSelected(position);
    }

}