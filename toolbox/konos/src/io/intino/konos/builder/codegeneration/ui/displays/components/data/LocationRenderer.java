package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.GeoRendererHelper;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.DataComponents.Location;

import java.util.List;
import java.util.stream.Collectors;

public class LocationRenderer extends ComponentRenderer<Location> {

	public LocationRenderer(CompilationContext compilationContext, Location component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.icon() != null && !element.icon().isEmpty())
			result.add("icon", resourceMethodFrame("icon", element.icon()));
		if (element.value() != null && !element.value().isEmpty()) result.add("value", element.value());
		addModes(result);
		GeoRendererHelper.addCenter(element.center(), result);
		GeoRendererHelper.addZoom(element.zoom(), result);
		result.add("controls", element.controls() != null ? element.controls().stream().map(c -> c.name().toLowerCase()).collect(Collectors.joining(",")) : "");
		return result;
	}

	private void addModes(FrameBuilder result) {
		if (!element.isEditable()) return;
		List<Location.Editable.Modes> modes = element.asEditable().modes();
		modes.forEach(m -> result.add("mode", m.name()));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz);
	}
}
