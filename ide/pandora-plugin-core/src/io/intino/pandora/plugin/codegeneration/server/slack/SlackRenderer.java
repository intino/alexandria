package io.intino.pandora.plugin.codegeneration.server.slack;

import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.slackbot.SlackBotService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class SlackRenderer {
	private final List<SlackBotService> slackServices;
	private final File destiny;
	private final String packageName;

	public SlackRenderer(Graph graph, File destiny, String packageName) {
		slackServices = graph.find(SlackBotService.class);
		this.destiny = destiny;
		this.packageName = packageName;
	}


	public void execute() {
		slackServices.forEach(this::processService);
	}

	private void processService(SlackBotService service) {
		final List<SlackBotService.Request> requests = service.requestList();
		Frame frame = new Frame().addTypes("slack");
		frame.addSlot("package", packageName);
		frame.addSlot("name", service.name());
		frame.addSlot("token", service.token());
		for (SlackBotService.Request request : requests) {
			final Frame requestFrame = new Frame().addTypes("request");
			requestFrame.addSlot("name", request.name());
			requestFrame.addSlot("description", request.description());
			final List<SlackBotService.Request.Parameter> parameters = request.parameterList();
			for (int i = 0; i < parameters.size(); i++)
				requestFrame.addSlot("parameter", new Frame().addTypes("parameter", parameters.get(i).type().name()).
						addSlot("type", parameters.get(i).type().name()).addSlot("name", parameters.get(i).name()).addSlot("pos", i));
			frame.addSlot("request", requestFrame);
		}
		String name = snakeCaseToCamelCase(service.name()) + "SlackBot";
		if (!alreadyRendered(destiny, name))
			Commons.writeFrame(destiny, name, template().format(frame));
	}

	private Template template() {
		Template template = SlackTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", ""));
		template.add("quoted", value -> '"' + value.toString() + '"');
		return template;
	}

	private boolean alreadyRendered(File destiny, String action) {
		return Commons.javaFile(destinyPackage(destiny), action + "Action").exists();
	}

	private File destinyPackage(File destiny) {
		return new File(destiny, "actions");
	}
}
