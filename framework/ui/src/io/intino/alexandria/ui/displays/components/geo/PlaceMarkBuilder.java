package io.intino.alexandria.ui.displays.components.geo;

import io.intino.alexandria.schemas.Geometry;
import io.intino.alexandria.schemas.Path;
import io.intino.alexandria.schemas.PlaceMark;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.model.locations.Point;
import io.intino.alexandria.ui.model.locations.Polygon;
import io.intino.alexandria.ui.model.locations.Polyline;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class PlaceMarkBuilder {

	public static <Item> List<PlaceMark> buildList(List<io.intino.alexandria.ui.model.PlaceMark<Item>> placeMarks, URL baseAssetUrl) {
		List<io.intino.alexandria.schemas.PlaceMark> result = new ArrayList<>();
		for (int i=0; i<placeMarks.size(); i++) result.add(build(placeMarks.get(i), i, baseAssetUrl));
		return result;
	}

	public static <Item> PlaceMark build(io.intino.alexandria.ui.model.PlaceMark<Item> placeMark, URL baseAssetUrl) {
		io.intino.alexandria.schemas.PlaceMark result = new PlaceMark().location(locationOf(placeMark)).label(placeMark.label());
		if (placeMark.icon() != null) result.icon(Asset.toResource(baseAssetUrl, placeMark.icon()).toUrl().toString());
		return result;
	}

	public static Geometry buildGeometry(io.intino.alexandria.ui.model.Geometry location) {
		Geometry result = new Geometry().type(typeOf(location));
		if (location != null) {
			fillPoint(location, result);
			fillPolygon(location, result);
			fillPolyline(location, result);
		}
		return result;
	}

	private static void fillPoint(io.intino.alexandria.ui.model.Geometry location, Geometry result) {
		if (!location.isPoint()) return;
		Point point = (Point)location;
		result.point(pointOf(point));
	}

	private static void fillPolyline(io.intino.alexandria.ui.model.Geometry location, Geometry result) {
		if (!location.isPolyline()) return;
		Polyline polyline = (Polyline)location;
		result.path(pathOf(polyline.path()));
	}

	private static void fillPolygon(io.intino.alexandria.ui.model.Geometry location, Geometry result) {
		if (!location.isPolygon()) return;
		Polygon polygon = (Polygon)location;
		result.paths(pathsOf(polygon.paths()));
	}

	private static List<io.intino.alexandria.schemas.Path> pathsOf(List<List<Point>> paths) {
		return paths.stream().map(PlaceMarkBuilder::pathOf).collect(toList());
	}

	private static io.intino.alexandria.schemas.Path pathOf(List<Point> path) {
		return new Path().pointList(path.stream().map(PlaceMarkBuilder::pointOf).collect(Collectors.toList()));
	}

	private static io.intino.alexandria.schemas.Point pointOf(Point point) {
		return new io.intino.alexandria.schemas.Point().lat(point.latitude()).lng(point.longitude());
	}

	private static <Item> PlaceMark build(io.intino.alexandria.ui.model.PlaceMark<Item> placeMark, long pos, URL baseAssetUrl) {
		PlaceMark result = build(placeMark, baseAssetUrl);
		result.pos(pos);
		return result;
	}

	private static <Item> Geometry locationOf(io.intino.alexandria.ui.model.PlaceMark<Item> placeMark) {
		return buildGeometry(placeMark.location());
	}

	private static Geometry.Type typeOf(io.intino.alexandria.ui.model.Geometry location) {
		if (location != null) {
			if (location.isPolyline()) return Geometry.Type.Polyline;
			else if (location.isPolygon()) return Geometry.Type.Polygon;
		}
		return Geometry.Type.Point;
	}

}
