package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.InteractionComponents;

import java.util.UUID;

public class ActionableRenderer extends ComponentRenderer<InteractionComponents.Actionable> {

	public ActionableRenderer(CompilationContext context, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(context, component, provider, target);
	}

	@Override
	protected void fill(FrameBuilder builder) {
		super.fill(builder);
		addSignedMethods(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("actionable");
		properties.add("title", element.title());
		properties.add("target", element.target().name().toLowerCase());
		properties.add("mode", mode());
		properties.add("actionableMode", modeFrame());
		properties.add("size", element.size().name());
		addHighlight(properties);
		if (element.isReadonly() || isSelectionContext()) properties.add("readonly", "true");
		if (element.isAffirmed()) properties.add("affirmed", element.asAffirmed().affirmText());
		if (element.isSigned()) {
			InteractionComponents.Actionable.Signed signed = element.asSigned();
			properties.add("signText", signed.signText());
			if (signed.reasonText() != null) properties.add("reasonText", signed.reasonText());
		}
		if (element.i$(InteractionComponents.MaterialIconButton.class)) properties.add("icon", element.a$(InteractionComponents.MaterialIconButton.class).icon());
		else if (element.i$(InteractionComponents.MaterialIconToggle.class)) properties.add("icon", element.a$(InteractionComponents.MaterialIconToggle.class).icon());
		return properties;
	}

	private void addHighlight(FrameBuilder properties) {
		if (!element.i$(InteractionComponents.AbstractButton.class)) return;
		InteractionComponents.AbstractButton button = element.a$(InteractionComponents.AbstractButton.class);
		if (button.highlight() == InteractionComponents.AbstractButton.Highlight.None) return;
		properties.add("highlighted", button.highlight().name());
	}

	private boolean isSelectionContext() {
		return element.isAction() && element.asAction().context() == InteractionComponents.Actionable.Action.Context.Selection ||
			   element.isDownload() && element.asDownload().context() == InteractionComponents.Actionable.Download.Context.Selection;
	}

	private FrameBuilder modeFrame() {
		FrameBuilder result = new FrameBuilder("actionableMode", mode());
		result.add("mode", mode());
		if (element.i$(InteractionComponents.IconButton.class)) result.add("icon", element.a$(InteractionComponents.IconButton.class).icon());
		else if (element.i$(InteractionComponents.MaterialIconButton.class)) result.add("icon", element.a$(InteractionComponents.MaterialIconButton.class).icon());
		else if (element.i$(InteractionComponents.IconToggle.class)) result.add("icon", element.a$(InteractionComponents.IconToggle.class).icon());
		else if (element.i$(InteractionComponents.MaterialIconToggle.class)) result.add("icon", element.a$(InteractionComponents.MaterialIconToggle.class).icon());
		else if (element.i$(InteractionComponents.SplitButton.class)) {
			result.add("option", element.a$(InteractionComponents.SplitButton.class).options().toArray());
			result.add("default", element.a$(InteractionComponents.SplitButton.class).defaultOption());
		}
		return result;
	}

	private String mode() {
		if (element.i$(InteractionComponents.IconButton.class)) return InteractionComponents.IconButton.class.getSimpleName();
		else if (element.i$(InteractionComponents.MaterialIconButton.class)) return InteractionComponents.MaterialIconButton.class.getSimpleName();
		else if (element.i$(InteractionComponents.Button.class)) return InteractionComponents.Button.class.getSimpleName();
		else if (element.i$(InteractionComponents.IconToggle.class)) return InteractionComponents.IconToggle.class.getSimpleName();
		else if (element.i$(InteractionComponents.MaterialIconToggle.class)) return InteractionComponents.MaterialIconToggle.class.getSimpleName();
		else if (element.i$(InteractionComponents.Toggle.class)) return InteractionComponents.Toggle.class.getSimpleName();
		else if (element.i$(InteractionComponents.SplitButton.class)) return InteractionComponents.SplitButton.class.getSimpleName();
		else if (element.i$(InteractionComponents.AvatarIconButton.class)) return InteractionComponents.AvatarIconButton.class.getSimpleName();
		return InteractionComponents.Link.class.getSimpleName();
	}

	private void addSignedMethods(FrameBuilder builder) {
		if (!element.isSigned()) return;
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(InteractionComponents.Actionable.Signed.class.getSimpleName());
		result.add("name", nameOf(element));
		builder.add("methods", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("actionable", "");
	}

}
