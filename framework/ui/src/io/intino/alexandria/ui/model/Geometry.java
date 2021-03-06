package io.intino.alexandria.ui.model;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.model.locations.Point;
import io.intino.alexandria.ui.model.locations.Polygon;
import io.intino.alexandria.ui.model.locations.Polyline;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Geometry {
	public abstract String toWkt();

	public boolean isPoint() {
		return this instanceof Point;
	}

	public boolean isPolygon() {
		return this instanceof Polygon;
	}

	public boolean isPolyline() {
		return this instanceof Polyline;
	}

	public boolean contains(Point point) {
		try {
			org.locationtech.jts.geom.Geometry figureGeometry = new WKTReader().read(toWkt());
			org.locationtech.jts.geom.Geometry pointGeometry = new WKTReader().read(point.toWkt());
			return figureGeometry.contains(pointGeometry);
		} catch (ParseException e) {
			return false;
		}
	}

	public static Geometry fromWkt(String value) {
		try {
			org.locationtech.jts.geom.Geometry geometry = new WKTReader().read(value);
			Coordinate[] coordinates = geometry.getCoordinates();
			if (geometry instanceof org.locationtech.jts.geom.Point)
				return new Point(coordinates[0].x, coordinates[0].y);
			else if (geometry instanceof org.locationtech.jts.geom.Polygon) {
				Polygon polygon = new Polygon();
				polygon.add(Arrays.stream(coordinates).map(c -> new Point(c.x, c.y)).collect(Collectors.toList()));
				return polygon;
			}
			else if (geometry instanceof org.locationtech.jts.geom.LineString) {
				Polyline polyline = new Polyline();
				Arrays.stream(coordinates).forEach(c -> polyline.add(new Point(c.x, c.y)));
				return polyline;
			}
		} catch (ParseException e) {
			Logger.error(e);
		}
		return null;
	}
}
