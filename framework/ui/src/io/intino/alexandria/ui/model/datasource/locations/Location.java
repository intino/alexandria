package io.intino.alexandria.ui.model.datasource.locations;

import java.util.List;

public abstract class Location {
	public abstract String toWkt();
	public abstract List<Point> points();

	public boolean isPoint() {
		return this instanceof Point;
	}

	public boolean isPolygon() {
		return this instanceof Polygon;
	}

	public boolean isPolyline() {
		return this instanceof Polyline;
	}
}
