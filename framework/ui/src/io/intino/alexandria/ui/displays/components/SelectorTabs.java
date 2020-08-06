package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.selector.SelectorOption;
import io.intino.alexandria.ui.displays.notifiers.SelectorTabsNotifier;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SelectorTabs<DN extends SelectorTabsNotifier, B extends Box> extends AbstractSelectorTabs<DN, B> {
	private int selected = -1;
	private Set<Integer> hiddenOptions = new HashSet<>();

    public SelectorTabs(B box) {
        super(box);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<String> selection() {
		if (selected == -1) return Collections.emptyList();
		return Collections.singletonList(nameOf(selected));
	}

	public void selection(String option) {
		int position = position(option);
		if (position == -1) return;
		selection(position);
	}

	public void selection(int option) {
		notifier.refreshSelected(option);
		if (this.selected == option) return;
		this.selected = option;
	}

	public void select(String option) {
    	selection(option);
		notifySelection();
	}

	public void select(int option) {
    	selection(option);
		notifySelection();
	}

	public void showOption(String option) {
		int position = position(option);
		hiddenOptions.remove(position);
		notifier.refreshOptionsVisibility(new ArrayList<>(hiddenOptions));
	}

	public void hideOption(String option) {
		int position = position(option);
		hiddenOptions.add(position);
		notifier.refreshOptionsVisibility(new ArrayList<>(hiddenOptions));
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
	public void reset() {
		selected = -1;
		notifier.refreshSelected(null);
	}

}