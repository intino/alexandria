package io.intino.konos.builder.codegeneration;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Settings.DataHubManifest;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.*;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static io.intino.konos.builder.codegeneration.Formatters.firstLowerCase;


public class AbstractBoxRenderer extends Renderer {
	private final KonosGraph graph;
	private final Configuration configuration;
	private final boolean hasModel;
	private final Set<String> konosParameters;

	AbstractBoxRenderer(Settings settings, KonosGraph graph, boolean hasModel) {
		super(settings, Target.Owner);
		this.graph = graph;
		this.configuration = module() != null ? TaraUtil.configurationOf(module()) : null;
		this.hasModel = hasModel;
		this.konosParameters = new HashSet<>();
		this.konosParameters.add("workspace");
	}

	@Override
	public void render() {
		FrameBuilder root = new FrameBuilder("box");
		final String boxName = settings.boxName();
		root.add("name", boxName).add("package", packageName());
		if (hasModel) root.add("tara", boxName);
		parent(root);
		services(root, boxName);
		sentinels(root, boxName);
		datalake(root);
		messageHub(root, boxName);
		terminal(root, boxName);
		workflow(root);
		if (hasAuthenticatedApis()) root.add("authenticationValidator", new FrameBuilder().add("type", "Basic"));
		graph.datamartList().forEach(d -> datamart(root, d));
		Commons.writeFrame(settings.gen(Target.Owner), "AbstractBox", template().render(root.toFrame()));
		notifyNewParameters();
	}

	Set<String> customParameters() {
		return konosParameters;
	}

	private void datamart(FrameBuilder root, Datamart datamart) {
		root.add("datamart", new FrameBuilder("datamart").add("name", datamart.name$()).add("path", parameter(datamart.path())));
	}

	private void notifyNewParameters() {
		new ParameterPublisher((LegioConfiguration) configuration).publish(konosParameters);
	}

	private void sentinels(FrameBuilder builder, String boxName) {
		if (!graph.sentinelList().isEmpty())
			builder.add("sentinel", new FrameBuilder("sentinel").add("configuration", boxName).toFrame());
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

	private void terminal(FrameBuilder root, String boxName) {
		DataHubManifest manifest = settings.dataHubManifest();
		if (manifest == null) return;
		FrameBuilder builder = new FrameBuilder("terminal").
				add("name", manifest.terminal).
				add("qn", manifest.qn).
				add("package", packageName()).
				add("box", boxName());
		manifest.parameters.forEach(p -> builder.add("parameter", parameter(p, "custom", p.contains("directory") ? "file" : "standard")));
		Frame[] subscriber = graph.subscriberList().stream().map(s -> terminalFrameOf(s, manifest, boxName).toFrame()).toArray(Frame[]::new);
		if (subscriber.length != 0) builder.add("subscriber", subscriber);
		root.add("terminal", builder.toFrame());
	}

	private FrameBuilder terminalFrameOf(Subscriber subscriber, DataHubManifest manifest, String boxName) {
		String type = manifest.tankClasses.get(subscriber.channel());
		FrameBuilder builder = new FrameBuilder("subscriber", "terminal").
				add("package", packageName()).
				add("name", subscriber.name$()).
				add("box", boxName).
				add("terminal", manifest.qn).
				add("type", type).
				add("typeName", type.substring(type.lastIndexOf(".") + 1));
		if (subscriber.subscriberId() != null) builder.add("subscriberId", subscriber.subscriberId());
		if (manifest.messageContexts.get(type.substring(type.lastIndexOf(".") + 1)).size() > 1) {
			String context = subscriber.channel().substring(0, subscriber.channel().lastIndexOf(".")).replace(".", "-");
			builder.add("context",
					new FrameBuilder("context").
							add("value", firstLowerCase(Formatters.snakeCaseToCamelCase().format(context).toString())).
							add("type", type).toFrame());
		}
		return builder;
	}

	private void messageHub(FrameBuilder root, String boxName) {
		MessageHub hub = graph.messageHub();
		if (hub == null) return;
		FrameBuilder builder = new FrameBuilder("messagehub");
		if (hub.isJmsBus()) {
			MessageHub.JmsBus jmsHub = hub.asJmsBus();
			builder.add("jms").add("parameter", parameter(jmsHub.url())).add("parameter", parameter(jmsHub.user())).add("parameter", parameter(jmsHub.password())).add("parameter", parameter(jmsHub.clientId()));
		}
		builder.add("package", packageName());
		builder.add("box", boxName());
		Frame[] feederFrames = graph.feederList().stream().filter(f -> !f.sensorList().isEmpty()).map(this::frameOf).toArray(Frame[]::new);
		if (feederFrames.length != 0) builder.add("feeder", feederFrames);
		Frame[] mounterFrames = graph.subscriberList().stream().map(s -> frameOf(s, boxName).add("messageHub").toFrame()).toArray(Frame[]::new);
		if (mounterFrames.length != 0) builder.add("mounter", mounterFrames);
		root.add("messagehub", builder.toFrame());
	}

	private void workflow(FrameBuilder root) {
		if (graph.workflow() == null || graph.workflow().processList().isEmpty()) return;
		root.add("workflow", buildBaseFrame().add("workflow"));
		konosParameters.add("workspace");
	}

	private FrameBuilder frameOf(Subscriber subscriber, String boxName) {
		FrameBuilder builder = new FrameBuilder("subscriber").add("package", packageName()).add("name", subscriber.name$()).
				add("source", subscriber.channel());
		if (subscriber.subscriberId() != null) builder.add("subscriberId", subscriber.subscriberId());
		return builder.add("box", boxName);
	}

	@NotNull
	private FrameBuilder parameter(String parameter, String... types) {
		return new FrameBuilder(types).add("parameter").add(isCustom(parameter) ? "custom" : "standard").add("value", parameter);
	}

	@NotNull
	private Frame parameter(Service.SlackBot service) {
		return new FrameBuilder(isCustom(service.token()) ? "custom" : "standard").add("value", service.token()).toFrame();
	}

	private Frame frameOf(Feeder feeder) {
		return new FrameBuilder("feeder").add("package", packageName()).add("name", feeder.name$()).add("box", settings.boxName()).toFrame();
	}

	private void services(FrameBuilder builder, String name) {
		if (!graph.messagingServiceList().isEmpty()) builder.add("jms", "");
		rest(builder, name);
		jms(builder, name);
		jmx(builder, name);
		slackServices(builder, name);
		if (!graph.restServiceList().isEmpty() || !graph.uiServiceList().isEmpty()) builder.add("spark", "stop");
		ui(builder, name);
	}

	private void rest(FrameBuilder frame, String name) {
		for (Service.REST service : graph.restServiceList())
			frame.add("service",
					new FrameBuilder("service", "rest")
							.add("name", service.name$())
							.add("configuration", name)
							.add("parameter", parameter(service.port())).toFrame());
	}

	private boolean hasAuthenticatedApis() {
		return graph.restServiceList().stream().anyMatch(restService -> restService.authenticatedWithToken() != null);
	}

	private void jms(FrameBuilder frame, String name) {
		for (Service.Messaging service : graph.messagingServiceList())
			frame.add("service", new FrameBuilder("service", "jms").add("name", service.name$()).add("configuration", name));
	}

	private void jmx(FrameBuilder frame, String name) {
		for (Service.JMX service : graph.jmxServiceList())
			frame.add("service", new FrameBuilder("service", "jmx").add("name", service.name$()).add("configuration", name).toFrame());
	}

	private void slackServices(FrameBuilder frame, String name) {
		for (Service.SlackBot service : graph.slackBotServiceList()) {
			frame.add("service", new FrameBuilder("service", "slack")
					.add("name", service.name$()).add("configuration", name)
					.add("parameter", parameter(service)).toFrame());
		}
	}

	private void ui(FrameBuilder builder, String name) {
		if (!graph.uiServiceList().isEmpty()) {
			final FrameBuilder uiFrame = new FrameBuilder();
			if (settings.parent() != null) uiFrame.add("parent", settings.parent());
			builder.add("hasUi", uiFrame);
			builder.add("uiAuthentication", uiFrame);
			builder.add("uiEdition", uiFrame);
			builder.add("service", graph.uiServiceList().stream().map(s -> ui(s, name)).toArray(Frame[]::new));
		}
	}

	private Frame ui(Service.UI service, String name) {
		final FrameBuilder builder = new FrameBuilder("service", "ui").add("name", service.name$()).add("configuration", name)
				.add("parameter", parameter(service.port()).toFrame()).add("package", packageName());
		if (service.authentication() != null)
			builder.add("authentication", parameter(service.authentication().by()).toFrame());
		if (service.edition() != null)
			builder.add("edition", new FrameBuilder(isCustom(service.edition().by()) ? "custom" : "standard").add("value", service.edition().by()).toFrame());
		service.useList().forEach(use -> builder.add("use", use.className() + "Service"));
		return builder.toFrame();
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
