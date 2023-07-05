package io.intino.alexandria.ui.model.datasource.grid;

import java.util.ArrayList;
import java.util.List;

public class GridItem {
	private boolean selectable = true;
	private final List<GridValue> values = new ArrayList<>();

	public GridItem selectable(boolean value) {
		this.selectable = value;
		return this;
	}

	public boolean selectable() {
		return selectable;
	}

	public List<GridValue> values() {
		return values;
	}

	public GridItem add(GridValue value) {
		this.values.add(value);
		return this;
	}

	public GridItem add(Object value) {
		return add(new GridValue(value));
	}

}
