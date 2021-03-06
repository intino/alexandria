package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;

import static io.intino.konos.model.graph.DataComponents.Date;

public class DateRenderer extends ComponentRenderer<Date> {

	public DateRenderer(CompilationContext compilationContext, Date component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("pattern", element.pattern());
		if (element.value() != null) result.add("value", element.value().toEpochMilli());
		if (element.mode() != Date.Mode.None) result.add("mode", element.mode().name().toLowerCase());
		addEditableProperties(result);
		return result;
	}

	private void addEditableProperties(FrameBuilder builder) {
		if (!element.isEditable()) return;
		Date.Editable editableDate = element.asEditable();
		if (editableDate.min() != null) builder.add("min", editableDate.min().toEpochMilli());
		if (editableDate.max() != null) builder.add("max", editableDate.max().toEpochMilli());
		if (editableDate.timePicker()) builder.add("timePicker", editableDate.timePicker());
		if (editableDate.mask() != null) builder.add("mask", editableDate.mask());
		if (editableDate.allowEmpty()) builder.add("allowEmpty", editableDate.allowEmpty());
		if (editableDate.views() != null) editableDate.views().forEach(view -> builder.add("view", view.name()));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("header", "");
	}
}
