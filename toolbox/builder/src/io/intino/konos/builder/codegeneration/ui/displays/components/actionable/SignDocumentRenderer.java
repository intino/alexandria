package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.InteractionComponents;

public class SignDocumentRenderer extends ActionableRenderer {

	public SignDocumentRenderer(CompilationContext context, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(context, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		InteractionComponents.Actionable.SignDocument signDocument = element.asSignDocument();
		if (signDocument.document() != null) properties.add("document", fixResourceValue(signDocument.document()));
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
