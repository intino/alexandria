package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.ActionableComponents;
import io.intino.konos.model.InteractionComponents;

public class ExportRenderer extends ActionableRenderer {

	public ExportRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		ActionableComponents.Actionable.Export export = element.asExport();
		if (export.from() != null) properties.add("from", export.from().toEpochMilli());
		if (export.to() != null) properties.add("to", export.to().toEpochMilli());
		if (export.min() != null) properties.add("min", export.min().toEpochMilli());
		if (export.max() != null) properties.add("max", export.max().toEpochMilli());
		properties.add("rangeMin", export.rangeMin());
		properties.add("rangeMax", export.rangeMax());
		export.options().forEach(o -> properties.add("option", o));
		return properties;
	}

}
