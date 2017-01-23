package io.intino.pandora.builder.codegeneration.server.slack;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import io.intino.pandora.builder.codegeneration.Formatters;
import io.intino.pandora.builder.helpers.Commons;
import io.intino.pandora.model.slackbot.SlackBotService;
import io.intino.tara.magritte.Graph;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class SlackRenderer {
	private final Project project;
	private final List<SlackBotService> services;
	private final File src;
	private final File gen;
	private final String packageName;
	private final String boxName;

	public SlackRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.services = graph.find(SlackBotService.class);
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(SlackBotService service) {
		String srcName = snakeCaseToCamelCase(service.name()) + "SlackBotActions";
		Commons.writeFrame(gen, snakeCaseToCamelCase(service.name()) + "SlackBot", template().format(createFrame(service, true)));
		if (alreadyRendered(src, srcName)) updateBot(service, srcName);
		else newBotActions(service, srcName);
	}

	private void updateBot(SlackBotService service, String name) {
		new BotActionsUpdater(project, Commons.javaFile(src, name), service.requestList(), boxName).update();
		VirtualFileManager.getInstance().asyncRefresh(null);
	}

	private void newBotActions(SlackBotService service, String name) {
		Commons.writeFrame(src, name, template().format(createFrame(service, false)));
	}

	@NotNull
	private Frame createFrame(SlackBotService service, boolean gen) {
		final List<SlackBotService.Request> requests = service.requestList();
		Frame frame = new Frame().addTypes("slack", (gen ? "gen" : "actions"));
		frame.addSlot("package", packageName).
				addSlot("name", service.name()).
				addSlot("box", boxName);
		createRequests(service, requests, frame);
		return frame;
	}

	private void createRequests(SlackBotService service, List<SlackBotService.Request> requests, Frame frame) {
		for (SlackBotService.Request request : requests) {
			frame.addSlot("request", createRequestFrame(service, request));
			createRequests(service, request.requestList(), frame);
		}
	}

	private Frame createRequestFrame(SlackBotService service, SlackBotService.Request request) {
		final Frame requestFrame = new Frame().addTypes("request").addSlot("bot", service.name()).addSlot("box", boxName).addSlot("name", request.name()).addSlot("description", request.description());
		final List<SlackBotService.Request.Parameter> parameters = request.parameterList();
		for (int i = 0; i < parameters.size(); i++)
			requestFrame.addSlot("parameter", new Frame().addTypes("parameter", parameters.get(i).type().name(), parameters.get(i).multiple() ? "multiple" : "single").
					addSlot("type", parameters.get(i).type().name()).addSlot("name", parameters.get(i).name()).addSlot("pos", i));
		for (SlackBotService.Request component : request.requestList())
			requestFrame.addSlot("component", component.name());
		return requestFrame;
	}

	private Template template() {
		return Formatters.customize(SlackTemplate.create());
	}

	private boolean alreadyRendered(File destiny, String name) {
		return Commons.javaFile(destiny, name).exists();
	}
}
