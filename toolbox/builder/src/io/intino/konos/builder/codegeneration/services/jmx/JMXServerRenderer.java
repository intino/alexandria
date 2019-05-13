package io.intino.konos.builder.codegeneration.services.jmx;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.jmx.JMXService.Operation;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class JMXServerRenderer {
	private final List<JMXService> jmxServices;
	private final File destination;
	private final String packageName;
	private final String boxName;

	public JMXServerRenderer(KonosGraph graph, File destination, String packageName, String boxName) {
		jmxServices = graph.jMXServiceList();
		this.destination = destination;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		jmxServices.forEach(this::processService);
	}

	private void processService(JMXService service) {
		final List<Operation> operations = service.operationList();
		if (operations.isEmpty()) return;
		writeFrame(destination, "JMX" + snakeCaseToCamelCase(service.name$()), template().render(new FrameBuilder("jmxserver")
				.add("name", service.name$())
				.add("box", boxName)
				.add("package", packageName).toFrame()));
	}

	private Template template() {
		return Formatters.customize(new JMXServerTemplate());
	}
}
