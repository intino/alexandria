package io.intino.alexandria.ui.model.datasource.temporal;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.TimeRange;
import io.intino.alexandria.ui.model.datasource.BoundingBox;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.PlaceMark;

import java.util.List;

public abstract class TemporalMapDatasource<O> extends MapDatasource<O> {

	@Override
	public List<PlaceMark<O>> placeMarks(String condition, List<Filter> filters, BoundingBox boundingBox) {
		return placeMarks(null, condition, filters, boundingBox);
	}

	public abstract List<PlaceMark<O>> placeMarks(Timetag timetag, String condition, List<Filter> filters, BoundingBox boundingBox);

	@Override
	public long placeMarkCount(String condition, List<Filter> filters) {
		return placeMarkCount(null, condition, filters);
	}

	public abstract long placeMarkCount(Timetag timetag, String condition, List<Filter> filters);

	public abstract TimeRange range();

}
