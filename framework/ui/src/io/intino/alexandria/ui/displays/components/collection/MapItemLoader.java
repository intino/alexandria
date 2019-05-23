package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.datasource.PlaceMark;

import java.util.List;

public class MapItemLoader<Item> extends ItemLoader<MapDatasource<Item>, Item> {

	public MapItemLoader(MapDatasource source, int pageSize) {
		super(source, pageSize);
	}

	public List<PlaceMark<Item>> placeMarks() {
		return source.placeMarks(condition, filters);
	}
}