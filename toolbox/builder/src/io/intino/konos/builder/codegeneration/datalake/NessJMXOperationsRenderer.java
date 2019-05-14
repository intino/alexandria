package io.intino.konos.builder.codegeneration.datalake;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class NessJMXOperationsRenderer extends Renderer {
	private final boolean hasGraph;

	public NessJMXOperationsRenderer(Settings settings, KonosGraph graph, boolean hasGraph) {
		super(settings, Target.Service);
		this.hasGraph = hasGraph;
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("operations").
				add("package", packageName()).
				add("box", boxName());
		if (hasGraph) builder.add("graph");
		writeFrame(new File(gen(), "datalake"), "NessOperations", operationsTemplate().render(builder.toFrame()));
		writeFrame(new File(gen(), "datalake"), "NessOperationsMBean", operationsTemplate().render(builder.add("interface").toFrame()));
		if (!Commons.javaFile(new File(src(), "datalake"), "ReflowAssistant").exists())
			writeFrame(new File(src(), "datalake"), "ReflowAssistant", assistantTemplate().render(builder.toFrame()));
	}

	private Template operationsTemplate() {
		return Formatters.customize(new NessJMXOperationsTemplate());
	}

	private Template assistantTemplate() {
		return Formatters.customize(new ReflowAssistantTemplate());
	}
}
