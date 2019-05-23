package io.intino.alexandria.ui.model.datasource.placemarks;

import io.intino.alexandria.ui.model.datasource.PlaceMark;

import java.util.ArrayList;
import java.util.List;

public class Polyline<O> extends PlaceMark<O> {
	private List<Point> path = new ArrayList<>();

	public List<Point> path() {
		return path;
	}

	public Polyline path(List<Point> path) {
		this.path = path;
		return this;
	}
}
