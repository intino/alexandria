package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;

import java.util.ArrayList;
import java.util.List;

public class SelectorMenu<B extends Box> extends AbstractSelectorMenu<B> implements Selector {
	private int selected;
	private List<SelectListener> onSelectListeners = new ArrayList<>();

	public SelectorMenu(B box) {
		super(box);
	}

	@Override
	public SelectorMenu onSelect(SelectListener selectListener) {
		this.onSelectListeners.add(selectListener);
		return this;
	}

	@Override
	public String selectedOption() {
		return nameOf(selected);
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
		notifySelection(option);
		notifier.refreshSelected(option);
	}

	private void notifySelection(int option) {
		SelectEvent event = new SelectEvent(this, nameOf(option), option);
		onSelectListeners.forEach(l -> l.accept(event));
	}

	private String nameOf(int option) {
		if (selected == -1) return null;
		Component child = child(option);
		return child != null ? child.id() : null;
	}

	private int position(String option) {
		List<Display> children = children();
		for (int i = 0; i< children.size(); i++)
			if (children.get(i).name().equalsIgnoreCase(option))
				return i;
		return -1;
	}

}