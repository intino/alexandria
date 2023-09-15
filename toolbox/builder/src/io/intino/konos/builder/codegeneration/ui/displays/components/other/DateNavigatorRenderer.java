package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents;
import io.intino.konos.model.VisualizationComponents;
import io.intino.konos.model.VisualizationComponents.DateNavigator;
import io.intino.konos.model.VisualizationComponents.Reel;

import java.util.List;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class DateNavigatorRenderer extends ComponentRenderer<DateNavigator> {

	public DateNavigatorRenderer(CompilationContext compilationContext, DateNavigator component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.temporalComponents());
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		element.scales().forEach(s -> result.add("scale", scaleFrameOf(s)));
		if (element.selected() != null) result.add("selected", element.selected().toEpochMilli());
		addRange(result);
		return result;
	}

	private FrameBuilder scaleFrameOf(DateNavigator.Scales scale) {
		return new FrameBuilder("scale").add("value", scale.name());
	}

	private void addRange(FrameBuilder result) {
		if (element.range() == null) return;
		DateNavigator.Range range = element.range();
		result.add("from", range.from().toEpochMilli());
		result.add("to", range.to().toEpochMilli());
	}

	protected void addBinding(FrameBuilder builder, List<VisualizationComponents.TemporalComponent> components) {
		if (components.size() <= 0) return;
		FrameBuilder result = new FrameBuilder("binding", type()).add("name", nameOf(element));
		components.forEach(c -> result.add("component", nameOf(c)));
		builder.add("binding", result);
	}

}
