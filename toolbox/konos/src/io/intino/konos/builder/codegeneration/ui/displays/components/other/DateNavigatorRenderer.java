package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents;
import io.intino.konos.dsl.VisualizationComponents.DateNavigator;

import java.util.List;
import java.util.stream.Collectors;

public class DateNavigatorRenderer extends ComponentRenderer<DateNavigator> {

	public DateNavigatorRenderer(CompilationContext compilationContext, DateNavigator component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.temporalComponents().stream().filter(c -> c.i$(VisualizationComponents.Timeline.class)).collect(Collectors.toList()));
		addBinding(builder, element.temporalComponents().stream().filter(c -> c.i$(VisualizationComponents.Reel.class)).collect(Collectors.toList()));
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
