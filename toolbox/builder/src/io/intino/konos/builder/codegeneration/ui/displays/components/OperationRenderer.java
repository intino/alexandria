package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.OperationComponents;
import io.intino.konos.model.graph.OperationComponents.Operation;

public class OperationRenderer<O extends Operation> extends ComponentRenderer<O> {

	public OperationRenderer(Settings settings, O component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	protected void fill(FrameBuilder builder) {
		super.fill(builder);
		addSignedMethods(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add(Operation.class.getSimpleName().toLowerCase());
		properties.add("title", element.title());
		properties.add("target", element.target().name().toLowerCase());
		properties.add("mode", mode());
		properties.add("operationMode", modeFrame());
		properties.add("size", element.size().name());
		if (element.isHighlighted()) properties.add("highlighted", element.asHighlighted().style().name());
		if (element.isReadonly() || element.i$(OperationComponents.SelectionOperation.class)) properties.add("readonly", "true");
		if (element.isAffirmed()) properties.add("affirmed", element.asAffirmed().affirmText());
		if (element.isSigned()) {
			Operation.Signed signed = element.asSigned();
			properties.add("signText", signed.signText());
			if (signed.reasonText() != null) properties.add("reasonText", signed.reasonText());
		}
		if (element.isMaterialIconButton()) properties.add("icon", element.asMaterialIconButton().icon());
		return properties;
	}

	private FrameBuilder modeFrame() {
		FrameBuilder result = new FrameBuilder("operationMode", mode());
		result.add("mode", mode());
		if (element.isIconButton()) result.add("icon", element.asIconButton().icon());
		else if (element.isMaterialIconButton()) result.add("icon", element.asMaterialIconButton().icon());
		return result;
	}

	private String mode() {
		if (element.isIconButton()) return Operation.IconButton.class.getSimpleName();
		else if (element.isMaterialIconButton()) return Operation.MaterialIconButton.class.getSimpleName();
		else if (element.isButton()) return Operation.Button.class.getSimpleName();
		return Operation.Link.class.getSimpleName();
	}

	private void addSignedMethods(FrameBuilder builder) {
		if (!element.isSigned()) return;
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Operation.Signed.class.getSimpleName());
		result.add("name", nameOf(element));
		builder.add("methods", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}

}
