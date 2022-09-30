package io.intino.alexandria.ui.model.datasource.grid;

import java.util.Arrays;
import java.util.List;

import static io.intino.alexandria.ui.model.datasource.grid.GridColumn.Type.Number;
import static io.intino.alexandria.ui.model.datasource.grid.GridColumn.Type.*;

public class GridColumnMode {
	private String name;
	private List<GridColumn.Type> acceptedTypes;

	public GridColumnMode(String name) {
		this.name = name;
		this.acceptedTypes = Arrays.asList(GridColumn.Type.values());
	}

	public GridColumnMode(String name, GridColumn.Type... acceptedTypes) {
		this.name = name;
		this.acceptedTypes = Arrays.asList(acceptedTypes);
	}

	public String name() {
		return name;
	}

	public GridColumnMode name(String name) {
		this.name = name;
		return this;
	}

	public List<GridColumn.Type> acceptedTypes() {
		return acceptedTypes;
	}

	public GridColumnMode acceptedTypes(List<GridColumn.Type> acceptedTypes) {
		this.acceptedTypes = acceptedTypes;
		return this;
	}
}
