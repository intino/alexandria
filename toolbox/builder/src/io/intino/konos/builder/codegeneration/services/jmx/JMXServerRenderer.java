package io.intino.konos.builder.codegeneration.services.jmx;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.jmx.JMXService.Operation;

import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class JMXServerRenderer extends Renderer {
	private final List<JMXService> jmxServices;

	public JMXServerRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
		jmxServices = graph.jMXServiceList();
	}

	@Override
	public void render() {
		jmxServices.forEach(this::processService);
	}

	private void processService(JMXService service) {
		final List<Operation> operations = service.operationList();
		if (operations.isEmpty()) return;
		writeFrame(gen(), "JMX" + snakeCaseToCamelCase(service.name$()), template().render(new FrameBuilder("jmxserver")
				.add("name", service.name$())
				.add("box", boxName())
				.add("package", packageName()).toFrame()));
	}

	private Template template() {
		return Formatters.customize(new JMXServerTemplate());
	}
}
