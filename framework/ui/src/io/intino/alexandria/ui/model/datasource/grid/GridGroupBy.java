package io.intino.alexandria.ui.model.datasource.grid;

public class GridGroupBy {
	private GridColumn column;
	private String group;
	private int groupIndex;
	private String mode;

	public GridColumn column() {
		return column;
	}

	public GridGroupBy column(GridColumn column) {
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

	public int groupIndex() {
		return groupIndex;
	}

	public GridGroupBy groupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
		return this;
	}

	public String mode() {
		return mode;
	}

	public GridGroupBy mode(String mode) {
		this.mode = mode;
		return this;
	}
}
