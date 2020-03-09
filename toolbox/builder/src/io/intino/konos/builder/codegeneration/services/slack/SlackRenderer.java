package io.intino.konos.builder.codegeneration.services.slack;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Service.SlackBot.Request;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class SlackRenderer extends Renderer {
	private final List<Service.SlackBot> services;

	public SlackRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.services = graph.serviceList(Service::isSlackBot).map(Service::asSlackBot).collect(Collectors.toList());

	}

	@Override
	public void render() {
		services.forEach(this::processService);
	}

	private void processService(Service.SlackBot service) {
		String srcName = snakeCaseToCamelCase(service.name$()) + "Slack";
		final FrameBuilder builder = createFrameBuilder(service.name$(), service.requestList(), true);
		for (String level : collectLevels(service).keySet())
			builder.add("level", new FrameBuilder("level").add("name", level).toFrame());
		writeFrame(gen(), snakeCaseToCamelCase(service.name$()) + "SlackBot", template().render(builder));
		if (alreadyRendered(new File(src(), "slack"), srcName)) updateBot(service, srcName);
		else newBotActions(service);
	}

	private void updateBot(Service.SlackBot service, String name) {
		new BotActionsUpdater(context, Commons.javaFile(new File(src(), "slack"), name), service.requestList()).update();
	}

	private void newBotActions(Service.SlackBot service) {
		final File directory = new File(src(), "slack");
		if (!alreadyRendered(directory, snakeCaseToCamelCase(service.name$()) + "Slack"))
			writeFrame(directory, snakeCaseToCamelCase(service.name$()) + "Slack", template().render(createFrameBuilder(service.name$(), service.requestList(), false)));
		Map<String, List<Request>> groups = collectLevels(service);
		for (String requestContainer : groups.keySet()) {
			classes().put("Service#" + service.name$(), "slack." + requestContainer + "Slack");
			if (!alreadyRendered(directory, requestContainer + "Slack"))
				writeFrame(directory, requestContainer + "Slack", template().render(createFrameBuilder(requestContainer, groups.get(requestContainer), false)));
		}
	}

	private Map<String, List<Request>> collectLevels(Service.SlackBot service) {
		return collect(service.requestList());
	}

	private LinkedHashMap<String, List<Request>> collect(List<Request> requests) {
		final LinkedHashMap<String, List<Request>> map = requests.stream().filter(request -> !request.requestList().isEmpty()).
				collect(Collectors.toMap(this::name, Request::requestList, (a, b) -> b, LinkedHashMap::new));
		for (Request request : requests) map.putAll(collect(request.requestList()));
		return map;
	}

	private String name(Request request) {
		StringBuilder name = new StringBuilder();
		Request r = request;
		while (r.i$(Request.class)) {
			name.insert(0, Commons.firstUpperCase(r.name$()) + "|");
			if (!r.core$().owner().is(Request.class)) break;
			r = r.core$().ownerAs(Request.class);
		}
		final String s = name.toString();
		return s.endsWith("|") ? s.substring(0, s.length() - 1) : s;
	}

	private FrameBuilder createFrameBuilder(String name, List<Request> requests, boolean gen) {
		FrameBuilder builder = new FrameBuilder("slack", (gen ? "gen" : "actions"));
		builder.add("package", packageName()).
				add("name", name).
				add("box", boxName());
		if (gen) allRequests(requests, builder);
		else createRequests(requests, builder);
		return builder;
	}

	private void allRequests(List<Request> requests, FrameBuilder builder) {
		for (Request request : requests) {
			builder.add("request", createRequestFrame(request));
			allRequests(request.requestList(), builder);
		}
	}

	private void createRequests(List<Request> requests, FrameBuilder builder) {
		for (Request request : requests) builder.add("request", createRequestFrame(request));
	}

	private Frame createRequestFrame(Request request) {
		final FrameBuilder builder = new FrameBuilder("request").add("type", request.core$().owner().is(Request.class) ? name(request.core$().ownerAs(Request.class)) : request.core$().owner().name()).add("box", boxName()).add("name", request.name$()).add("description", request.description());
		if (request.core$().owner().is(Request.class))
			builder.add("context", name(request.core$().ownerAs(Request.class)));
		builder.add("responseType", request.responseType().equals(Request.ResponseType.Text) ? "String" : "SlackAttachment");
		final List<Request.Parameter> parameters = request.parameterList();
		for (int i = 0; i < parameters.size(); i++)
			builder.add("parameter", new FrameBuilder("parameter", parameters.get(i).type().name(), parameters.get(i).multiple() ? "multiple" : "single").
					add("type", parameters.get(i).type().name()).add("name", parameters.get(i).name$()).add("pos", i).toFrame());
		for (Request component : request.requestList())
			builder.add("component", component.name$());
		return builder.toFrame();
	}

	private Template template() {
		return Formatters.customize(new SlackTemplate()).add("slashToCamelCase", o -> snakeCaseToCamelCase(o.toString().replace("|", "_")));
	}

	private boolean alreadyRendered(File destiny, String name) {
		return Commons.javaFile(destiny, name).exists();
	}
}
