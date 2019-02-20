package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;

public class Menu<B extends Box> extends AbstractMenu<B> {
	private String selected;
	private SelectListener onSelect = null;

	public Menu(B box) {
        super(box);
    }

    public void binding(Selectable component) {
		this.onSelect(e -> component.select(e));
	}

	public Menu onSelect(SelectListener selectListener) {
		this.onSelect = selectListener;
		return this;
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