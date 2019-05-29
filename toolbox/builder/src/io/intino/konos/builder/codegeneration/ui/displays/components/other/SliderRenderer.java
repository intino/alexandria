package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.BindingCollectionRenderer;
import io.intino.konos.model.graph.OtherComponents.Slider;
import io.intino.konos.model.graph.temporal.othercomponents.TemporalSlider;

public class SliderRenderer extends BindingCollectionRenderer<Slider> {

	public SliderRenderer(Settings settings, Slider component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder result = super.frameBuilder();
		if (element.isTemporal()) addBinding(result, element.asTemporal().collections());
		return result;
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.arrangement() != null) result.add("arrangement", element.arrangement().name());
		if (element.value() != -1) result.add("value", element.value());
		addRange(result);
		addAnimation(result);
		addTemporal(result);
		return result;
	}

	private void addRange(FrameBuilder builder) {
		if (element.range() == null) return;
		Slider.Range range = element.range();
		builder.add("min", range.min());
		builder.add("max", range.max());
	}

	private void addAnimation(FrameBuilder builder) {
		if (element.animation() == null) return;
		Slider.Animation animation = element.animation();
		builder.add("interval", animation.interval());
		builder.add("loop", animation.loop());
	}

	private void addTemporal(FrameBuilder builder) {
		if (!element.isTemporal()) return;
		TemporalSlider temporalSlider = element.asTemporal();
		temporalSlider.scales().forEach(scale -> {
			FrameBuilder ordinal = new FrameBuilder("ordinalMethod").add("name", scale);
			builder.add("ordinal", ordinal);
		});
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("slider", "");
	}
}
