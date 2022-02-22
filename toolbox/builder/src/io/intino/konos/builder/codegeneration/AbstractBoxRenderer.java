package io.intino.konos.builder.codegeneration;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.CompilationContext.DataHubManifest;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.compiler.shared.PostCompileConfigurationParameterActionMessage;
import io.intino.konos.model.graph.*;

import java.util.*;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.model.graph.Subscriber.Durable.SubscriptionMode.ReceiveAfterLastSeal;

public class AbstractBoxRenderer extends Renderer {
	private final KonosGraph graph;
	private final CompilerConfiguration configuration;
	private final Set<String> konosParameters;

	AbstractBoxRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.graph = graph;
		this.configuration = compilationContext.configuration();
		this.konosParameters = new LinkedHashSet<>();
	}

	@Override
	public void render() {
		FrameBuilder root = new FrameBuilder("box");
		root.add("name", boxName()).add("package", packageName());
		parent(root);
		connector(root);
		services(root);
		sentinels(root);
		datalake(root);
		terminal(root);
		workflow(root);
		Commons.writeFrame(context.gen(Target.Owner), "AbstractBox", template().render(root.toFrame()));
		context.compiledFiles().add(new OutputItem(sourceFileOf(graph), javaFile(gen(), "AbstractBox").getAbsolutePath()));
		notifyNewParameters();
	}

	private String sourceFileOf(KonosGraph graph) {
		if (!graph.serviceList().isEmpty()) return context.sourceFileOf(graph.serviceList().get(0));
		if (graph.datalake() != null) return context.sourceFileOf(graph.datalake());
		if (!graph.schemaList().isEmpty()) return context.sourceFileOf(graph.schemaList().get(0));
		if (!graph.datamartList().isEmpty()) return context.sourceFileOf(graph.datamartList().get(0));
		return null;
	}

	Set<String> customParameters() {
		return konosParameters;
	}

	private void notifyNewParameters() {
		konosParameters.forEach(p -> context.postCompileActionMessages().add(new PostCompileConfigurationParameterActionMessage(context.module(), p)));
	}

	private void sentinels(FrameBuilder builder) {
		if (!graph.sentinelList().isEmpty()) {
			FrameBuilder frame = new FrameBuilder("sentinel").add("configuration", boxName());
			if (graph.sentinelList().stream().anyMatch(Sentinel::isWebHook)) frame.add("hasWebhook", ",");
			builder.add("sentinel", frame);
		}
	}

	private void datalake(FrameBuilder root) {
		Datalake datalake = graph.datalake();
		if (datalake == null) return;
		FrameBuilder builder = new FrameBuilder("datalake", datalake.isLocal() ? "Local" : "Mirror");
		builder.add("package", packageName());
		builder.add("path", parameter(datalake.path()));
		if (datalake.isSshMirrored()) {
			Datalake.SshMirrored mirror = datalake.asSshMirrored();
			builder.add("parameter", parameter(mirror.url())).
					add("parameter", parameter(mirror.user())).
					add("parameter", parameter(mirror.password())).
					add("parameter", parameter(mirror.originDatalakePath())).
					add("parameter", parameter(mirror.startingTimetag()));
		} else if (datalake.isNfsMirrored())
			builder.add("parameter", parameter(datalake.asNfsMirrored().originDatalakePath())).
					add("parameter", parameter(datalake.asNfsMirrored().startingTimetag()));
		root.add("datalake", builder);
	}

	private void terminal(FrameBuilder root) {
		DataHubManifest manifest = context.dataHubManifest();
		if (manifest == null) return;
		FrameBuilder builder = new FrameBuilder("terminal").
				add("name", manifest.terminal).
				add("qn", manifest.qn).
				add("package", packageName()).
				add("box", boxName());
		Frame[] subscriber = graph.subscriberList().stream().filter(s -> manifest.tankClasses.containsKey(s.event())).map(s -> subscriberFrameOf(s, manifest)).toArray(Frame[]::new);
		if (subscriber.length != 0) builder.add("subscriber", subscriber);
		root.add("terminal", builder.toFrame());
	}

	private Frame subscriberFrameOf(Subscriber subscriber, DataHubManifest manifest) {
		String tankClass = manifest.tankClasses.get(subscriber.event());
		FrameBuilder builder = subscriberFrame(subscriber, manifest);
		if (!subscriber.splits().isEmpty())
			subscriber.splits().forEach(s -> splitFrame(tankClass, builder, s));
		else {
			if (manifest.messageContexts.get(subscriber.event()).size() > 1) {
				List<String> strings = manifest.messageContexts.get(subscriber.event());
				strings.sort(String::compareTo);
				for (String context : strings) splitFrame(tankClass, builder, context);
			}
		}
		return builder.toFrame();
	}

	private FrameBuilder subscriberFrame(Subscriber subscriber, DataHubManifest manifest) {
		FrameBuilder builder = new FrameBuilder("subscriber", "terminal").
				add("package", packageName()).
				add("name", subscriber.name$()).
				add("box", boxName()).
				add("terminal", manifest.qn).
				add("eventQn", subscriber.event().replace(".", "")).
				add("event", subscriber.event());
		if (subscriber.isDurable()) {
			builder.add("durable").add("subscriberId", subscriber.asDurable().subscriberId());
			if (subscriber.asDurable().subscriptionMode().equals(ReceiveAfterLastSeal)) builder.add("filtered");
		}
		return builder;
	}

	private void splitFrame(String tankClass, FrameBuilder builder, String context) {
		builder.add("split", new FrameBuilder("split").
				add("value", Formatters.snakeCaseToCamelCase().format(context.replace(".", "-")).toString()).
				add("type", tankClass).toFrame());
	}

	private void connector(FrameBuilder root) {
		if (graph.messagingServiceList().isEmpty() && context.dataHubManifest() == null) return;
		String[] parameters = new String[]{"datahub_url", "datahub_user", "datahub_password", "datahub_clientId", "datahub_outbox_directory"};
		FrameBuilder builder = new FrameBuilder("connector");
		for (String p : parameters)
			builder.add("parameter", parameter(p, "conf", p.contains("directory") ? "file" : "standard"));
		builder.add("package", packageName());
		builder.add("box", boxName());
		root.add("connector", builder.toFrame());
		Collections.addAll(konosParameters, parameters);
	}

	private void workflow(FrameBuilder root) {
		if (graph.workflow() == null || graph.workflow().processList().isEmpty()) return;
		root.add("workflow", buildBaseFrame().add("workflow"));
	}

	private FrameBuilder parameter(String parameter, String... types) {
		return new FrameBuilder(types).add("parameter").add(isCustom(parameter) ? "custom" : "standard").add("value", parameter);
	}

	private Frame parameter(Service.SlackBot service) {
		return new FrameBuilder(isCustom(service.token()) ? "custom" : "standard").add("value", service.token()).toFrame();
	}

	private void services(FrameBuilder builder) {
		if (!graph.messagingServiceList().isEmpty()) builder.add("jms", "");
		if (!graph.subscriberList().isEmpty() || !graph.uiServiceList().isEmpty() || !graph.restServiceList().isEmpty())
			builder.add("logger", new FrameBuilder("logger"));
		soap(builder);
		messaging(builder);
		jmx(builder);
		agenda(builder);
		slackServices(builder);
		ui(builder);
		rest(builder);
		if (!graph.soapServiceList().isEmpty() || !graph.restServiceList().isEmpty() || !graph.uiServiceList().isEmpty())
			builder.add("spark", "stop");
	}

	private void rest(FrameBuilder frame) {
		List<Service.REST> services = graph.restServiceList();
		services.sort(Comparator.<Service.REST>comparingInt(r -> r.notificationList().size()).reversed());
		for (Service.REST service : services)
			frame.add("service",
					new FrameBuilder("service", "rest")
							.add("name", service.name$())
							.add("configuration", boxName())
							.add("parameter", parameter(service.port())).toFrame());
	}

	private void soap(FrameBuilder frame) {
		for (Service.Soap service : graph.soapServiceList())
			frame.add("service",
					new FrameBuilder("service", "soap")
							.add("name", service.name$())
							.add("configuration", boxName())
							.add("parameter", parameter(service.port())).toFrame());
	}

	private void messaging(FrameBuilder frame) {
		for (Service.Messaging service : graph.messagingServiceList())
			frame.add("service", new FrameBuilder("service", "messaging").add("name", service.name$()).add("configuration", boxName()));
	}

	private void jmx(FrameBuilder frame) {
		for (Service.JMX service : graph.jmxServiceList())
			frame.add("service", new FrameBuilder("service", "jmx").add("name", service.name$()).add("configuration", boxName()).toFrame());
	}

	private void agenda(FrameBuilder frame) {
		if (graph.agendaServiceList().isEmpty() || graph.agendaServiceList().get(0).futureList().isEmpty()) return;
		final Service.Agenda service = graph.agendaServiceList().get(0);
		frame.add("service", new FrameBuilder("service", "agenda").add("name", service.name$()).add("configuration", boxName()).toFrame());
	}

	private void slackServices(FrameBuilder frame) {
		for (Service.SlackBot service : graph.slackBotServiceList()) {
			frame.add("service", new FrameBuilder("service", "slack")
					.add("name", service.name$()).add("configuration", boxName())
					.add("parameter", parameter(service)).toFrame());
		}
	}

	private void ui(FrameBuilder builder) {
		if (!graph.uiServiceList().isEmpty()) {
			final FrameBuilder uiFrame = uiFrame();
			if (context.parent() != null) uiFrame.add("parent", context.parent());
			builder.add("hasUi", uiFrame);
			builder.add("uiAuthentication", uiFrame);
			builder.add("uiEdition", uiFrame);
			builder.add("service", graph.uiServiceList().stream().map(s -> ui(s, boxName())).toArray(Frame[]::new));
		}
	}

	private FrameBuilder uiFrame() {
		FrameBuilder result = new FrameBuilder().add("package", packageName());
		graph.uiServiceList().forEach(service -> service.useList().forEach(use -> result.add("useDictionaries", dictionariesOf(use))));
		return result;
	}

	private Frame ui(Service.UI service, String name) {
		final FrameBuilder builder = new FrameBuilder("service", "ui").add("name", service.name$()).add("configuration", name)
				.add("parameter", parameter(service.port()).toFrame()).add("package", packageName());
		if (service.authentication() != null)
			builder.add("authentication", parameter(service.authentication().by()).toFrame());
		if (service.edition() != null)
			builder.add("edition", new FrameBuilder(isCustom(service.edition().by()) ? "custom" : "standard").add("value", service.edition().by()).toFrame());
		service.useList().forEach(use -> builder.add("use", serviceNameOf(use)));
		return builder.toFrame();
	}

	private String serviceNameOf(Service.UI.Use use) {
		return use.package$() + ".box.ui." + firstUpperCase(use.service()) + "Service";
	}

	private String dictionariesOf(Service.UI.Use use) {
		return use.package$() + ".box.I18n.dictionaries()";
	}

	private boolean isCustom(String value) {
		final boolean custom = value != null && value.startsWith("{");
		if (custom) konosParameters.add(value.substring(1, value.length() - 1));
		return custom;
	}

	private void parent(FrameBuilder builder) {
		if (parent() != null && configuration != null && configuration.model() != null && !configuration.model().level().isPlatform())
			builder.add("parent", parent()).add("hasParent", "");
		else builder.add("hasntParent", "");
	}

	private Template template() {
		return Formatters.customize(new AbstractBoxTemplate());
	}
}
