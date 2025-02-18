package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.ActionableComponents;
import io.intino.konos.dsl.ActionableComponents.IconButton.TitlePosition;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class ActionableRenderer extends ComponentRenderer<ActionableComponents.Actionable> {

	public ActionableRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
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
		addShortcut(properties);
		if (element.isReadonly() || isSelectionContext()) properties.add("readonly", "true");
		if (element.isAffirmed()) properties.add("affirmed", element.asAffirmed().affirmText());
		if (element.isSigned()) {
			ActionableComponents.Actionable.Signed signed = element.asSigned();
			properties.add("signMode", signed.mode().name());
			properties.add("signText", signed.signText());
			if (signed.mode() == ActionableComponents.Actionable.Signed.Mode.OneTimePassword)
				properties.add("signChecker", "oneTimePassword");
			if (signed.reasonText() != null) properties.add("reasonText", signed.reasonText());
		}
		if (element.i$(conceptOf(ActionableComponents.MaterialIconButton.class))) {
			ActionableComponents.MaterialIconButton materialIconButton = element.a$(ActionableComponents.MaterialIconButton.class);
			properties.add("icon", materialIconButton.icon());
			if (materialIconButton.titlePosition() != ActionableComponents.MaterialIconButton.TitlePosition.None) properties.add("titlePosition", materialIconButton.titlePosition().name());
		}
		else if (element.i$(conceptOf(ActionableComponents.IconToggle.class)) && element.a$(ActionableComponents.IconToggle.class).titlePosition() != ActionableComponents.IconToggle.TitlePosition.None) {
			properties.add("titlePosition", element.a$(ActionableComponents.IconToggle.class).titlePosition().name());
		}
		else if (element.i$(conceptOf(ActionableComponents.IconButton.class)) && element.a$(ActionableComponents.IconButton.class).titlePosition() != TitlePosition.None) {
			properties.add("titlePosition", element.a$(ActionableComponents.IconButton.class).titlePosition().name());
		}
		else if (element.i$(conceptOf(ActionableComponents.MaterialIconToggle.class))) {
			ActionableComponents.MaterialIconToggle materialIconToggle = element.a$(ActionableComponents.MaterialIconToggle.class);
			properties.add("icon", materialIconToggle.icon());
			if (materialIconToggle.titlePosition() != ActionableComponents.MaterialIconToggle.TitlePosition.None) properties.add("titlePosition", materialIconToggle.titlePosition().name());
		}
		else if (element.i$(conceptOf(ActionableComponents.AbstractSplitButton.class))) {
			if (element.i$(conceptOf(ActionableComponents.IconSplitButton.class)) && element.a$(ActionableComponents.IconSplitButton.class).titlePosition() != ActionableComponents.IconSplitButton.TitlePosition.None) {
				properties.add("titlePosition", element.a$(ActionableComponents.IconSplitButton.class).titlePosition().name());
			}
			else if (element.i$(conceptOf(ActionableComponents.MaterialIconSplitButton.class)) && element.a$(ActionableComponents.MaterialIconSplitButton.class).titlePosition() != ActionableComponents.MaterialIconSplitButton.TitlePosition.None) {
				properties.add("titlePosition", element.a$(ActionableComponents.MaterialIconSplitButton.class).titlePosition().name());
			}
		}
		return properties;
	}

	private void addShortcut(FrameBuilder properties) {
		if (element.shortcut() == null) return;
		properties.add("shortcut", shortcutFrame(element.shortcut()));
	}

	private FrameBuilder shortcutFrame(ActionableComponents.Actionable.Shortcut shortcut) {
		FrameBuilder result = new FrameBuilder("shortcut");
		result.add("key", shortcut.key());
		result.add("altKey", shortcut.metaKeys().contains(ActionableComponents.Actionable.Shortcut.MetaKeys.Alt));
		result.add("ctrlKey", shortcut.metaKeys().contains(ActionableComponents.Actionable.Shortcut.MetaKeys.Control));
		result.add("shiftKey", shortcut.metaKeys().contains(ActionableComponents.Actionable.Shortcut.MetaKeys.Shift));
		result.add("visible", shortcut.visible());
		return result;
	}

	private void addHighlight(FrameBuilder properties) {
		if (!element.i$(conceptOf(ActionableComponents.AbstractButton.class))) return;
		ActionableComponents.AbstractButton button = element.a$(ActionableComponents.AbstractButton.class);
		if (button.highlight() == ActionableComponents.AbstractButton.Highlight.None) return;
		properties.add("highlighted", button.highlight().name());
	}

	private boolean isSelectionContext() {
		return element.isAction() && element.asAction().context() == ActionableComponents.Actionable.Action.Context.Selection ||
				element.isDownload() && element.asDownload().context() == ActionableComponents.Actionable.Download.Context.Selection;
	}

	private FrameBuilder modeFrame() {
		FrameBuilder result = new FrameBuilder("actionableMode", mode());
		result.add("mode", mode());
		if (element.i$(conceptOf(ActionableComponents.IconButton.class))) addIcons(element.a$(ActionableComponents.IconButton.class), result);
		else if (element.i$(conceptOf(ActionableComponents.MaterialIconButton.class))) addIcons(element.a$(ActionableComponents.MaterialIconButton.class), result);
		else if (element.i$(conceptOf(ActionableComponents.IconToggle.class))) addIcons(element.a$(ActionableComponents.IconToggle.class), result);
		else if (element.i$(conceptOf(ActionableComponents.MaterialIconToggle.class))) addIcons(element.a$(ActionableComponents.MaterialIconToggle.class), result);
		else if (element.i$(conceptOf(ActionableComponents.AbstractSplitButton.class))) {
			result.add("option", element.a$(ActionableComponents.AbstractSplitButton.class).options().toArray());
			result.add("default", element.a$(ActionableComponents.AbstractSplitButton.class).defaultOption());
			if (element.i$(conceptOf(ActionableComponents.IconSplitButton.class))) addIcons(element.a$(ActionableComponents.IconSplitButton.class), result);
			else if (element.i$(conceptOf(ActionableComponents.MaterialIconSplitButton.class))) addIcons(element.a$(ActionableComponents.MaterialIconSplitButton.class), result);
		}
		return result;
	}

	private void addIcons(ActionableComponents.MaterialIconSplitButton actionable, FrameBuilder result) {
		result.add("icon", actionable.icon());
		if (actionable.darkIcon() != null) result.add("darkIcon", actionable.darkIcon());
	}

	private void addIcons(ActionableComponents.IconSplitButton actionable, FrameBuilder result) {
		result.add("icon", actionable.icon());
		if (actionable.darkIcon() != null) result.add("darkIcon", actionable.darkIcon());
	}

	private void addIcons(ActionableComponents.MaterialIconToggle actionable, FrameBuilder result) {
		result.add("icon", actionable.icon());
		if (actionable.darkIcon() != null) result.add("darkIcon", actionable.darkIcon());
	}

	private void addIcons(ActionableComponents.IconToggle actionable, FrameBuilder result) {
		result.add("icon", actionable.icon());
		if (actionable.darkIcon() != null) result.add("darkIcon", actionable.darkIcon());
	}

	private void addIcons(ActionableComponents.MaterialIconButton actionable, FrameBuilder result) {
		result.add("icon", actionable.icon());
		if (actionable.darkIcon() != null) result.add("darkIcon", actionable.darkIcon());
	}

	private void addIcons(ActionableComponents.IconButton actionable, FrameBuilder result) {
		result.add("icon", actionable.icon());
		if (actionable.darkIcon() != null) result.add("darkIcon", actionable.darkIcon());
	}

	private String mode() {
		if (element.i$(conceptOf(ActionableComponents.IconButton.class)))
			return ActionableComponents.IconButton.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.MaterialIconButton.class)))
			return ActionableComponents.MaterialIconButton.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.Button.class)))
			return ActionableComponents.Button.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.IconToggle.class)))
			return ActionableComponents.IconToggle.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.MaterialIconToggle.class)))
			return ActionableComponents.MaterialIconToggle.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.Toggle.class)))
			return ActionableComponents.Toggle.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.SplitButton.class)))
			return ActionableComponents.SplitButton.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.IconSplitButton.class)))
			return ActionableComponents.IconSplitButton.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.MaterialIconSplitButton.class)))
			return ActionableComponents.MaterialIconSplitButton.class.getSimpleName();
		else if (element.i$(conceptOf(ActionableComponents.AvatarIconButton.class)))
			return ActionableComponents.AvatarIconButton.class.getSimpleName();
		return ActionableComponents.Link.class.getSimpleName();
	}

	private void addSignedMethods(FrameBuilder builder) {
		if (!element.isSigned()) return;
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(ActionableComponents.Actionable.Signed.class.getSimpleName());
		result.add("name", nameOf(element));
		builder.add("methods", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("actionable", "");
	}

}
