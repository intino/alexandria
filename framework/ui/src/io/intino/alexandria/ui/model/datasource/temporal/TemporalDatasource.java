package io.intino.alexandria.ui.model.datasource.temporal;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.TimeRange;
import io.intino.alexandria.ui.model.datasource.Filter;

import java.util.List;

public abstract class TemporalDatasource<O> extends Datasource<O> {

	public abstract long itemCount(Timetag timetag, String condition, List<Filter> filters);
	public abstract List<O> items(Timetag timetag, int start, int count, String condition, List<Filter> filters, List<String> sortings);
	public abstract TimeRange range();

}
