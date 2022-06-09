package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.util.List;

public abstract class DynamicTableDatasource<O> extends Datasource<O> {

	public abstract String name();
	public abstract List<Section> sections(String dimension, String drill, String condition, List<Filter> filters);
	public abstract List<O> items(int start, int count, Section section, String row, String condition, List<Filter> filters, List<String> sortings);
	public abstract long itemCount(Section section, String row, String condition, List<Filter> filters);

}
