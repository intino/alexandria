package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.OtherComponents.Selector;

public class SelectorRenderer extends ComponentRenderer<Selector> {

	public SelectorRenderer(Settings settings, Selector component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("multipleSelection", element.multipleSelection() ? "true" : "false");
		if (element.isReadonly()) result.add("readonly", element.isReadonly());
		if (element.isFocused()) result.add("readonly", element.isFocused());
		addComboBoxProperties(result);
		return result;
	}

	private void addComboBoxProperties(FrameBuilder builder) {
		if (!element.isComboBox()) return;
		String placeholder = element.asComboBox().placeholder();
		if (placeholder == null || placeholder.isEmpty()) return;
		builder.add("placeholder", placeholder);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("selector", "");
	}
}
