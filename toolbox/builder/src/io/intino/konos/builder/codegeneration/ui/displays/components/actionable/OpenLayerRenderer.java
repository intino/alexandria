package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.Addressable;
import io.intino.konos.model.InteractionComponents;

public class OpenLayerRenderer extends ActionableRenderer {

	public OpenLayerRenderer(CompilationContext context, InteractionComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addAddressableProperties(result);
		if (element.asOpenLayer().transition() != null) result.add("transition", element.asOpenLayer().transition().name());
		return result;
	}

	@Override
	protected void fill(FrameBuilder builder) {
		super.fill(builder);
		addMethod(builder);
	}

	private void addMethod(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		builder.add("methods", addressedMethod());
	}

	private FrameBuilder addressedMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(InteractionComponents.Actionable.OpenLayer.class.getSimpleName()).add("addressable");
		result.add("name", nameOf(element));
		return result;
	}

	private void addAddressableProperties(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		Addressable addressable = element.asAddressable();
		builder.add("path", addressable.addressableResource() != null ? addressable.addressableResource().path() : "");
	}

}
