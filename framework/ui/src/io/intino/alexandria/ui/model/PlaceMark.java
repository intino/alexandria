package io.intino.alexandria.ui.model;

import java.net.URL;

public class PlaceMark<O> {
	private O item;
	private String label;
	private URL icon = null;
	private Geometry position;

	public O item() {
		return item;
	}

	public PlaceMark item(O item) {
		this.item = item;
		return this;
	}

	public String label() {
		return label;
	}

	public PlaceMark label(String label) {
		this.label = label;
		return this;
	}

	public URL icon() {
		return icon;
	}

	public PlaceMark icon(URL icon) {
		this.icon = icon;
		return this;
	}

	public Geometry location() {
		return position;
	}

	public PlaceMark location(Geometry location) {
		this.position = location;
		return this;
	}

	public static PlaceMark build(String label, String location) {
		PlaceMark result = new PlaceMark();
		result.label(label);
		result.location(location != null ? Geometry.fromWkt(location) : null);
		return result;
	}

}
