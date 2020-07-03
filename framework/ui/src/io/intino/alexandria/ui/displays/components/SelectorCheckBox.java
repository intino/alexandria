package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SelectorCheckBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorCheckBox<DN extends SelectorCheckBoxNotifier, B extends Box> extends AbstractSelectorCheckBox<DN, B> {
	private List<String> selection = new ArrayList<>();

    public SelectorCheckBox(B box) {
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
		updateSelection(Arrays.asList(options));
	}

	public void updateSelection(List<String> selection) {
		this.selection = new ArrayList<>(selection);
		notifier.refreshSelection(this.selection);
		notifySelection();
	}
}