package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.OperationComponents;
import io.intino.konos.model.graph.OperationComponents.Operation;
import io.intino.konos.model.graph.button.operationcomponents.ButtonOperation;
import io.intino.konos.model.graph.iconbutton.operationcomponents.IconButtonOperation;
import io.intino.konos.model.graph.link.operationcomponents.LinkOperation;
import io.intino.konos.model.graph.materialiconbutton.operationcomponents.MaterialIconButtonOperation;
import org.siani.itrules.model.Frame;

public class OperationRenderer<O extends Operation> extends ComponentRenderer<O> {

	public OperationRenderer(Settings settings, O component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		return super.buildFrame();
	}

	@Override
	public Frame properties() {
		Frame properties = super.properties();
		properties.addTypes(Operation.class.getSimpleName().toLowerCase());
		properties.addSlot("title", element.title());
		properties.addSlot("target", element.target().name().toLowerCase());
		properties.addSlot("operationMode", modeFrame());
		if (element.i$(OperationComponents.SelectionOperation.class)) properties.addSlot("disabled", "true");
		if (element.isConfirmable()) properties.addSlot("confirm", element.asConfirmable().confirmText());
		if (element.isMaterialIconButton()) properties.addSlot("icon", element.asMaterialIconButton().icon());
		return properties;
	}

	private Frame modeFrame() {
		Frame frame = new Frame("operationMode", mode());
		frame.addSlot("mode", mode());
		if (element.isIconButton()) frame.addSlot("icon", element.asIconButton().icon());
		else if (element.isMaterialIconButton()) frame.addSlot("icon", element.asMaterialIconButton().icon());
		return frame;
	}

	private String mode() {
		if (element.isIconButton()) return className(IconButtonOperation.class);
		else if (element.isMaterialIconButton()) return className(MaterialIconButtonOperation.class);
		else if (element.isButton()) return className(ButtonOperation.class);
		return className(LinkOperation.class);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
