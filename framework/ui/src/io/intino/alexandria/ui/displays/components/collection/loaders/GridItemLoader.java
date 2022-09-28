package io.intino.alexandria.ui.displays.components.collection.loaders;

import io.intino.alexandria.ui.model.datasource.GridDatasource;
import io.intino.alexandria.ui.model.datasource.grid.GridGroupBy;
import io.intino.alexandria.ui.model.datasource.grid.GridItem;

import java.util.ArrayList;
import java.util.List;

public class GridItemLoader<Item> extends PageItemLoader<GridDatasource<Item>, Item> {
	private GridGroupBy groupBy;

	public GridItemLoader(GridDatasource<Item> source, int pageSize) {
		super(source, pageSize);
	}

	public GridGroupBy groupBy() {
		return groupBy;
	}

	public GridItemLoader<Item> groupBy(GridGroupBy groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	@Override
	protected List<Item> items(int start, int pageSize) {
		ArrayList<String> sortingList = new ArrayList<>(this.sortings);
		return source.items(start, pageSize, condition, filters, sortingList, groupBy);
	}

}