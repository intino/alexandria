package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.SelectorMenuSelection;
import io.intino.alexandria.ui.displays.components.selector.SelectorOption;
import io.intino.alexandria.ui.displays.notifiers.SelectorMenuNotifier;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SelectorMenu<DN extends SelectorMenuNotifier, B extends Box> extends AbstractSelectorMenu<DN, B> {
	private int selected;

	public SelectorMenu(B box) {
		super(box);
	}

	@Override
	public List<String> selection() {
		return Collections.singletonList(nameOf(selected));
	}

	@Override
	public void reset() {
		selected = -1;
		notifier.refreshSelected(new SelectorMenuSelection().option(-1));
	}

	public void select(String option) {
		int position = position(option);
		if (position == -1) return;
		this.select(position);
	}

	public void select(int option) {
		notifier.refreshSelected(new SelectorMenuSelection().option(option).ancestors(ancestors(option)));
		if (this.selected == option) return;
		this.selected = option;
		notifySelection();
	}

	private List<String> ancestors(int option) {
		return findOption(option).ancestors().stream().map(Block::label).filter(Objects::nonNull).collect(Collectors.toList());
	}

	private String nameOf(int option) {
		if (selected == -1) return null;
		SelectorOption child = findOption(option);
		return child != null ? child.name() : null;
	}

	private SelectorOption findOption(int option) {
		List<SelectorOption> options = options();
		return options.get(option);
	}

	private int position(String option) {
		List<SelectorOption> options = options();
		for (int i = 0; i< options.size(); i++) {
			SelectorOption selectorOption = options.get(i);
			if (selectorOption.name().equalsIgnoreCase(option) || selectorOption.id().equals(option))
				return i;
		}
		return -1;
	}
}