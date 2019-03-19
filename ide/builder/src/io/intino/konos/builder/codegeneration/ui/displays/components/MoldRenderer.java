package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Mold;
import io.intino.konos.model.graph.rules.Spacing;
import org.siani.itrules.model.Frame;

public class MoldRenderer extends ComponentRenderer<Mold> {

	public MoldRenderer(Settings settings, Mold component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		addAttributes(frame);
		return frame;
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		addSpacing(result);
		addLayout(result);
		return result;
	}

	private void addSpacing(Frame result) {
		if (element.spacing() != Spacing.None) result.addSlot("spacing", element.spacing().value());
	}

	private void addLayout(Frame result) {
		String[] layout = element.layout().stream().map(l -> l.name().toLowerCase()).toArray(String[]::new);
		result.addSlot("layout", layout);
	}

	private void addAttributes(Frame result) {
		String modelClass = element.modelClass();
		if (modelClass == null) return;
		Frame frame = new Frame("attribute");
		frame.addSlot("clazz", modelClass);
		frame.addSlot("name", modelClass.lastIndexOf(".") != -1 ? modelClass.substring(modelClass.lastIndexOf(".")+1) : modelClass);
		result.addSlot("attribute", frame);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("mold", "");
	}
}
