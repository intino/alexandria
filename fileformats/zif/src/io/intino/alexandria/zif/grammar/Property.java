package io.intino.alexandria.zif.grammar;

import java.time.Instant;

public class Property {
	public final static String Identifier = "id/";
	public final static String Feature = "feature/";
	public final static String Credential = "credential/";
	public final static String Role = "role/";

	private final String name;
	private final String label;
	private final Type type;
	private final Cardinality cardinality;
	private final String grammar;

	public Property(String name, String label, Type type) {
		this(name, label, type, null);
	}

	public Property(String name, String label, Type type, String grammar) {
		this(name, label, type, grammar, Cardinality.single);
	}

	public Property(String name, String label, Type type, String grammar, Cardinality cardinality) {
		this.name = name;
		this.label = label;
		this.type = type;
		this.grammar = grammar;
		this.cardinality = cardinality;
	}

	public Property(String[] data) {
		this(data[0], data[1], Type.valueOf(data[2]), grammarOf(data), cardinalityOf(data));
	}

	public String name() {
		return name;
	}

	public String label() {
		return label;
	}

	public Type type() {
		return type;
	}

	public boolean isIdentifier() {
		return name.startsWith(Identifier);
	}

	public boolean isFeature() {
		return name.startsWith(Feature);
	}

	public boolean isCredential() {
		return name.startsWith(Credential);
	}

	public boolean isRole() {
		return name.startsWith(Role);
	}

	public String grammar() {
		return grammar;
	}

	public Cardinality cardinality() {
		return cardinality;
	}

	public enum Type {
		string, image
	}

	public enum Cardinality {
		single, multiple
	}

	@Override
	public String toString() {
		return name + "\t" + label + "\t" + type.name() + "\t" + (grammar != null ? grammar : "") + "\t" + (cardinality != null ? cardinality.name() : "");
	}

	private static String grammarOf(String[] data) {
		return data.length > 3 ? data[3] : null;
	}

	private static Cardinality cardinalityOf(String[] data) {
		return data.length > 4 ? Cardinality.valueOf(data[4]) : Cardinality.single;
	}

}
