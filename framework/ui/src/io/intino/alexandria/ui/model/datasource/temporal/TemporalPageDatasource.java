package io.intino.alexandria.ui.model.datasource.temporal;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

import java.util.List;

public abstract class TemporalPageDatasource<O> extends PageDatasource<O> {

	@Override
	public List<O> items(int start, int count, String condition, List<Filter> filters, List<String> sortings) {
		return items(null, start, count, condition, filters, sortings);
	}

	public abstract List<O> items(Timetag timetag, int start, int count, String condition, List<Filter> filters, List<String> sortings);

	@Override
	public long itemCount(String condition, List<Filter> filters) {
		return itemCount(null, condition, filters);
	}

	public abstract long itemCount(Timetag timetag, String condition, List<Filter> filters);

}
