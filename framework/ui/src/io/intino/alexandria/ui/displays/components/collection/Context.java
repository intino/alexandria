package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.datasource.Filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Context {
	String condition;
	List<Filter> filters = new ArrayList<>();
	HashSet<String> sortings = new HashSet<>();
	Timetag timetag = null;
}
