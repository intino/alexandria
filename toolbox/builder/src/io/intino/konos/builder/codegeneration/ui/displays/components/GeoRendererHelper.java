package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.DataComponents;

public class GeoRendererHelper {

	public static void addCenter(CatalogComponents.Map.Center center, FrameBuilder builder) {
		if (center == null) return;
		addCenter(builder, center.latitude(), center.longitude());
	}

	public static void addCenter(DataComponents.Location.Center center, FrameBuilder builder) {
		if (center == null) return;
		addCenter(builder, center.latitude(), center.longitude());
	}

	public static void addZoom(CatalogComponents.Map.Zoom zoom, FrameBuilder builder) {
		if (zoom == null) return;
		addZoom(builder, zoom.min(), zoom.max(), zoom.defaultZoom());
	}

	public static void addZoom(DataComponents.Location.Zoom zoom, FrameBuilder builder) {
		if (zoom == null) return;
		addZoom(builder, zoom.min(), zoom.max(), zoom.defaultZoom());
	}

	private static void addCenter(FrameBuilder builder, double latitude, double longitude) {
		builder.add("centerLat", latitude);
		builder.add("centerLng", longitude);
	}

	private static void addZoom(FrameBuilder builder, int min, int max, int defaultZoom) {
		builder.add("zoomMin", min);
		builder.add("zoomMax", max);
		builder.add("zoomDefault", defaultZoom);
	}
}
