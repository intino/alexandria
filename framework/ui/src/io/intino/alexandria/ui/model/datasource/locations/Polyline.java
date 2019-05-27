package io.intino.alexandria.ui.model.datasource.locations;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Polyline extends Location {
	private List<Point> path = new ArrayList<>();

	public List<Point> path() {
		return path;
	}

	public Polyline path(List<Point> path) {
		this.path = path;
		return this;
	}

	public void add(Point point) {
		this.path.add(point);
	}

	@Override
	public String toWkt() {
		return String.format("LINESTRING(%s)", String.join(",", path.stream().map(Point::toWkt).collect(toList())));
	}

	@Override
	public List<Point> points() {
		return path;
	}
}
