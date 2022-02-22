package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.filters.GroupFilter;
import io.intino.alexandria.ui.model.datasource.filters.RangeFilter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class CollectionAddressResolver {

	public static final String Condition = "c";
	public static final String Filters = "f";
	public static final String Sortings = "s";

	public static String queryString(io.intino.alexandria.ui.displays.components.Collection<?, ?> collection, String grouping, java.util.List<String> groups) {
		if (collection == null) return null;
		return serialize(parameters(collection, grouping, groups));
	}

	private static java.util.Map<String, String> parameters(io.intino.alexandria.ui.displays.components.Collection<?, ?> collection, String grouping, java.util.List<String> groups) {
		java.util.Map<String, String> result = new LinkedHashMap<>();
		if (collection.condition() != null) result.put(Condition, collection.condition());
		List<String> filters = filters(collection.filters(), grouping, groups);
		if (filters.size() > 0) result.put(Filters, serialize(filters));
		if (collection.sortings().size() > 0) result.put(Sortings, String.join(",", collection.sortings()));
		return result;
	}

	private static String serialize(List<String> filters) {
		return String.join(";", filters);
	}

	private static String serialize(java.util.Map<String, String> parameters) {
		return parameters.entrySet().stream().map(p -> p.getKey() + "=" + URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8)).collect(Collectors.joining("&"));
	}

	private static String serialize(Filter filter) {
		return String.join(",", values(filter));
	}

	private static List<String> filters(java.util.List<Filter> filterList, String grouping, List<String> groups) {
		Map<String, String> result = filterList.stream().collect(toMap(Filter::grouping, CollectionAddressResolver::serialize));
		if (grouping != null) result.put(grouping, String.join(",", groups));
		return result.entrySet().stream().filter(e -> !e.getValue().isEmpty()).map(f -> f.getKey() + "#" + f.getValue()).collect(Collectors.toList());
	}

	private static Set<String> values(Filter filter) {
		if (filter instanceof GroupFilter) return ((GroupFilter)filter).groups();
		if (filter instanceof RangeFilter) return Set.of(new Timetag(((RangeFilter)filter).from(), Scale.Minute).value(), new Timetag(((RangeFilter)filter).to(), Scale.Minute).value());
		return Collections.emptySet();
	}

}
