package io.intino.alexandria.ui.model.locations;

import io.intino.alexandria.ui.model.Geometry;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Polyline extends Geometry {
	private List<Point> path = new ArrayList<>();

	public List<Point> path() {
		return path;
	}

	public Polyline path(List<Point> path) {
		this.path = path;
		return this;
	}

	public Polyline add(Point point) {
		this.path.add(point);
		return this;
	}

	@Override
	public String toWkt() {
		String result = pathToWkt(path);
		return String.format("LINESTRING(%s)", String.join(",", result));
	}

	private String pathToWkt(List<Point> path) {
		return path.stream().map(p -> p.latitude() + " " + p.longitude()).collect(java.util.stream.Collectors.joining(","));
	}
}
