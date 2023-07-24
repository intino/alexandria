package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.ActionableComponents;
import io.intino.konos.model.Addressable;
import io.intino.konos.model.InteractionComponents;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class ActionRenderer extends ActionableRenderer {

	public ActionRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	protected void fill(FrameBuilder builder) {
		super.fill(builder);
		if (element.asAction().context() == ActionableComponents.Actionable.Action.Context.Selection) builder.add("selection");
		addMethod(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.i$(conceptOf(ActionableComponents.Switch.class))) {
			result.add("switch");
			result.add("state", element.a$(ActionableComponents.Switch.class).state().name());
		}
		else if (element.i$(conceptOf(ActionableComponents.AbstractToggle.class))) {
			result.add("toggle");
			result.add("state", element.a$(ActionableComponents.AbstractToggle.class).state().name());
		}
		else if (element.i$(conceptOf(ActionableComponents.AbstractSplitButton.class))) {
			result.add("splitbutton");
			result.add("option", element.a$(ActionableComponents.AbstractSplitButton.class).options().toArray());
			result.add("default", element.a$(ActionableComponents.AbstractSplitButton.class).defaultOption());
			if (element.i$(conceptOf(ActionableComponents.IconSplitButton.class))) result.add("icon", element.a$(ActionableComponents.IconSplitButton.class).icon());
			else if (element.i$(conceptOf(ActionableComponents.MaterialIconSplitButton.class))) result.add("icon", element.a$(ActionableComponents.MaterialIconSplitButton.class).icon());
		}
		addAddressableProperties(result);
		return result;
	}

	private void addMethod(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		builder.add("methods", addressedMethod());
	}

	private FrameBuilder addressedMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(ActionableComponents.Actionable.Action.class.getSimpleName()).add("addressable");
		result.add("name", nameOf(element));
		return result;
	}

	private void addAddressableProperties(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		Addressable addressable = element.asAddressable();
		builder.add("path", addressable.addressableResource() != null ? addressable.addressableResource().path() : "");
	}

}
