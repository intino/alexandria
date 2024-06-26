package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.CollectionRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.GeoRendererHelper;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.CatalogComponents.Map;

import java.util.stream.Collectors;

public class MapRenderer extends CollectionRenderer<Map> {

	public MapRenderer(CompilationContext compilationContext, Map component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.icon() != null && !element.icon().isEmpty())
			result.add("icon", resourceMethodFrame("icon", element.icon()));
		addFacets(result);
		GeoRendererHelper.addCenter(element.center(), result);
		GeoRendererHelper.addZoom(element.zoom(), result);
		result.add("controls", element.controls() != null ? element.controls().stream().map(c -> c.name().toLowerCase()).collect(Collectors.joining(",")) : "");
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

	@Override
	protected String className(Class clazz) {
		return super.className(clazz);
	}
}
