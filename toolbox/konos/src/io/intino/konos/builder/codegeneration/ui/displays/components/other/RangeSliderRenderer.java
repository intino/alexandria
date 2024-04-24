package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.BindingCollectionRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents.RangeSlider;

public class RangeSliderRenderer extends BindingCollectionRenderer<RangeSlider> {

	public RangeSliderRenderer(CompilationContext context, RangeSlider component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("rangeslider");
		if (element.arrangement() != null) result.add("arrangement", element.arrangement().name());
		if (element.isReadonly()) result.add("readonly", element.isReadonly());
		addValue(result);
		addRange(result);
		addAnimation(result);
		addPosition(result);
		addStyle(result);
		return result;
	}

	private void addValue(FrameBuilder builder) {
		builder.add("from", element.from());
		builder.add("to", element.to());
		if (element.minimumDistance() != -1) builder.add("minimumDistance", element.minimumDistance());
	}

	private void addRange(FrameBuilder builder) {
		RangeSlider.Range range = element.a$(RangeSlider.class).range();
		builder.add("min", range.min());
		builder.add("max", range.max());
	}

	private void addAnimation(FrameBuilder builder) {
		if (element.animation() == null) return;
		RangeSlider.Animation animation = element.animation();
		builder.add("interval", animation.interval());
		builder.add("loop", animation.loop());
	}

	private void addPosition(FrameBuilder builder) {
		RangeSlider.Position position = element.position();
		if (position == null) return;
		builder.add("position", position.name());
	}

	private void addStyle(FrameBuilder builder) {
		RangeSlider.Style style = element.style();
		if (style == RangeSlider.Style.Full) return;
		builder.add("style", style.name());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("rangeslider", "");
	}
}
