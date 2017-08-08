package io.intino.konos.builder.codegeneration.services.slack;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.slackbot.SlackBotService;
import io.intino.konos.model.graph.slackbot.SlackBotService.Request;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.slackbot.SlackBotService.Request.ResponseType.Text;

public class SlackRenderer {
	private final Project project;
	private final List<SlackBotService> services;
	private final File src;
	private final File gen;
	private final String packageName;
	private final String boxName;

	public SlackRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.services = graph.slackBotServiceList();
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(SlackBotService service) {
		String srcName = snakeCaseToCamelCase(service.name$()) + "Slack";
		final Frame frame = createFrame(service.name$(), service.requestList(), true);
		for (String level : collectLevels(service).keySet())
			frame.addSlot("level", new Frame().addTypes("level").addSlot("name", level));
		writeFrame(gen, snakeCaseToCamelCase(service.name$()) + "SlackBot", template().format(frame));
		if (alreadyRendered(new File(src, "slack"), srcName)) updateBot(service, srcName);
		else newBotActions(service);
	}

	private void updateBot(SlackBotService service, String name) {
		new BotActionsUpdater(project, Commons.javaFile(new File(src, "slack"), name), service.requestList(), boxName).update();
		VirtualFileManager.getInstance().asyncRefresh(null);
	}

	private void newBotActions(SlackBotService service) {
		final File directory = new File(src, "slack");
		if (!alreadyRendered(directory, snakeCaseToCamelCase(service.name$()) + "Slack"))
			writeFrame(directory, snakeCaseToCamelCase(service.name$()) + "Slack", template().format(createFrame(service.name$(), service.requestList(), false)));
		Map<String, List<Request>> groups = collectLevels(service);
		for (String requestContainer : groups.keySet())
			if (!alreadyRendered(directory, requestContainer + "Slack"))
				writeFrame(directory, requestContainer + "Slack", template().format(createFrame(requestContainer, groups.get(requestContainer), false)));
	}

	private Map<String, List<Request>> collectLevels(SlackBotService service) {
		final List<Request> requests = service.requestList();
		final Map<String, List<Request>> collect = collect(requests);
		return collect;
	}

	private LinkedHashMap<String, List<Request>> collect(List<Request> requests) {
		final LinkedHashMap<String, List<Request>> map = requests.stream().filter(request -> !request.requestList().isEmpty()).
				collect(Collectors.toMap(this::name, Request::requestList, (a, b) -> b, LinkedHashMap::new));
		for (Request request : requests) map.putAll(collect(request.requestList()));
		return map;
	}

	private String name(Request request) {
		String name = "";
		Request r = request;
		while (r.i$(Request.class)) {
			name = Commons.firstUpperCase(r.name$()) + name;
			if (!r.core$().owner().is(Request.class)) break;
			r = r.core$().ownerAs(Request.class);
		}
		return name;
	}

	@NotNull
	private Frame createFrame(String name, List<Request> requests, boolean gen) {
		Frame frame = new Frame().addTypes("slack", (gen ? "gen" : "actions"));
		frame.addSlot("package", packageName).
				addSlot("name", name).
				addSlot("box", boxName);
		if (gen) allRequests(requests, frame);
		else createRequests(requests, frame);
		return frame;
	}

	private void allRequests(List<Request> requests, Frame frame) {
		for (Request request : requests) {
			frame.addSlot("request", createRequestFrame(request));
			allRequests(request.requestList(), frame);
		}
	}

	private void createRequests(List<Request> requests, Frame frame) {
		for (Request request : requests) frame.addSlot("request", createRequestFrame(request));
	}

	private Frame createRequestFrame(Request request) {
		final Frame requestFrame = new Frame().addTypes("request").addSlot("type", request.core$().owner().is(Request.class) ? name(request.core$().ownerAs(Request.class)) : request.core$().owner().name()).addSlot("box", boxName).addSlot("name", request.name$()).addSlot("description", request.description());
		if (request.core$().owner().is(Request.class)) requestFrame.addSlot("context", name(request.core$().ownerAs(Request.class)));
		requestFrame.addSlot("responseType", request.responseType().equals(Text) ? "String" : "SlackAttachment");
		final List<Request.Parameter> parameters = request.parameterList();
		for (int i = 0; i < parameters.size(); i++)
			requestFrame.addSlot("parameter", new Frame().addTypes("parameter", parameters.get(i).type().name(), parameters.get(i).multiple() ? "multiple" : "single").
					addSlot("type", parameters.get(i).type().name()).addSlot("name", parameters.get(i).name$()).addSlot("pos", i));
		for (Request component : request.requestList())
			requestFrame.addSlot("component", component.name$());
		return requestFrame;
	}

	private Template template() {
		return Formatters.customize(SlackTemplate.create());
	}

	private boolean alreadyRendered(File destiny, String name) {
		return Commons.javaFile(destiny, name).exists();
	}
}
