package io.intino.alexandria.sumus.parser;

import io.intino.alexandria.sumus.Attribute;
import io.intino.alexandria.sumus.Classifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class DimensionDefinition {
	private final String name;
	private final Attribute.Type type;
	private final Classifier classifier;

	public DimensionDefinition(String name, Attribute.Type type, String options) {
		this.name = name;
		this.type = type;
		this.classifier = type.isNumeric() ? numericalClassifier(asLongs(options)) : categoricalClassifier(asCategories(options));
	}

	private Classifier categoricalClassifier(String[] options) {
		return new Classifier() {
			@Override
			public List<String> categories() {
				//TODO when category definition include options
				return null;
			}

			@Override
			public Predicate<Object> predicateOf(String category) {
				return null;
			}

			@Override
			public String toString() {
				return Arrays.toString(options);
			}
		};
	}

	public String name() {
		return name;
	}

	public Classifier classifier() {
		return classifier;
	}

	private Classifier numericalClassifier(Long[] longs) {
		List<String> categories = categoriesOf(longs);
		Map<String, Predicate<Object>> predicates = IntStream
				.range(0, categories.size())
				.boxed()
				.collect(toMap(categories::get, i -> predicate(longs[i], longs[i + 1])));
		return classifier(categories, predicates);
	}

	private List<String> categoriesOf(Long[] longs) {
		return IntStream.range(0, longs.length - 1)
				.mapToObj(i -> clean("[" + longs[i] + "-" + longs[i + 1] + "]"))
				.collect(toList());
	}

	private Long[] asLongs(String options) {
		return Arrays.stream(options.split(","))
				.map(DimensionDefinition::parse)
				.toArray(Long[]::new);
	}

	private String[] asCategories(String options) {
		return Arrays.stream(options.split(","))
				.map(String::trim)
				.toArray(String[]::new);
	}

	private static String clean(String value) {
		return value.replace("null", "");
	}

	private Classifier classifier(List<String> categories, Map<String, Predicate<Object>> predicates) {
		return new Classifier() {
			@Override
			public List<String> categories() {
				return categories;
			}

			@Override
			public Predicate<Object> predicateOf(String category) {
				return predicates.getOrDefault(category, s -> false);
			}
		};
	}

	private Predicate<Object> predicate(Long a, Long b) {
		return type == Attribute.Type.integer ?
				v -> !isNull(v) && (isNull(a) || (long) v >= a) && (isNull(b) || (long) v < b) :
				v -> !isNull(v) && (isNull(a) || ((double) v >= a)) && (isNull(b) || ((double) v < b));
	}

	private static boolean isNull(Object value) {
		return value == null;
	}

	private static Long parse(String s) {
		try {
			return Long.parseLong(s.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return name + classifier.toString().replace(" ","");
	}
}