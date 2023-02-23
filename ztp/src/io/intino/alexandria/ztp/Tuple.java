package io.intino.alexandria.ztp;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class Tuple {

	public static final String TUPLE_SEPARATOR = "\t";

	private final String[] fields;

	public Tuple(String text) {
		this(text.split(TUPLE_SEPARATOR, -1));
	}

	public Tuple(String[] fields) {
		this.fields = fields;
	}

	public String get(int index) {
		return fields[index];
	}

	public int size() {
		return fields.length;
	}

	public List<String> fields() {
		return new AbstractList<>() {
			@Override
			public String get(int index) {
				return fields[index];
			}

			@Override
			public int size() {
				return fields.length;
			}
		};
	}

	@Override
	public String toString() {
		return String.join("\t", fields);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tuple tuple = (Tuple) o;
		return Arrays.equals(fields, tuple.fields);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(fields);
	}
}
