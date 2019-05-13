package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.DataComponents.Number;
import io.intino.konos.model.graph.editable.datacomponents.EditableNumber;
import org.siani.itrules.model.Frame;

public class NumberRenderer extends ComponentRenderer<Number> {

	public NumberRenderer(Settings settings, Number component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addSlot("value", element.value());
		if (element.prefix() != null) result.addSlot("prefix", element.prefix());
		if (element.suffix() != null) result.addSlot("suffix", element.suffix());
		addEditableProperties(result);
		return result;
	}

	private void addEditableProperties(Frame result) {
		if (!element.isEditable()) return;
		EditableNumber editableNumber = element.asEditable();
		result.addSlot("min", editableNumber.min());
		result.addSlot("max", editableNumber.max());
		result.addSlot("step", editableNumber.step());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("number", "");
	}
}
