package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.CloseDrawer;

public class CloseDrawerRenderer extends OperationRenderer<CloseDrawer> {

	public CloseDrawerRenderer(CompilationContext compilationContext, CloseDrawer component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.drawer() != null) properties.add("drawer", nameOf(element.drawer()));
		return properties;
	}

	protected void addBinding(FrameBuilder builder) {
		if (element.drawer() == null) return;
		FrameBuilder result = new FrameBuilder("binding", "closedrawer").add("name", nameOf(element));
		result.add("drawer", nameOf(element.drawer()));
		builder.add("binding", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
