package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.BindingCollectionRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents.AbstractSlider;
import io.intino.konos.dsl.VisualizationComponents.Slider;
import io.intino.konos.dsl.VisualizationComponents.TemporalSlider;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class SliderRenderer extends BindingCollectionRenderer<AbstractSlider> {

	public SliderRenderer(CompilationContext context, AbstractSlider component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		builder.add("abstractslider");
		if (element.i$(conceptOf(TemporalSlider.class)))
			addBinding(builder, element.a$(TemporalSlider.class).collections());
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("abstractslider");
		if (element.arrangement() != null) result.add("arrangement", element.arrangement().name());
		if (element.isReadonly()) result.add("readonly", element.isReadonly());
		addValue(result);
		addRange(result);
		addAnimation(result);
		addOrdinals(result);
		addPosition(result);
		addStyle(result);
		return result;
	}

	private void addValue(FrameBuilder builder) {
		if (element.i$(conceptOf(Slider.class))) {
			builder.add("value", element.a$(Slider.class).value());
		} else if (element.i$(conceptOf(TemporalSlider.class))) {
			builder.add("value", element.a$(TemporalSlider.class).value());
		}
	}

	private void addRange(FrameBuilder builder) {
		if (element.i$(conceptOf(Slider.class))) {
			Slider.Range range = element.a$(Slider.class).range();
			builder.add("min", range.min());
			builder.add("max", range.max());
		} else if (element.i$(conceptOf(TemporalSlider.class))) {
			TemporalSlider.Range range = element.a$(TemporalSlider.class).range();
			builder.add("min", range.min() != null ? range.min().toEpochMilli() : 0);
			builder.add("max", range.max() != null ? range.max().toEpochMilli() : 0);
		}
	}

	private void addAnimation(FrameBuilder builder) {
		if (element.animation() == null) return;
		Slider.Animation animation = element.animation();
		builder.add("interval", animation.interval());
		builder.add("loop", animation.loop());
	}

	private void addOrdinals(FrameBuilder builder) {
		if (!element.i$(conceptOf(TemporalSlider.class))) return;
		TemporalSlider temporalSlider = element.a$(TemporalSlider.class);
		temporalSlider.scales().forEach(scale -> {
			FrameBuilder ordinal = new FrameBuilder("ordinalMethod").add("name", scale);
			builder.add("ordinal", ordinal);
		});
	}

	private void addPosition(FrameBuilder builder) {
		AbstractSlider.Position position = element.position();
		if (position == null) return;
		builder.add("position", position.name());
	}

	private void addStyle(FrameBuilder builder) {
		Slider.Style style = element.style();
		if (style == Slider.Style.Full) return;
		builder.add("style", style.name());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("slider", "");
	}
}
