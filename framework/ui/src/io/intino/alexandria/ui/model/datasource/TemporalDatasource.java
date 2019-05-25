package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.Datasource;

import java.util.List;

import static java.util.Collections.emptyList;

public abstract class TemporalDatasource<O> extends Datasource<O> {

	public long itemCount(Timetag timetag) {
		return itemCount(timetag, null, emptyList());
	}
	public abstract long itemCount(Timetag timetag, String condition, List<Filter> filters);
	public abstract List<O> items(Timetag timetag, int start, int count, String condition, List<Filter> filters, List<String> sortings);

}
