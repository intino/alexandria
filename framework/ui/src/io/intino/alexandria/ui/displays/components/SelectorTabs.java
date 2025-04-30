package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.SelectorTabsOptionVisibility;
import io.intino.alexandria.ui.displays.components.selector.SelectorOption;
import io.intino.alexandria.ui.displays.notifiers.SelectorTabsNotifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectorTabs<DN extends SelectorTabsNotifier, B extends Box> extends AbstractSelectorTabs<DN, B> {
	private int selected = -1;

    public SelectorTabs(B box) {
        super(box);
    }

	@Override
	public void didMount() {
		super.didMount();
		selection(selected);
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

	public void selectByName(String option) {
    	select(option);
	}

	public void select(int option) {
    	selection(option);
		notifySelection();
	}

	public void showOption(String option) {
		hiddenOptions.remove(option);
		notifier.refreshOptionsVisibility(optionsVisibility());
	}

	public void hideOption(String option) {
		hiddenOptions.add(option);
		notifier.refreshOptionsVisibility(optionsVisibility());
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

	private List<SelectorTabsOptionVisibility> optionsVisibility() {
		List<SelectorTabsOptionVisibility> result = new ArrayList<>();
		List<SelectorOption> options = options();
		for (int i=0; i<options.size(); i++) result.add(new SelectorTabsOptionVisibility().index(i).visible(!hiddenOptions.contains(options.get(i).name())));
		return result;
	}

}