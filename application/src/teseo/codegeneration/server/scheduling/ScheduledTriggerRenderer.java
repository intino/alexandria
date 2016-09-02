package teseo.codegeneration.server.scheduling;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Application;
import teseo.Channel;
import teseo.Producer;
import teseo.scheduled.ScheduledTrigger;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.writeFrame;

public class ScheduledTriggerRenderer {
	private final List<Application> applications;
	private File genDestination;
	private String packageName;

	public ScheduledTriggerRenderer(Graph graph) {
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
		for (Channel channel : trigger.as(Producer.class).channel())
			frame.addSlot("channel", new Frame().addTypes("channel").addSlot("name", channel.name()).addSlot("package", packageName));
		writeFrame(destinyPackage(), trigger.name() + "Trigger", template().format(frame));
	}

	private Template template() {
		final Template template = ScheduledTriggerTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private File destinyPackage() {
		return new File(genDestination, "scheduling");
	}

}
