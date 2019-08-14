package io.intino.konos.builder.codegeneration.services.taskinbox;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.itrules.formatters.StringFormatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Inbox;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.list.ListData;
import io.intino.konos.model.graph.taskinbox.TaskInboxService;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Locale;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class TaskInboxRenderer extends Renderer {
	private static final String TASKINBOX_PACKAGE = "taskinbox";
	private final List<TaskInboxService> services;

	public TaskInboxRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
		this.services = graph.taskInboxServiceList();
	}

	@Override
	public void render() {
		services.forEach(this::processService);
	}

	private void processService(TaskInboxService service) {
		writeFrame(new File(gen(), TASKINBOX_PACKAGE), nameOf(service), template().render(new FrameBuilder("taskInbox", "service").
				add("name", service.name$()).
				add("box", boxName()).
				add("package", packageName()).
				add("inbox", processInboxes(service.inboxList()))));
		for (Inbox inbox : service.inboxList())
			writeFrame(new File(src(), TASKINBOX_PACKAGE), nameOf(inbox), inboxTemplate().render(frameOf(inbox)));
	}

	private Frame[] processInboxes(List<Inbox> inboxList) {
		return inboxList.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(Inbox inbox) {
		FrameBuilder builder = new FrameBuilder("inbox").add("name", inbox.name$());
		if (inbox.isProcessTrigger()) {
			builder.add("process");
			builder.add("taskInbox", inbox.core$().owner().name());
			builder.add("package", packageName());
			builder.add("box", boxName());
			builder.add("process", inbox.asProcessTrigger().process().name$());
			builder.add("input", parameterType(inbox.asProcessTrigger().input()));
			builder.add("output", Commons.returnType(inbox.asProcessTrigger().output(), packageName()));
		}
		return builder.toFrame();
	}


	private Frame parameterType(io.intino.konos.model.graph.Parameter parameter) {
		String innerPackage = parameter.isObject() ? String.join(".", packageName(), "schemas.") : "";
		final FrameBuilder builder = new FrameBuilder().add("value", innerPackage + parameter.asType().type());
		if (parameter.i$(ListData.class)) builder.add("list");
		return builder.toFrame();
	}

	@NotNull
	private String nameOf(TaskInboxService service) {
		return StringFormatters.get(Locale.getDefault()).get("firstuppercase").format(service.name$()).toString() + "Service";
	}

	private String nameOf(Inbox inbox) {
		return StringFormatters.get(Locale.getDefault()).get("firstuppercase").format(inbox.name$()).toString();
	}

	private Template template() {
		return customize(new TaskInboxServiceTemplate());
	}

	private Template inboxTemplate() {
		return customize(new InboxTemplate());
	}

}