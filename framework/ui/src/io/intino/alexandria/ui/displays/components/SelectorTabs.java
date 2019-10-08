package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.selector.SelectorOption;
import io.intino.alexandria.ui.displays.notifiers.SelectorTabsNotifier;

import java.util.Collections;
import java.util.List;

public class SelectorTabs<DN extends SelectorTabsNotifier, B extends Box> extends AbstractSelectorTabs<DN, B> {
	private int selected = -1;

    public SelectorTabs(B box) {
        super(box);
    }

	public void select(String option) {
		int position = position(option);
		if (position == -1) return;
		this.select(position);
	}

	public void select(int option) {
		notifier.refreshSelected(option);
		if (this.selected == option) return;
		this.selected = option;
		notifySelection();
	}

	public void show(String option) {
		SelectorOption component = findOption(position(option));
		component.visible(true);
	}

	public void hide(String option) {
		SelectorOption component = findOption(position(option));
		component.visible(false);
	}

	@Override
	public List<String> selection() {
		if (selected == -1) return Collections.emptyList();
    	return Collections.singletonList(nameOf(selected));
	}

	@Override
	public void reset() {
		selected = -1;
		notifier.refreshSelected(null);
	}

}