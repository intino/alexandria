package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Operation;
import io.intino.konos.model.graph.button.childcomponents.ButtonOperation;
import io.intino.konos.model.graph.iconbutton.childcomponents.IconButtonOperation;
import io.intino.konos.model.graph.link.childcomponents.LinkOperation;
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
		properties.addSlot("target", element.target().name().toLowerCase());
		properties.addSlot("operationMode", modeFrame());
		return properties;
	}

	private Frame modeFrame() {
		Frame frame = new Frame("operationMode", mode());
		if (element.isIconButton()) frame.addSlot("icon", element.asIconButton().alexandriaIcon());
		return frame;
	}

	private String mode() {
		if (element.isLink()) return className(LinkOperation.class);
		else if (element.isIconButton()) return className(IconButtonOperation.class);
		else if (element.isButton()) return className(ButtonOperation.class);
		return "";
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
