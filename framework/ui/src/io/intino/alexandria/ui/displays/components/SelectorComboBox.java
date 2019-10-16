package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SelectorComboBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorComboBox<DN extends SelectorComboBoxNotifier, B extends Box> extends AbstractSelectorComboBox<DN, B> {
	private java.util.List<String> selection = new ArrayList<>();
	private boolean readonly;

	public SelectorComboBox(B box) {
        super(box);
    }

	@Override
	public List<String> selection() {
		return selection;
	}

	public boolean readonly() {
		return readonly;
	}

	public SelectorComboBox<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	@Override
	public void reset() {
		select();
	}

	public void select(String... options) {
		updateSelection(options);
	}

	public void updateSelection(String[] selection) {
		this.selection = Arrays.asList(selection);
		notifier.refreshSelection(this.selection);
		notifySelection();
	}

	protected SelectorComboBox<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

}