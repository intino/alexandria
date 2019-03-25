package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Chart;
import org.siani.itrules.model.Frame;

public class ChartRenderer extends SizedRenderer<Chart> {

	public ChartRenderer(Settings settings, Chart component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		addInput(result);
		result.addSlot("code", element.code());
		return result;
	}

	private void addInput(Frame frame) {
		Chart.Input input = element.input();
		if (input == null || !input.isCSV()) return;
		frame.addSlot("source", resourceMethodFrame("input", input.asCSV().source()));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("image", "");
	}
}
