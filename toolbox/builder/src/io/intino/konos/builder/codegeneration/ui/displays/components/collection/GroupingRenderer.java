package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.CatalogComponents.Grouping;

public class GroupingRenderer extends BindingCollectionRenderer<Grouping> {

	public GroupingRenderer(Settings settings, Grouping component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.collections());
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		addComboBoxProperties(properties);
		return properties;
	}

	private void addComboBoxProperties(FrameBuilder builder) {
		if (!element.isComboBox()) return;
		String placeholder = element.asComboBox().placeholder();
		if (placeholder == null || placeholder.isEmpty()) return;
		builder.add("placeholder", placeholder);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("grouping", "");
	}
}
