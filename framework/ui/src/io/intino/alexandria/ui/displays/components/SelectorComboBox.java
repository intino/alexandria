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

	public SelectorComboBox<DN, B> readonly(boolean value) {
		this.readonly = readonly;
		return this;
	}

	public SelectorComboBox<DN, B> updateReadonly(boolean value) {
		readonly(value);
		notifier.refreshReadonly(value);
		return this;
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