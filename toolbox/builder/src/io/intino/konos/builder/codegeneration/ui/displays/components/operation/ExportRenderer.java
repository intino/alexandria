package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.Export;
import org.siani.itrules.model.Frame;

public class ExportRenderer extends OperationRenderer<Export> {

	public ExportRenderer(Settings settings, Export component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame properties = super.properties();
		if (element.from() != null) properties.addSlot("from", element.from().toEpochMilli());
		if (element.to() != null) properties.addSlot("to", element.to().toEpochMilli());
		if (element.min() != null) properties.addSlot("min", element.min().toEpochMilli());
		if (element.max() != null) properties.addSlot("max", element.max().toEpochMilli());
		properties.addSlot("rangeMin", element.rangeMin());
		properties.addSlot("rangeMax", element.rangeMax());
		element.options().forEach(o -> properties.addSlot("option", o));
		return properties;
	}

}
