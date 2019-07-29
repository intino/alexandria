package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.Datasource;

import java.util.List;

public abstract class PageDatasource<O> extends Datasource<O> {

	public abstract List<O> items(int start, int count, String condition, List<Filter> filters, List<String> sortings);
	public abstract long itemCount(String condition, List<Filter> filters);

}
