package io.intino.konos.builder.codegeneration.services.jmx;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.jmx.JMXService.Operation;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

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
		Frame frame = new Frame().addTypes("jmxserver");
		frame.addSlot("name", service.name$());
		frame.addSlot("box", boxName);
		frame.addSlot("package", packageName);
		Commons.writeFrame(destination, "JMX" + snakeCaseToCamelCase(service.name$()), template().format(frame));
	}

	private Template template() {
		return Formatters.customize(JMXServerTemplate.create());
	}
}
