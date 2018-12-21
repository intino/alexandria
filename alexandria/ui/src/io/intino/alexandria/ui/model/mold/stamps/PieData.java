package io.intino.alexandria.ui.model.mold.stamps;

import java.util.ArrayList;
import java.util.List;

public class PieData extends ChartData {
	private List<Value> values = new ArrayList<>();

	public List<Value> values() {
		return values;
	}

	public PieData values(List<Value> value) {
		this.values = value;
		return this;
	}

	public PieData add(String name, double value) {
		this.values.add(new Value().name(name).value(value));
		return this;
	}

	public class Value {
		private String name;
		private String color;
		private Double value;

		public String name() {
			return name;
		}

		public Value name(String name) {
			this.name = name;
			return this;
		}

		public String color() {
			return color;
		}

		public Value color(String color) {
			this.color = color;
			return this;
		}

		public Double value() {
			return value;
		}

		public Value value(Double value) {
			this.value = value;
			return this;
		}
	}
}