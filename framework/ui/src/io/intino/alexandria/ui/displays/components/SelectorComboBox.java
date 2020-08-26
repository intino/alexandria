package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.selector.SelectorOption;
import io.intino.alexandria.ui.displays.notifiers.SelectorComboBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorComboBox<DN extends SelectorComboBoxNotifier, B extends Box> extends AbstractSelectorComboBox<DN, B> {
	private java.util.List<String> selection = new ArrayList<>();

	public SelectorComboBox(B box) {
        super(box);
    }

	@Override
	public void didMount() {
		super.didMount();
		updateSelection(selection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> selection() {
		return selection;
	}

	public SelectorComboBox<DN, B> multipleSelection(boolean multipleSelection) {
		_multipleSelection(multipleSelection);
		notifier.refreshMultipleSelection(multipleSelection);
		return this;
	}

	@Override
	public void reset() {
		select();
	}

	@Override
	public void refresh() {
		super.refresh();
		children().stream().filter(c -> c instanceof SelectorOption).forEach(Display::refresh);
	}

	public void selection(String... selection) {
		selection(Arrays.asList(selection));
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