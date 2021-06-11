package io.intino.alexandria.ui.model.dynamictable;

import java.util.ArrayList;
import java.util.List;

public class Row {
	private String label;
	private String description;
	private List<Cell> cells = new ArrayList<>();

	public Row(String label) {
		this.label = label;
	}

	public Row(String label, String description) {
		this(label);
		this.description = description;
	}

	public String label() {
		return label;
	}

	public Row label(String label) {
		this.label = label;
		return this;
	}

	public String description() {
		return description;
	}

	public Row description(String description) {
		this.description = description;
		return this;
	}

	public Cell cell(String name) {
		return cells.stream().filter(c -> c.name().equals(name)).findFirst().orElse(null);
	}

	public List<Cell> cells() {
		return cells;
	}

	public Row add(String column, double value) {
		add(new Cell(column, value));
		return this;
	}

	public Row add(Cell cell) {
		cells.add(cell);
		return this;
	}
}
