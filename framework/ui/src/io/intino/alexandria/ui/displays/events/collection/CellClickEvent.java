package io.intino.alexandria.ui.displays.events.collection;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

public class CellClickEvent extends Event {
	private final String column;
	private final String row;
	private final Object item;

	public CellClickEvent(Display<?, ?> sender, String column, String row, Object item) {
		super(sender);
		this.column = column;
		this.row = row;
		this.item = item;
	}

	public String column() {
		return column;
	}

	public String row() {
		return row;
	}

	@SuppressWarnings("unchecked")
	public <T> T item() {
		return (T)item;
	}
}
