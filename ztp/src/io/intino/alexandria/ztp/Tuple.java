package io.intino.alexandria.ztp;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class Tuple {

	public static final String TUPLE_SEPARATOR = "\t";
	public static final int MIN_SIZE = 5;

	private final String[] fields;

	public Tuple(String text) {
		this(text.split(TUPLE_SEPARATOR, -1));
	}

	public Tuple(String[] fields) {
		if(fields.length < MIN_SIZE) throw new IllegalArgumentException("A tuple must have at least " + MIN_SIZE + " fields");
		this.fields = fields;
	}

	public String subject() {
		return fields[0];
	}

	public String predicate() {
		return fields[1];
	}

	public String value() {
		return fields[2];
	}

	public String get(int index) {
		return fields[index];
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
