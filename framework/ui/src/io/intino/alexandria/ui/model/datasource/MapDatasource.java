package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.Datasource;

import java.util.List;

public abstract class MapDatasource<O> extends Datasource<O> {

	public abstract List<PlaceMark<O>> placeMarks(String condition, List<Filter> filters);

}
