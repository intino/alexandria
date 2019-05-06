package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.OtherComponents.Chart;
import io.intino.konos.model.graph.Dataframe;
import org.siani.itrules.model.Frame;

public class ChartRenderer extends SizedRenderer<Chart> {

	public ChartRenderer(Settings settings, Chart component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addSlot("query", element.query());
		addInput(result);
		addOutput(result);
		return result;
	}

	private void addInput(Frame frame) {
		Dataframe input = element.input();
		String type = input.isCustom() ? "source" : "csv";
		frame.addSlot("input", inputMethodFrame(input, type));
	}

	private Frame inputMethodFrame(Dataframe input, String type) {
		Frame frame = new Frame("inputMethod", type).addSlot("value", value(input));
		addOwner(frame);
		return frame;
	}

	private String value(Dataframe input) {
		if (input.isCSV()) return fixResourceValue(input.asCSV().datasourceFilename());
		if (input.isCustom()) return input.asCustom().datasourceClass();
		return "";
	}

	private void addOutput(Frame frame) {
		Chart.Output output = element.output();
		frame.addSlot("output", output.name());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("image", "");
	}
}
