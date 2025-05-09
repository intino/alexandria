package io.intino.konos.builder.codegeneration;

import io.intino.builder.CompilerConfiguration;
import io.intino.builder.PostCompileConfigurationParameterActionMessage;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.CompilationContext.DataHubManifest;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Sentinel;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Subscriber;
import io.intino.magritte.framework.Node;

import java.util.*;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.dsl.Subscriber.Durable.SubscriptionMode.ReceiveAfterLastSeal;

public class AbstractBoxRenderer extends Renderer {
	private final KonosGraph graph;
	private final CompilerConfiguration configuration;
	private final Set<String> konosParameters;

	AbstractBoxRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
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
		terminal(root);
		workflow(root);
		Commons.writeFrame(context.gen(Target.Service), "AbstractBox", new AbstractBoxTemplate().render(root.toFrame(), Formatters.all));
		context.compiledFiles().add(new OutputItem(sourceFileOf(graph), javaFile(gen(Target.Service), "AbstractBox").getAbsolutePath()));
		notifyNewParameters();
	}

	private String sourceFileOf(KonosGraph graph) {
		if (!graph.serviceList().isEmpty()) return context.sourceFileOf(graph.serviceList().get(0));
		if (!graph.schemaList().isEmpty()) return context.sourceFileOf(graph.schemaList().get(0));
		if (!graph.datamartList().isEmpty()) return context.sourceFileOf(graph.datamartList().get(0));
		final List<Node> nodes = graph.core$().rootList();
		return nodes.isEmpty() ? "" : context.sourceFileOf(nodes.get(0));
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
		konosParameters.addAll(graph.sentinelList().stream()
				.filter(Sentinel::isFileListener)
				.map(s -> s.asFileListener().file())
				.map(Commons::extractParameters)
				.flatMap(Collection::stream).toList());
	}


	private void terminal(FrameBuilder root) {
		DataHubManifest manifest = context.dataHubManifest();
		if (manifest == null) return;
		FrameBuilder builder = new FrameBuilder("terminal").
				add("name", manifest.terminal).
				add("qn", manifest.qn).
				add("package", packageName()).
				add("box", boxName());
		if (!manifest.datamartsAutoLoad) {
			builder.add("datamartsLoad");
			builder.add("datamartsLoad", "datamartsSourceSelector");
		}
		Frame[] subscriber = graph.subscriberList().stream().filter(s -> manifest.tankClasses.containsKey(s.event())).map(s -> subscriberFrameOf(s, manifest)).toArray(Frame[]::new);
		if (subscriber.length != 0) builder.add("subscriber", subscriber);
		root.add("terminal", builder.toFrame());
	}

	private Frame subscriberFrameOf(Subscriber subscriber, DataHubManifest manifest) {
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
		return builder.toFrame();
	}

	private void connector(FrameBuilder root) {
		if (graph.messagingServiceList().isEmpty() && context.dataHubManifest() == null) return;
		List<String> parameters = Objects.requireNonNullElseGet(context.dataHubManifest().connectionParameters, () -> Arrays.asList("datahub_url", "datahub_user", "datahub_password", "datahub_clientId", "keystore_file",
				"truststore_file", "keystore_password", "truststore_password"));
		List<String> additionalParameters = Objects.requireNonNullElseGet(context.dataHubManifest().additionalParameters, () -> List.of("datahub_outbox_directory"));
		FrameBuilder builder = new FrameBuilder("connector");
		for (String p : parameters)
			builder.add("parameter", parameter(p, "conf", p.contains("directory") || p.contains("file") ? "file" : "standard"));
		for (String p : additionalParameters)
			builder.add("additionalParameter", parameter(p, "conf", p.contains("directory") || p.contains("file") ? "file" : "standard"));
		builder.add("package", packageName());
		builder.add("box", boxName());
		root.add("connector", builder.toFrame());
		konosParameters.addAll(parameters);
		konosParameters.addAll(additionalParameters);
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
		cli(builder);
		if (!graph.soapServiceList().isEmpty() || !graph.restServiceList().isEmpty() || !graph.uiServiceList().isEmpty())
			builder.add("server", "stop");
	}

	private void rest(FrameBuilder frame) {
		List<Service.REST> services = graph.restServiceList();
		services.sort(Comparator.<Service.REST>comparingInt(r -> r.notificationList().size()).reversed());
		for (Service.REST service : services)
			frame.add("service",
					new FrameBuilder("service", "rest")
							.add("name", service.name$())
							.add("configuration", boxName())
							.add("port", parameter(service.port(), "port"))
							.add("maxResourceSize", parameter(String.valueOf(service.maxResourceSize()*1024*1024), "maxResourceSize"))
							.toFrame());
	}

	private void cli(FrameBuilder frame) {
		List<Service.CLI> services = graph.cliServiceList();
		for (Service.CLI service : services)
			frame.add("service",
					new FrameBuilder("service", "cli")
							.add("name", service.name$())
							.add("configuration", boxName())
							.add("port", parameter(service.port(), "port"))
							.add("maxResourceSize", parameter(String.valueOf(service.maxResourceSize()*1024*1024), "maxResourceSize"))
							.toFrame());
	}

	private void soap(FrameBuilder frame) {
		for (Service.Soap service : graph.soapServiceList())
			frame.add("service",
					new FrameBuilder("service", "soap")
							.add("name", service.name$())
							.add("configuration", boxName())
							.add("port", parameter(service.port(), "port"))
							.add("maxResourceSize", parameter(String.valueOf(service.maxResourceSize()*1024*1024), "maxResourceSize"))
							.toFrame());
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
		final FrameBuilder builder = new FrameBuilder("service", "ui")
				.add("name", service.name$())
				.add("configuration", name)
				.add("port", parameter(service.port(), "port"))
				.add("maxResourceSize", parameter(String.valueOf(service.maxResourceSize()*1024*1024), "maxResourceSize"))
				.add("package", packageName());
		if (service.authentication() != null)
			builder.add("authentication", parameter(service.authentication().by()).toFrame());
		if (service.edition() != null)
			builder.add("edition", new FrameBuilder(isCustom(service.edition().by()) ? "custom" : "standard").add("value", service.edition().by()).toFrame());
		service.useList().forEach(use -> builder.add("use", serviceNameOf(use)));
		service.importList().forEach($import -> builder.add("importLibrary", importFrameOf($import)));
		return builder.toFrame();
	}

	private FrameBuilder importFrameOf(Service.UI.Import $import) {
		FrameBuilder result = buildBaseFrame().add("importLibrary");
		result.add("name", $import.name$());
		result.add("libraryName", parameter($import.name()));
		result.add("basePackage", parameter($import.package$()));
		result.add("libraryFile", parameter($import.libraryFile()));
		result.add("destination", System.getProperty("java.io.tmpdir"));
		return result;
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
		if (parent() != null && configuration != null && configuration.dsl() != null && !configuration.dsl().level().isMetaMetaModel())
			builder.add("parent", parent()).add("hasParent", "");
		else builder.add("hasntParent", "");
	}

}
