package io.intino.alexandria.ui.model.datasource.locations;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Polygon extends Location {
	private List<Point> paths = new ArrayList<>();

	public List<Point> paths() {
		return paths;
	}

	public Polygon paths(List<Point> paths) {
		this.paths = paths;
		return this;
	}

	@Override
	public String toWkt() {
		return String.format("POLYGON((%s))", String.join(",", paths.stream().map(Point::toWkt).collect(toList())));
	}

}
