package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.SelectorMenuSelection;
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
	public void didMount() {
		super.didMount();
		select(selected);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> selection() {
		return Collections.singletonList(nameOf(selected));
	}

	@Override
	public void reset() {
		selected = -1;
		notifier.refreshSelected(new SelectorMenuSelection().option(-1));
	}

	public void selection(String option) {
		int position = position(option);
		if (position == -1) return;
		selection(position);
	}

	public void selection(int option) {
		notifier.refreshSelected(new SelectorMenuSelection().option(option).ancestors(ancestors(option)));
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

	private List<String> ancestors(int option) {
		return findOption(option).ancestors().stream().map(Block::label).filter(Objects::nonNull).collect(Collectors.toList());
	}

}