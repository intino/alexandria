package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.ChildComponents.Date;
import io.intino.konos.model.graph.editable.childcomponents.EditableDate;
import org.siani.itrules.model.Frame;

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
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("header", "");
	}
}
