package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SelectorToggleBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorToggleBox<DN extends SelectorToggleBoxNotifier, B extends Box> extends AbstractSelectorToggleBox<DN, B> {
	private java.util.List<String> selection = new ArrayList<>();
	private java.util.List<String> disabledOptions = new ArrayList<>();

	public SelectorToggleBox(B box) {
		super(box);
	}

	@Override
	public void didMount() {
		super.didMount();
		selection(selection);
	}

	public void enableOptions(String... options) {
		disabledOptions.removeAll(List.of(options));
		notifier.refreshDisabledOptions(disabledOptions);
	}

	public void disableOptions(String... options) {
		disabledOptions.addAll(List.of(options));
		notifier.refreshDisabledOptions(disabledOptions);
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