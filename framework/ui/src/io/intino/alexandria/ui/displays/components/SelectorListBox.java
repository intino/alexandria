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

	@SuppressWarnings("unchecked")
	@Override
	public List<String> selection() {
		return selection;
	}

	@Override
	public void reset() {
		select();
	}

	public void selection(String... options) {
		selection(Arrays.asList(options));
	}

	public void selection(List<String> selection) {
		this.selection = selection;
		notifier.refreshSelection(this.selection);
	}

	public void select(String... options) {
		updateSelection(Arrays.asList(options));
	}

	public void updateSelection(List<String> options) {
		selection(options);
		notifySelection();
	}
}