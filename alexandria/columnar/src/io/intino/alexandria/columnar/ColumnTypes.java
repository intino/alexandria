package io.intino.alexandria.columnar;

import java.util.HashMap;
import java.util.Map;

public class ColumnTypes {

	Map<java.lang.String, ColumnType> types = new HashMap<>();

	public ColumnTypes put(String key, ColumnType value) {
		types.put(key, value);
		return this;
	}

	public ColumnType getOrDefault(java.lang.String key, ColumnType defaultValue) {
		return types.getOrDefault(key, defaultValue);
	}

	public static abstract class ColumnType {
		public static class Date extends ColumnType {
			private final java.lang.String format;

			public Date(java.lang.String format) {
				this.format = format;
			}

			public java.lang.String format() {
				return format;
			}
		}

		public static class Nominal extends ColumnType {
			private final java.lang.String[] values;

			public Nominal(java.lang.String[] values) {
				this.values = values;
			}

			public java.lang.String[] values() {
				return values;
			}
		}

		public static class Numeric extends ColumnType {

		}

		public static class String extends ColumnType {

		}
	}
}
