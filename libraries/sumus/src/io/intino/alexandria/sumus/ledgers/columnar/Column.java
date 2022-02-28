package io.intino.alexandria.sumus.ledgers.columnar;

import io.intino.alexandria.sumus.Attribute;
import io.intino.alexandria.sumus.Index;
import io.intino.alexandria.sumus.Lookup;
import io.intino.alexandria.sumus.dimensions.Category;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class Column implements Lookup {
	public static final Column Null = new Column(Attribute.Null);
	public final Attribute attribute;
	public final List<Object> values;
	private Object min;
	private Object max;

	public Column(Attribute attribute, String... values) {
		this.attribute = attribute;
		this.values = map(values, function(values));
	}

	private Comparator<Object> comparatorOf(Attribute.Type type) {
		switch (type) {
			case integer:
			case date:
				return Comparator.comparingLong(a -> (long) a);
			case number:
				return Comparator.comparingDouble(a -> (double) a);
		}
		return (a,b)->0;
	}

	private Function<String, Object> function(String[] values) {
		switch (attribute.type) {
			case category: return get(categoriesIn(values));
			case integer: return Long::parseLong;
			case number: return Double::parseDouble;
			case date: return this::toEpoch;
		}
		return s->s;
	}

	private Function<String, Object> get(Map<String, Category> categories) {
		return categories::get;
	}

	private List<Object> map(String[] values, Function<String, Object> function) {
		return Arrays.stream(values)
				.map(Column::clean)
				.map(v-> v.isEmpty() ? null : catching(() -> function.apply(v)))
				.collect(toList());
	}

	private static String clean(String v) {
		return v != null ? v.trim() : "";
	}

	private static Map<String, Category> categoriesIn(String[] values) {
		Map<String, Category> categories = new HashMap<>();
		Arrays.stream(values)
				.map(Column::clean)
				.filter(v -> !v.isEmpty())
				.filter(v -> !categories.containsKey(v))
				.forEach(v -> categories.put(v, category(v, indexOf(categories))));
		return categories;
	}

	private void setRange() {
		Comparator<Object> comparator = comparatorOf(attribute.type);
		for (Object value : values) {
			if (value == null) continue;
			if (min == null || comparator.compare(value,min) < 0) min = value;
			if (max == null || comparator.compare(value,max) > 0) max = value;
		}
	}

	private static int indexOf(Map<String, Category> categories) {
		return categories.size() + 1;
	}

	private static Category category(String label, int idx) {
		return new Category(label.isEmpty() ? 0 : idx, label.isEmpty() ? "NA" : label);
	}

	@Override
	public String name() {
		return attribute.name;
	}

	@Override
	public Attribute.Type type() {
		return attribute.type;
	}

	public int size() {
		return values.size();
	}

	public Object value(int idx) {
		return values.get(idx);
	}

	public boolean hasNA() {
		return values.stream().anyMatch(Objects::isNull);
	}

	@Override
	public List<Object> uniques() {
		return values.stream()
				.distinct()
				.filter(Objects::nonNull)
				.collect(toList());
	}

	@Override
	public Object min() {
		if (min == null) setRange();
		return min;
	}

	@Override
	public Object max() {
		if (max == null) setRange();
		return max;
	}


	@Override
	public Index index(Predicate<Object> predicate) {
		return idx -> predicate.test(value(idx));
	}

	private Object catching(Callable<Object> function) {
		try {
			return function.call();
		}
		catch (Exception e) {
			return null;
		}
	}

	private static final DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	private Object toEpoch(String value) {
		if (value == null) return null;
		return LocalDate.parse(trim(value), DateFormatter).toEpochDay();
	}

	private static String trim(String value) {
		return value.replaceAll("[-/: .]", "");
	}


}
