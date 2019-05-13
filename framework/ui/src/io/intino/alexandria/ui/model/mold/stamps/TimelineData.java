package io.intino.alexandria.ui.model.mold.stamps;

import java.util.ArrayList;
import java.util.List;

import static io.intino.alexandria.ui.model.mold.stamps.TimelineData.Serie.Type.Spline;

public class TimelineData extends ChartData {
	private List<String> categories = new ArrayList<>();
	private List<Serie> series = new ArrayList<>();

	public List<String> categories() {
		return this.categories;
	}

	public ChartData categories(List<String> categories) {
		this.categories = categories;
		return this;
	}

	public List<Serie> series() {
		return this.series;
	}

	public ChartData series(List<Serie> series) {
		this.series = series;
		return this;
	}

	public ChartData add(String category) {
		categories.add(category);
		return this;
	}

	public ChartData add(Serie serie) {
		series.add(serie);
		return this;
	}

	public static class Serie {
		private String name;
		private Type type = Spline;
		private String color;
		private List<Double> values = new ArrayList<>();

		public enum Type { Spline, Column }

		public String name() {
			return name;
		}

		public Serie name(String name) {
			this.name = name;
			return this;
		}

		public Type type() {
			return type;
		}

		public Serie type(Type type) {
			this.type = type;
			return this;
		}

		public String color() {
			return color;
		}

		public Serie color(String color) {
			this.color = color;
			return this;
		}

		public List<Double> values() {
			return this.values;
		}

		public Serie values(List<Double> values) {
			this.values = values;
			return this;
		}

		public Serie add(Double value) {
			this.values.add(value);
			return this;
		}
	}
}