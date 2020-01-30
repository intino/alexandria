package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.Task;

public class TaskRenderer extends OperationRenderer<Task> {

	public TaskRenderer(CompilationContext compilationContext, Task component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	protected void fill(FrameBuilder builder) {
		super.fill(builder);
		addMethod(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.isSwitches()) {
			result.add("switches");
			result.add("state", element.asSwitches().state().name());
		}
		addAddressableProperties(result);
		return result;
	}

	private void addMethod(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		builder.add("methods", addressedMethod());
	}

	private FrameBuilder addressedMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Task.class.getSimpleName()).add("addressable");
		result.add("name", nameOf(element));
		return result;
	}

	private void addAddressableProperties(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		Task.Addressable addressable = element.asAddressable();
		String path = addressable.addressableResource().path();
		builder.add("path", path);
	}

}
