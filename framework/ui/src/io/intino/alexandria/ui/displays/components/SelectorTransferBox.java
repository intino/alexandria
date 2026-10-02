package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SelectorTransferBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorTransferBox<DN extends SelectorTransferBoxNotifier, B extends Box> extends AbstractSelectorTransferBox<DN, B> {
	private List<String> selection = new ArrayList<>();

	public SelectorTransferBox(B box) {
		super(box);
	}

	@Override
	public void didMount() {
		super.didMount();
		selection(selection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> selection() {
		return selection;
	}

	public SelectorTransferBox<DN, B> multipleSelection(boolean multipleSelection) {
		_multipleSelection(multipleSelection);
		notifier.refreshMultipleSelection(multipleSelection);
		return this;
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
