package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.SelectorComboBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorComboBox<DN extends SelectorComboBoxNotifier, B extends Box> extends AbstractSelectorComboBox<B> {
	private java.util.List<String> selection = new ArrayList<>();
	private boolean multipleSelection = false;
	private List<SelectionListener> onSelectionListeners = new ArrayList<>();

	public SelectorComboBox(B box) {
        super(box);
    }

    public SelectorComboBox<DN, B> multipleSelection(boolean value) {
    	this.multipleSelection = value;
    	return this;
	}

	public SelectorComboBox<DN, B> onSelect(SelectionListener selectionListener) {
		this.onSelectionListeners.add(selectionListener);
		return this;
	}

	public void select(String... options) {
		this.selection = Arrays.asList(options);
		notifier.refreshSelection(selection);
	}

	public void updateSelection(String[] selection) {
		this.selection = Arrays.asList(selection);
		notifySelection();
	}

	private void notifySelection() {
		onSelectionListeners.forEach(l -> l.accept(new SelectionEvent(this, selection)));
	}
}