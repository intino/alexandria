package io.intino.alexandria.ui.model.locations;

import io.intino.alexandria.ui.model.Geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

	public Point centroid() {
		double x = 0.;
		double y = 0.;
		List<Point> points = paths.stream().flatMap(Collection::stream).collect(Collectors.toList());
		int pointCount = points.size();
		for (int i = 0; i < pointCount - 1;i++){
			final Point point = points.get(i);
			x += point.latitude();
			y += point.longitude();
		}
		x = x/pointCount;
		y = y/pointCount;
		return new Point(x, y);
	}

	@Override
	public String toWkt() {
		StringBuilder result = new StringBuilder();
		for (int i=0; i<paths.size(); i++) result.append(pathToWkt(paths.get(i)));
		return String.format("POLYGON(%s)", String.join(",", result.toString()));
	}

	private String pathToWkt(List<Point> path) {
		return "(" + path.stream().map(p -> p.latitude() + " " + p.longitude()).collect(java.util.stream.Collectors.joining(",")) + ")";
	}
}
