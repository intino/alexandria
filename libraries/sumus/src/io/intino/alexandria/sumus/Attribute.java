package io.intino.alexandria.sumus;

import io.intino.alexandria.sumus.parser.AttributeDefinition;
import io.intino.alexandria.sumus.parser.DimensionDefinition;

import java.util.Objects;

import static io.intino.alexandria.sumus.Attribute.Type.label;

public class Attribute {
	public static Attribute Null = new Attribute("null", label);

	public final String name;
	public final Type type;
	public final DimensionDefinition[] dimensions;

	public Attribute(String name, Type type, DimensionDefinition... dimensions) {
		this.name = name;
		this.type = type;
		this.dimensions = dimensions;
	}

	public Attribute(AttributeDefinition definition) {
		this(definition.name, definition.type, definition.dimensions.toArray(DimensionDefinition[]::new));
	}

	public boolean isNumeric() {
		return type.isNumeric();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		return this == o || equals((Attribute) o);
	}

	private boolean equals(Attribute attribute) {
		return Objects.equals(name, attribute.name) && type == attribute.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type);
	}

	@Override
	public String toString() {
		return name;
	}

	public enum Type {
		label, number, integer, category, date;

		public boolean isNumeric() {
			return this == number || this == integer;
		}

		public boolean isOrdinal() {
			return isNumeric() ||  this == date;
		}

	}
}
