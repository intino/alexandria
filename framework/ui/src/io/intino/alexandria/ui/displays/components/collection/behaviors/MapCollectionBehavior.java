package io.intino.alexandria.ui.displays.components.collection.behaviors;

import io.intino.alexandria.ui.displays.components.Map;
import io.intino.alexandria.ui.displays.components.collection.loaders.MapItemLoader;
import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.datasource.PlaceMark;

import java.util.List;

public class MapCollectionBehavior<Item> extends CollectionBehavior<MapDatasource<Item>, Item, MapItemLoader<Item>> {

	public MapCollectionBehavior(Map collection) {
		super(collection);
	}

	@Override
	public MapCollectionBehavior setup(MapDatasource<Item> source) {
		if (source == null) return this;
		itemLoader = new MapItemLoader<>(source);
		update();
		return this;
	}

	@Override
	void update() {
		Map collection = collection();
		collection.refreshPlaceMarks(placeMarks());
	}

	public List<PlaceMark<Item>> placeMarks() {
		return itemLoader().placeMarks();
	}

	public void showPlaceMark(long pos) {
		Map collection = collection();
		collection.clear();
		collection.add(placeMarks().get((int)pos));
	}

}
