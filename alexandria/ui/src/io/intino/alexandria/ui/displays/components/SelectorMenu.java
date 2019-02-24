package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;

import java.util.List;

public class SelectorMenu<B extends Box> extends AbstractSelectorMenu<B> implements Selector {
	private int selected;
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
		option.properties().addClassName("option");
		super.add(option);
	}

	public String selected() {
		return nameOf(this.selected);
	}

	public void select(String option) {
		this.select(position(option));
	}

	public void select(int option) {
		if (this.selected == option) return;
		this.selected = option;
		if (onSelect != null) onSelect.accept(new SelectEvent(this, nameOf(option), option));
		notifier.refreshSelected(option);
	}

	private String nameOf(int option) {
		Component child = child(option);
		return child != null ? child.name() : null;
	}

	private int position(String option) {
		List<Display> children = children();
		for (int i = 0; i< children.size(); i++)
			if (children.get(i).name().equalsIgnoreCase(option))
				return i;
		return -1;
	}

}