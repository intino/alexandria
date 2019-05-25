package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.Timetag;

import java.util.List;

public abstract class TemporalMapDatasource<O> extends TemporalDatasource<O> {

	public abstract List<PlaceMark<O>> placeMarks(Timetag timetag, String condition, List<Filter> filters);

}
