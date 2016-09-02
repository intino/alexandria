package teseo.codegeneration.server.jmx;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Action;
import teseo.Application;
import teseo.Trigger;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.writeFrame;

public class JMXTriggerRenderer {

	private final List<Application> applications;
	private File genDestination;
	private String packageName;

	public JMXTriggerRenderer(Graph graph) {
		applications = graph.find(Application.class);
	}

	public void execute(File genDestination, String packageName) {
		this.genDestination = genDestination;
		this.packageName = packageName;
		this.applications.forEach(this::processApplication);

	}

	private void processApplication(Application application) {
		application.node().findNode(Trigger.class).forEach(this::processTrigger);
	}

	private void processTrigger(Trigger trigger) {
		Frame frame = new Frame().addTypes("jmx");
		frame.addSlot("name", trigger.name());
		frame.addSlot("package", packageName);
		for (Action action : trigger.actions())
			frame.addSlot("action", frameOf(action));
		writeFrame(destinyPackage(), trigger.name() + "MBean", interfaceTemplate().format(frame));
		writeFrame(destinyPackage(), trigger.name(), implementationTemplate().format(frame));
	}

	private Frame frameOf(Action action) {
		return new Frame().addTypes("action").addSlot("name", action.name()).
				addSlot("package", packageName).addSlot("returnType", action.returnType());
	}

	private Template interfaceTemplate() {
		final Template template = JMXIntefaceTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private Template implementationTemplate() {
		final Template template = JMXBeanTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private File destinyPackage() {
		return new File(genDestination, "jmx");
	}
}
