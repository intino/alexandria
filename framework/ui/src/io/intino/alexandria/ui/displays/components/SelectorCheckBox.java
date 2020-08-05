package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SelectorCheckBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorCheckBox<DN extends SelectorCheckBoxNotifier, B extends Box> extends AbstractSelectorCheckBox<DN, B> {
	private List<String> selection = new ArrayList<>();
	private boolean readonly;

    public SelectorCheckBox(B box) {
        super(box);
    }

	public boolean readonly() {
		return readonly;
	}

	public SelectorCheckBox<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

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

	protected SelectorCheckBox<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}
}