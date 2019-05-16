package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.Export;

public class ExportRenderer extends OperationRenderer<Export> {

	public ExportRenderer(Settings settings, Export component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.from() != null) properties.add("from", element.from().toEpochMilli());
		if (element.to() != null) properties.add("to", element.to().toEpochMilli());
		if (element.min() != null) properties.add("min", element.min().toEpochMilli());
		if (element.max() != null) properties.add("max", element.max().toEpochMilli());
		properties.add("rangeMin", element.rangeMin());
		properties.add("rangeMax", element.rangeMax());
		element.options().forEach(o -> properties.add("option", o));
		return properties;
	}

}
