package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.Addressable;
import io.intino.konos.model.InteractionComponents;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class ActionRenderer extends ActionableRenderer {

	public ActionRenderer(CompilationContext context, InteractionComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
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
		if (element.i$(conceptOf(InteractionComponents.Switch.class))) {
			result.add("switch");
			result.add("state", element.a$(InteractionComponents.Switch.class).state().name());
		}
		else if (element.i$(conceptOf(InteractionComponents.AbstractToggle.class))) {
			result.add("toggle");
			result.add("state", element.a$(InteractionComponents.AbstractToggle.class).state().name());
		}
		else if (element.i$(conceptOf(InteractionComponents.AbstractSplitButton.class))) {
			result.add("splitbutton");
			result.add("option", element.a$(InteractionComponents.AbstractSplitButton.class).options().toArray());
			result.add("default", element.a$(InteractionComponents.AbstractSplitButton.class).defaultOption());
			if (element.i$(conceptOf(InteractionComponents.IconSplitButton.class))) result.add("icon", element.a$(InteractionComponents.IconSplitButton.class).icon());
			else if (element.i$(conceptOf(InteractionComponents.MaterialIconSplitButton.class))) result.add("icon", element.a$(InteractionComponents.MaterialIconSplitButton.class).icon());
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
