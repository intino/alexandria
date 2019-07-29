package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.PlaceMark;

import java.util.List;

public abstract class MapDatasource<O> extends Datasource<O> {

	public abstract List<PlaceMark<O>> placeMarks(String condition, List<Filter> filters, BoundingBox boundingBox);

	public abstract long placeMarkCount(String condition, List<Filter> filters);
}
