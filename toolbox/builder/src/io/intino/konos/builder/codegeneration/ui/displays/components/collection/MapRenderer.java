package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.CollectionRenderer;
import io.intino.konos.model.graph.CatalogComponents.Map;

public class MapRenderer extends CollectionRenderer<Map> {

	public MapRenderer(Settings settings, Map component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addFacets(result);
		addCenter(result);
		addZoom(result);
		return result;
	}

	private void addFacets(FrameBuilder builder) {
		if (element.isCluster()) builder.add("type", "Cluster");
		if (element.isKml()) {
			builder.add("type", "Kml");
			builder.add("layer", resourceMethodFrame("kmlLayer", element.asKml().layer()));
		}
		if (element.isHeatmap()) builder.add("type", "Heatmap");
	}

	private void addCenter(FrameBuilder builder) {
		Map.Center center = element.center();
		if (center == null) return;
		builder.add("centerLat", center.latitude());
		builder.add("centerLng", center.longitude());
	}

	private void addZoom(FrameBuilder builder) {
		Map.Zoom zoom = element.zoom();
		if (zoom == null) return;
		builder.add("zoomMin", zoom.min());
		builder.add("zoomMax", zoom.max());
		builder.add("zoomDefault", zoom.defaultZoom());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz);
	}
}
