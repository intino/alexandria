package teseo.codegeneration.server.scheduling;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Application;
import teseo.scheduled.application.ScheduledTrigger;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.javaFile;
import static teseo.helpers.Commons.writeFrame;

public class JavaScheduledTriggerRenderer {
	private final List<Application> applications;
	private File genDestination;
	private String packageName;

	public JavaScheduledTriggerRenderer(Graph graph) {
		applications = graph.find(Application.class);
	}

	public void execute(File genDestination, String packageName) {
		this.genDestination = genDestination;
		this.packageName = packageName;
		this.applications.forEach(this::processApplication);
	}

	private void processApplication(Application application) {
		application.node().findNode(ScheduledTrigger.class).forEach(this::processTrigger);
	}

	private void processTrigger(ScheduledTrigger trigger) {
		Frame frame = new Frame().addTypes("scheduled");
		frame.addSlot("name", trigger.name());
		frame.addSlot("package", packageName);
		if (!alreadyRendered(trigger))
			writeFrame(destinyPackage(), trigger.name() + "Trigger", template().format(frame));
	}

	private Template template() {
		final Template template = ScheduledTriggerTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private boolean alreadyRendered(ScheduledTrigger trigger) {
		return javaFile(destinyPackage(), trigger.name() + "Trigger").exists();
	}

	private File destinyPackage() {
		return new File(genDestination, "scheduling");
	}

}
