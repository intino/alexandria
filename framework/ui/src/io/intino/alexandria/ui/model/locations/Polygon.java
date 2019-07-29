package io.intino.alexandria.ui.model.locations;

import io.intino.alexandria.ui.model.Geometry;

import java.util.ArrayList;
import java.util.List;

public class Polygon extends Geometry {
	private List<List<Point>> paths = new ArrayList<>();

	public List<List<Point>> paths() {
		return paths;
	}

	public Polygon paths(List<List<Point>> paths) {
		this.paths = paths;
		return this;
	}

	public Polygon add(List<Point> points) {
		this.paths.add(points);
		return this;
	}

	@Override
	public String toWkt() {
		StringBuilder result = new StringBuilder();
		for (int i=0; i<paths.size(); i++) result.append(pathToWkt(paths.get(i)));
		return String.format("POLYGON(%s)", String.join(",", result.toString()));
	}

	private String pathToWkt(List<Point> path) {
		return path.stream().map(Point::toWkt).collect(java.util.stream.Collectors.joining(","));
	}
}
