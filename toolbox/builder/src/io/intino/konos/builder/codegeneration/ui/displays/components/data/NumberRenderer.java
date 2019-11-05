package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.DataComponents.Number;

public class NumberRenderer extends ComponentRenderer<Number> {

	public NumberRenderer(Settings settings, Number component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("value", element.value());
		if (element.prefix() != null) result.add("prefix", element.prefix());
		if (element.suffix() != null) result.add("suffix", element.suffix());
		if (element.isReadonly()) result.add("readonly", element.isReadonly());
		if (element.isFocused()) result.add("focused", element.isFocused());
		result.add("decimals", element.countDecimals());
		result.add("style", element.style().name());
		result.add("expanded", element.expanded());
		addEditableProperties(result);
		return result;
	}

	private void addEditableProperties(FrameBuilder builder) {
		if (!element.isEditable()) return;
		Number.Editable editableNumber = element.asEditable();
		builder.add("min", editableNumber.min());
		builder.add("max", editableNumber.max());
		builder.add("step", editableNumber.step());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("number", "");
	}
}
