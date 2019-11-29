package io.intino.alexandria.ui.displays.components.collection.loaders;

import io.intino.alexandria.ui.model.datasource.BoundingBox;
import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.PlaceMark;
import io.intino.alexandria.ui.model.datasource.temporal.TemporalMapDatasource;

import java.util.List;

import static java.util.Collections.emptyList;

public class MapItemLoader<Item> extends ItemLoader<MapDatasource<Item>, Item> {
	private BoundingBox boundingBox;

	public MapItemLoader(MapDatasource source) {
		super(source);
	}

	public MapItemLoader boundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
		return this;
	}

	public List<PlaceMark<Item>> placeMarks() {
		if (source instanceof TemporalMapDatasource)
			return timetag != null ? ((TemporalMapDatasource)source).placeMarks(timetag, condition, filters, boundingBox) : emptyList();
		return source.placeMarks(condition, filters, boundingBox);
	}

}