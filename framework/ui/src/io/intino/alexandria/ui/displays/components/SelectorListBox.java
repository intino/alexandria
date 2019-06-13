package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SelectorListBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorListBox<DN extends SelectorListBoxNotifier, B extends Box> extends AbstractSelectorListBox<DN, B> {
	private java.util.List<String> selection = new ArrayList<>();

    public SelectorListBox(B box) {
        super(box);
    }

	@Override
	public List<String> selection() {
		return selection;
	}

	@Override
	public void reset() {
		select();
	}

	public void select(String... options) {
		this.selection = Arrays.asList(options);
		notifier.refreshSelection(selection);
	}

	public void updateSelection(String[] selection) {
		this.selection = Arrays.asList(selection);
		notifySelection();
	}
}