package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.OperationComponents.Toolbar;
import org.siani.itrules.model.Frame;

public class ToolbarRenderer extends ComponentRenderer<Toolbar> {

	public ToolbarRenderer(Settings settings, Toolbar component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		addBinding(frame);
		return frame;
	}

	private void addBinding(Frame frame) {
		if (!element.isLinked()) return;

		CatalogComponents.Collection collection = element.asLinked().to();
		if (collection == null) return;

		frame.addSlot("binding", new Frame("binding", Toolbar.class.getSimpleName())
			 	.addSlot("name", nameOf(element))
				.addSlot("collection", nameOf(collection)));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("toolbar", "");
	}
}
