package io.intino.alexandria.ui.displays.components.chart;

public class DataframeColumn {
	public String name;
	public Type type = Type.Double;

	public enum Type {
		Double, Integer, String
	}

	public String name() {
		return name;
	}

	public DataframeColumn name(String name) {
		this.name = name;
		return this;
	}

	public Type type() {
		return type;
	}

	public DataframeColumn type(Type type) {
		this.type = type;
		return this;
	}

}
