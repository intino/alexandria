package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.CatalogComponents.GroupBox;
import org.siani.itrules.model.Frame;

public class GroupBoxRenderer extends ComponentRenderer<GroupBox> {

	public GroupBoxRenderer(Settings settings, GroupBox component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		addBinding(frame);
		return frame;
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		addHistograms(result);
		return result;
	}

	private void addHistograms(Frame frame) {
		if (element.histogram() == null) return;
		Frame result = new Frame("histogram");
		GroupBox.Histogram histogram = element.histogram();
		result.addSlot("alwaysVisible", histogram.alwaysVisible());
		result.addSlot("type", histogram.type().name());
		frame.addSlot("histogram", result);
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
