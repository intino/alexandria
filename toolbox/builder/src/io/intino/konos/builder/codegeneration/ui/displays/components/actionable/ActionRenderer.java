package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.Addressable;
import io.intino.konos.model.graph.InteractionComponents;

public class ActionRenderer extends ActionableRenderer {

	public ActionRenderer(CompilationContext context, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(context, component, provider, target);
	}

	@Override
	protected void fill(FrameBuilder builder) {
		super.fill(builder);
		if (element.asAction().context() == InteractionComponents.Actionable.Action.Context.Selection) builder.add("selection");
		addMethod(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.i$(InteractionComponents.Switch.class)) {
			result.add("switch");
			result.add("state", element.a$(InteractionComponents.Switch.class).state().name());
		}
		else if (element.i$(InteractionComponents.AbstractToggle.class)) {
			result.add("toggle");
			result.add("state", element.a$(InteractionComponents.AbstractToggle.class).state().name());
		}
		else if (element.i$(InteractionComponents.SplitButton.class)) {
			result.add("splitbutton");
			result.add("option", element.a$(InteractionComponents.SplitButton.class).options().toArray());
			result.add("default", element.a$(InteractionComponents.SplitButton.class).defaultOption());
		}
		addAddressableProperties(result);
		return result;
	}

	private void addMethod(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		builder.add("methods", addressedMethod());
	}

	private FrameBuilder addressedMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(InteractionComponents.Actionable.Action.class.getSimpleName()).add("addressable");
		result.add("name", nameOf(element));
		return result;
	}

	private void addAddressableProperties(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		Addressable addressable = element.asAddressable();
		builder.add("path", addressable.addressableResource() != null ? addressable.addressableResource().path() : "");
	}

}
