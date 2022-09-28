package io.intino.alexandria.ui.displays.components.collection.behaviors;

import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.loaders.GridItemLoader;
import io.intino.alexandria.ui.displays.components.collection.loaders.PageItemLoader;
import io.intino.alexandria.ui.model.datasource.GridDatasource;
import io.intino.alexandria.ui.model.datasource.grid.GridGroupBy;
import io.intino.alexandria.ui.model.datasource.grid.GridItem;

public class GridCollectionBehavior<Item> extends PageCollectionBehavior<GridDatasource<Item>, Item> {

	public GridCollectionBehavior(Collection<?, ?> collection) {
		super(collection);
	}

	@Override
	public CollectionBehavior setup(GridDatasource<Item> source, int pageSize) {
		if (source == null) return this;
		computeUpdate(e -> {
			itemLoader = new GridItemLoader<Item>(source, pageSize);
			page(0);
		}, false);
		return this;
	}

	public GridGroupBy groupBy() {
		return ((GridItemLoader)itemLoader).groupBy();
	}

	public GridCollectionBehavior groupBy(GridGroupBy groupBy) {
		computeUpdate(e -> ((GridItemLoader)itemLoader).groupBy(groupBy));
		return this;
	}

}