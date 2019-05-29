package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.Dataframe;
import io.intino.konos.model.graph.OtherComponents.Chart;

public class ChartRenderer extends SizedRenderer<Chart> {

	public ChartRenderer(Settings settings, Chart component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("query", element.query());
		addInput(result);
		addOutput(result);
		return result;
	}

	private void addInput(FrameBuilder builder) {
		Dataframe input = element.input();
		String type = input.isCustom() ? "source" : "csv";
		builder.add("input", inputMethodFrame(input, type));
	}

	private FrameBuilder inputMethodFrame(Dataframe input, String type) {
		FrameBuilder frame = new FrameBuilder("inputMethod", type).add("value", value(input));
		addOwner(frame);
		return frame;
	}

	private String value(Dataframe input) {
		if (input.isCSV()) return fixResourceValue(input.asCSV().datasourceFilename());
		if (input.isCustom()) return input.asCustom().datasourceClass();
		return "";
	}

	private void addOutput(FrameBuilder builder) {
		Chart.Output output = element.output();
		builder.add("output", output.name());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("image", "");
	}
}
