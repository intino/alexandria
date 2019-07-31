package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents;

public class TaskRenderer extends OperationRenderer<OperationComponents.Task> {

	public TaskRenderer(Settings settings, OperationComponents.Task component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.isSwitches()) {
			result.add("switches");
			result.add("state", element.asSwitches().state().name());
		}
		return result;
	}

}
