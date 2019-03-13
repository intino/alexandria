package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.SelectorMenuSelection;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.components.selector.Selector;
import io.intino.alexandria.ui.displays.components.selector.SelectorOption;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SelectorMenu<B extends Box> extends AbstractSelectorMenu<B> implements Selector {
	private int selected;
	private List<SelectListener> onSelectListeners = new ArrayList<>();
	private List<Component> options = new ArrayList<>();

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
	public List<SelectorOption> options() {
		return findOptions();
	}

	@Override
	public void add(SelectorOption option) {
		((Component)option).properties().addClassName("option");
		super.add((Component)option);
	}

	public String selected() {
		return nameOf(this.selected);
	}

	public void select(String option) {
		int position = position(option);
		if (position == -1) return;
		this.select(position);
	}

	public void select(int option) {
		if (this.selected == option) return;
		this.selected = option;
		notifySelection(option);
		notifier.refreshSelected(new SelectorMenuSelection().option(option).ancestors(ancestors(option)));
	}

	private List<String> ancestors(int option) {
		return findOption(option).ancestors().stream().map(Block::label).filter(Objects::nonNull).collect(Collectors.toList());
	}

	private void notifySelection(int option) {
		SelectEvent event = new SelectEvent(this, nameOf(option), option);
		onSelectListeners.forEach(l -> l.accept(event));
	}

	private String nameOf(int option) {
		if (selected == -1) return null;
		SelectorOption child = findOption(option);
		return child != null ? child.id() : null;
	}

	private List<SelectorOption> findOptions() {
		List<SelectorOption> result = new ArrayList<>();
		List<Component> children = children(Component.class);
		children.forEach(c -> result.addAll(findOptions(c)));
		return result;
	}

	private List<SelectorOption> findOptions(Component child) {
		List<SelectorOption> result = new ArrayList<>();
		if (child instanceof SelectorOption) result.add((SelectorOption)child);
		List<Component> children = child.children(Component.class);
		children.forEach(c -> result.addAll(findOptions(c)));
		return result;
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