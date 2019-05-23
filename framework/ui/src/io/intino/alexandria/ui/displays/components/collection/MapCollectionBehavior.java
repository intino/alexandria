package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.datasource.PlaceMark;

import java.util.List;

public class MapCollectionBehavior<Item> extends CollectionBehavior<MapDatasource<Item>, Item> {

	public MapCollectionBehavior(Collection collection) {
		super(collection);
	}

	@Override
	public void setup(MapDatasource<Item> source, int pageSize) {
		if (source == null) return;
		itemLoader = new MapItemLoader<>(source, pageSize);
		page(0);
	}

	public List<PlaceMark<Item>> placeMarks() {
		MapItemLoader<Item> loader = itemLoader();
		return loader.placeMarks();
	}

}
