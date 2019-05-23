package io.intino.alexandria.ui.model.datasource.placemarks;

import io.intino.alexandria.ui.model.datasource.PlaceMark;

import java.util.ArrayList;
import java.util.List;

public class Polygon<O> extends PlaceMark<O> {
	private List<Point> paths = new ArrayList<>();

	public List<Point> paths() {
		return paths;
	}

	public Polygon paths(List<Point> paths) {
		this.paths = paths;
		return this;
	}
}
