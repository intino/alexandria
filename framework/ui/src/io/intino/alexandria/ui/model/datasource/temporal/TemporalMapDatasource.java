package io.intino.alexandria.ui.model.datasource.temporal;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.PlaceMark;

import java.util.List;

public abstract class TemporalMapDatasource<O> extends Datasource<O> {

	public abstract List<PlaceMark<O>> placeMarks(Timetag timetag, String condition, List<Filter> filters);

}
