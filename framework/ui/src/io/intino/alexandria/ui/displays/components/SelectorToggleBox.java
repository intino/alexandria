package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.AbstractSelectorToggleBox;
import io.intino.alexandria.ui.displays.notifiers.SelectorListBoxNotifier;
import io.intino.alexandria.ui.displays.notifiers.SelectorToggleBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorToggleBox<DN extends SelectorToggleBoxNotifier, B extends Box> extends AbstractSelectorToggleBox<DN, B> {
	private java.util.List<String> selection = new ArrayList<>();

	public SelectorToggleBox(B box) {
		super(box);
	}

	@SuppressWarnings("unchecked")
	@Override
	public java.util.List<String> selection() {
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
		notifier.refreshSelection(selection);
	}

	public void select(String... options) {
		updateSelection(Arrays.asList(options));
	}

	public void updateSelection(List<String> options) {
		selection(options);
		notifySelection();
	}
}