package io.intino.alexandria.ui.displays.components.geo;

import io.intino.alexandria.schemas.Geometry;
import io.intino.alexandria.schemas.PlaceMark;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.model.locations.Point;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
		return new Geometry().type(typeOf(location)).pointList(pointsOf(location));
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
		if (location.isPolyline()) return Geometry.Type.Polyline;
		else if (location.isPolygon()) return Geometry.Type.Polygon;
		return Geometry.Type.Point;
	}

	private static List<Geometry.Point> pointsOf(io.intino.alexandria.ui.model.Geometry location) {
		return location.points().stream().map(PlaceMarkBuilder::pointOf).collect(toList());
	}

	private static Geometry.Point pointOf(Point p) {
		return new Geometry.Point().lat(p.latitude()).lng(p.longitude());
	}
}
