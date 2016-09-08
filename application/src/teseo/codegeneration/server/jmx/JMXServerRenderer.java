package teseo.codegeneration.server.jmx;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Operation;
import teseo.helpers.Commons;
import teseo.jmx.JMXService;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMXServerRenderer {
	private final List<JMXService> jmxServices;

	public JMXServerRenderer(Graph graph) {
		jmxServices = graph.find(JMXService.class);
	}


	public void execute(File destiny, String packageName) {
		jmxServices.forEach(service -> processService(service, destiny, packageName));
	}

	private void processService(JMXService service, File destiny, String packageName) {
		final List<Operation> operations = service.operationList();
		if (operations.isEmpty()) return;
		Frame frame = new Frame().addTypes("jmxserver");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		Commons.writeFrame(destiny, "JMX" +snakeCaseToCamelCase(service.name()), template().format(frame));
	}


	private Template template() {
		Template template = JMXServerTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		template.add("quoted", value -> '"' + value.toString() + '"');
		return template;
	}
}
