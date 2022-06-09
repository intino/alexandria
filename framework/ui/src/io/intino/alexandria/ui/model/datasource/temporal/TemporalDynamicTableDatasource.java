package io.intino.alexandria.ui.model.datasource.temporal;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.TimeRange;
import io.intino.alexandria.ui.model.datasource.DynamicTableDatasource;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.dynamictable.Row;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.util.List;

public abstract class TemporalDynamicTableDatasource<O> extends DynamicTableDatasource<O> {

	public List<Section> sections(String dimension, String drill, String condition, List<Filter> filters) {
		return sections(null, dimension, drill, condition, filters);
	}

	@Override
	public List<O> items(int start, int count, Section section, String row, String condition, List<Filter> filters, List<String> sortings) {
		return items(null, start, count, section, row, condition, filters, sortings);
	}

	@Override
	public long itemCount(Section section, String row, String condition, List<Filter> filters) {
		return itemCount(null, section, row, condition, filters);
	}

	public abstract List<Section> sections(Timetag timetag, String dimension, String drill, String condition, List<Filter> filters);
	public abstract List<O> items(Timetag timetag, int start, int count, Section section, String row, String condition, List<Filter> filters, List<String> sortings);
	public abstract long itemCount(Timetag timetag, Section section, String row, String condition, List<Filter> filters);

	public abstract TimeRange range();

}
