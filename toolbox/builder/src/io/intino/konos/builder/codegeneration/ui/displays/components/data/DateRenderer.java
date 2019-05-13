package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.editable.datacomponents.EditableDate;
import org.siani.itrules.model.Frame;

import static io.intino.konos.model.graph.DataComponents.Date;

public class DateRenderer extends ComponentRenderer<Date> {

	public DateRenderer(Settings settings, Date component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addSlot("pattern", element.pattern());
		if (element.value() != null) result.addSlot("value", element.value().toEpochMilli());
		if (element.mode() != Date.Mode.None) result.addSlot("mode", element.mode().name().toLowerCase());
		addEditableProperties(result);
		return result;
	}

	private void addEditableProperties(Frame result) {
		if (!element.isEditable()) return;
		EditableDate editableDate = element.asEditable();
		if (editableDate.min() != null) result.addSlot("min", editableDate.min().toEpochMilli());
		if (editableDate.max() != null) result.addSlot("max", editableDate.max().toEpochMilli());
		if (editableDate.timePicker()) result.addSlot("timePicker", editableDate.timePicker());
		if (editableDate.mask() != null) result.addSlot("mask", editableDate.mask());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("header", "");
	}
}
