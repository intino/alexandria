package io.intino.alexandria.sumus.parser;

import io.intino.alexandria.sumus.Attribute;

import java.util.ArrayList;
import java.util.List;

public class AttributeDefinition {
	public final String name;
	public Attribute.Type type;
	public List<DimensionDefinition> dimensions;

	public AttributeDefinition(String name, Attribute.Type type) {
		this.name = name;
		this.type = type;
		this.dimensions = new ArrayList<>();
	}

	public AttributeDefinition(String name) {
		this(name, Attribute.Type.label);
	}

	@Override
	public String toString() {
		return name + ':' + type;
	}

	public void type(Attribute.Type type) {
		this.type = type;
	}

	public void dimension(String token) {
		this.dimensions.add(new DimensionDefinition(nextName(), type, token));
	}

	private String nextName() {
		return String.valueOf((char) ('1' + dimensions.size()));
	}
}
