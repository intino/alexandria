package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.datasource.grid.GridColumn;
import io.intino.alexandria.ui.model.datasource.grid.GridColumnMode;
import io.intino.alexandria.ui.model.datasource.grid.GridGroupBy;

import java.util.List;

public abstract class GridDatasource<O> extends PageDatasource<O> {

	public abstract String name();

	public abstract List<O> items(int start, int count, String condition, List<Filter> filters, List<String> sortings, GridGroupBy groupBy);

	public abstract List<String> columnGroups(GridColumn<O> column, String mode, String condition, List<Filter> filters);

	public abstract List<GridColumnMode> columnModes();

	@Override
	public List<O> items(int start, int count, String condition, List<Filter> filters, List<String> sortings) {
		return items(start, count, condition, filters, sortings, null);
	}

}
