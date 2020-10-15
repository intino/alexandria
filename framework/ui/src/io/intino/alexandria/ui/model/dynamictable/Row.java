package io.intino.alexandria.ui.model.dynamictable;

import java.util.ArrayList;
import java.util.List;

public class Row {
	private String label;
	private List<Column> columns = new ArrayList<>();

	public Row(String label) {
		this.label = label;
	}

	public String label() {
		return label;
	}

	public Row label(String label) {
		this.label = label;
		return this;
	}

	public Column column(String name) {
		return columns.stream().filter(c -> c.name().equals(name)).findFirst().orElse(null);
	}

	public List<Column> columns() {
		return columns;
	}

	public Row add(String column, double value) {
		add(new Column(column, value));
		return this;
	}

	public Row add(Column column) {
		columns.add(column);
		return this;
	}
}
