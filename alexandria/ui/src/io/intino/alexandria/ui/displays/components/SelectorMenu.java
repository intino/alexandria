package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;

import java.util.List;

public class SelectorMenu<B extends Box> extends AbstractSelectorMenu<B> implements Selector {
	private String selected;
	private SelectListener onSelect = null;

	public SelectorMenu(B box) {
		super(box);
	}

	@Override
	public SelectorMenu onSelect(SelectListener selectListener) {
		this.onSelect = selectListener;
		return this;
	}

	@Override
	public List<Component> options() {
		return children(Component.class);
	}

	@Override
	public void add(Component option) {
		addAndPersonify(option);
	}

	public String selected() {
		return this.selected;
	}

	public void select(String option) {
		if (this.selected != null && this.selected.equals(option)) return;
		this.selected = option;
		if (onSelect != null) onSelect.accept(new SelectEvent(this, option));
		notifier.refreshSelected(option);
	}

}