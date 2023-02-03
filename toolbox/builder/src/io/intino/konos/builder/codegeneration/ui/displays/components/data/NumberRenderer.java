package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.DataComponents.Number;

public class NumberRenderer extends ComponentRenderer<Number> {

	public NumberRenderer(CompilationContext compilationContext, Number component, RendererWriter provider) {
		super(compilationContext, component, provider);
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
		if (editableNumber.helperText() != null) builder.add("helperText", editableNumber.helperText());
		if (editableNumber.shrink()) builder.add("shrink", true);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("number", "");
	}
}
