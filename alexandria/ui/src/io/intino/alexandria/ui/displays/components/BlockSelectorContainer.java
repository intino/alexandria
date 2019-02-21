package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.components.selector.SelectorContainer;

public class BlockSelectorContainer<B extends Box> extends AbstractBlockSelectorContainer<B> implements SelectorContainer {
    private String selected;

    public BlockSelectorContainer(B box) {
        super(box);
    }

    @Override
	public void select(String name) {
        Component component = component(name);
        if (component == null) return;
        if (selected != null && selected.equals(name)) return;
        this.selected = name;
        notifier.refreshSelected(name);
    }

    @Override
    public void add(Component container) {
        addAndPersonify(container);
    }

    @Override
    public void bindTo(Selector selector) {
        selector.onSelect(e -> select(e.option()));
    }

    private Component component(String name) {
        return this.children(Component.class).stream().filter(p -> p.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}