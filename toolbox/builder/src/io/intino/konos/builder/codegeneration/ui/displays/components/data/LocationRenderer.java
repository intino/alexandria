package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.GeoRendererHelper;
import io.intino.konos.model.graph.DataComponents.Location;

public class LocationRenderer extends ComponentRenderer<Location> {

	public LocationRenderer(Settings settings, Location component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.icon() != null && !element.icon().isEmpty()) result.add("icon", resourceMethodFrame("icon", element.icon()));
		if (element.value() != null && !element.value().isEmpty()) result.add("value", element.value());
		GeoRendererHelper.addCenter(element.center(), result);
		GeoRendererHelper.addZoom(element.zoom(), result);
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz);
	}
}
