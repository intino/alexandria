package teseo.codegeneration.server.jmx;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import tara.magritte.Layer;
import teseo.Application;
import teseo.helpers.Commons;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMXServerRenderer {
	private final List<Application> applications;

	public JMXServerRenderer(Graph graph) {
		applications = graph.find(Application.class);
	}


	public void execute(File destiny, String packageName) {
		applications.forEach(app -> processApplication(app, destiny, packageName));
	}

	private void processApplication(Application application, File destiny, String packageName) {
		final List<JMXTrigger> triggers = application.node().findNode(JMXTrigger.class);
		if (triggers.isEmpty()) return;
		Frame frame = new Frame().addTypes("jmx");
		frame.addSlot("name", application.name());
		frame.addSlot("package", packageName);
		frame.addSlot("trigger", processTriggers(triggers));
		Commons.writeFrame(destiny, snakeCaseToCamelCase(application.name()) + "JMX", template().format(frame));
	}

	private String[] processTriggers(List<JMXTrigger> triggers) {
		return triggers.stream().map(Layer::name).toArray(String[]::new);
	}

	private Template template() {
		Template template = JMXServerTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		template.add("quoted", value -> '"' + value.toString() + '"');
		return template;
	}
}
