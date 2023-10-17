package io.intino.alexandria.ui.model;

import java.net.URL;

public class PlaceMark<O> {
	private O item;
	private String label;
	private URL icon = null;
	private int weight = 1;
	private Geometry position;

	public O item() {
		return item;
	}

	public PlaceMark<O> item(O item) {
		this.item = item;
		return this;
	}

	public String label() {
		return label;
	}

	public PlaceMark<O> label(String label) {
		this.label = label;
		return this;
	}

	public URL icon() {
		return icon;
	}

	public PlaceMark<O> icon(URL icon) {
		this.icon = icon;
		return this;
	}

	public Geometry location() {
		return position;
	}

	public PlaceMark<O> location(Geometry location) {
		this.position = location;
		return this;
	}

	public int weight() {
		return weight;
	}

	public PlaceMark<O> weight(int weight) {
		this.weight = weight;
		return this;
	}

	public static PlaceMark<?> build(String label, String location) {
		PlaceMark<?> result = new PlaceMark<>();
		result.label(label);
		result.location(location != null ? Geometry.fromWkt(location) : null);
		return result;
	}

}
