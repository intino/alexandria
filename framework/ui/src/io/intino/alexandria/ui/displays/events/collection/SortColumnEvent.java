package io.intino.alexandria.ui.displays.events.collection;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.model.datasource.grid.GridColumn;

import java.util.Arrays;

public class SortColumnEvent extends Event {
	private final GridColumn<?> column;
	private final Mode mode;

	public enum Mode {
		None, Ascendant, Descendant;

		public static Mode from(String mode) {
			String lowerMode = mode.toLowerCase();
			return Arrays.stream(values()).filter(m -> m.name().equalsIgnoreCase(mode) || m.name().toLowerCase().startsWith(lowerMode)).findFirst().orElse(null);
		}
	}

	public SortColumnEvent(Display sender, GridColumn<?> column, Mode mode) {
		super(sender);
		this.column = column;
		this.mode = mode;
	}

	@SuppressWarnings("unchecked")
	public <O> GridColumn<O> column() {
		return (GridColumn<O>) column;
	}

	public Mode mode() {
		return mode;
	}

}
