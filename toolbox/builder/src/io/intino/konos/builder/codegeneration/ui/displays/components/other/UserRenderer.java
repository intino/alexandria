package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.OperationComponents.Operation;
import io.intino.konos.model.graph.OtherComponents.User;

public class UserRenderer<O extends Operation> extends ComponentRenderer<User> {

	public UserRenderer(CompilationContext compilationContext, User component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.mode() != null) properties.add("mode", element.mode().name());
		return properties;
	}

}
