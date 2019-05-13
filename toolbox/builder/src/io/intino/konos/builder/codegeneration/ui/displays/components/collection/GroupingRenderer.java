package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.CatalogComponents.Grouping;
import org.siani.itrules.model.Frame;

public class GroupingRenderer extends ComponentRenderer<Grouping> {

	public GroupingRenderer(Settings settings, Grouping component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		addBinding(frame);
		return frame;
	}

	private void addBinding(Frame frame) {
		if (element.collection() == null) return;
		Frame result = new Frame("binding", type()).addSlot("name", nameOf(element));
		result.addSlot("collection", nameOf(element.collection()));
		frame.addSlot("binding", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
