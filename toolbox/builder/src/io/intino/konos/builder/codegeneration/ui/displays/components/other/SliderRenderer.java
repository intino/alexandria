package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.BindingCollectionRenderer;
import io.intino.konos.model.graph.OtherComponents.AbstractSlider;
import io.intino.konos.model.graph.OtherComponents.Slider;
import io.intino.konos.model.graph.OtherComponents.TemporalSlider;

public class SliderRenderer extends BindingCollectionRenderer<AbstractSlider> {

	public SliderRenderer(Settings settings, AbstractSlider component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		builder.add("abstractslider");
		if (element.i$(TemporalSlider.class)) addBinding(builder, element.a$(TemporalSlider.class).collections());
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("abstractslider");
		if (element.arrangement() != null) result.add("arrangement", element.arrangement().name());
		if (element.value() != -1) result.add("value", element.value());
		if (element.isReadonly()) result.add("readonly", element.isReadonly());
		addRange(result);
		addAnimation(result);
		addOrdinals(result);
		addStyle(result);
		return result;
	}

	private void addRange(FrameBuilder builder) {
		if (element.i$(Slider.class)) {
			Slider.Range range = element.a$(Slider.class).range();
			builder.add("min", range.min());
			builder.add("max", range.max());
		}
		else if (element.i$(TemporalSlider.class)) {
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
		if (!element.i$(TemporalSlider.class)) return;
		TemporalSlider temporalSlider = element.a$(TemporalSlider.class);
		temporalSlider.scales().forEach(scale -> {
			FrameBuilder ordinal = new FrameBuilder("ordinalMethod").add("name", scale);
			builder.add("ordinal", ordinal);
		});
	}

	private void addStyle(FrameBuilder builder) {
		AbstractSlider.Style style = element.style();
		if (style == AbstractSlider.Style.Full) return;
		builder.add("style", style.name());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("slider", "");
	}
}
