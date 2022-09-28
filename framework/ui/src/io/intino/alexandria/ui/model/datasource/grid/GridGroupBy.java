package io.intino.alexandria.ui.model.datasource.grid;

public class GridGroupBy {
	private String column;
	private String group;

	public String column() {
		return column;
	}

	public GridGroupBy column(String column) {
		this.column = column;
		return this;
	}

	public String group() {
		return group;
	}

	public GridGroupBy group(String group) {
		this.group = group;
		return this;
	}
}
