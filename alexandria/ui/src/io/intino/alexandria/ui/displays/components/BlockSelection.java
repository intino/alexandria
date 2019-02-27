package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.components.selector.Selection;

public class BlockSelection<B extends Box> extends AbstractBlockSelection<B> implements Selection {

    public BlockSelection(B box) {
        super(box);
    }

    @Override
    public void add(Component container) {
        super.add(container);
    }

    @Override
    public void bindTo(Selector selector, String option) {
        updateVisibility(selector, option);
        selector.onSelect(e -> notifier.refreshVisibility(e.option().equals(option)));
    }

    private void updateVisibility(Selector selector, String option) {
        if (selector == null) return;
        String selectedOption = selector.selectedOption();
        if (selectedOption == null || !selectedOption.equals(option)) return;
        notifier.refreshVisibility(true);
    }

}