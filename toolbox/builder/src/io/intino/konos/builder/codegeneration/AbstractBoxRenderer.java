package io.intino.konos.builder.codegeneration;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.jmsbus.JmsBusMessageHub;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.messaging.MessagingService;
import io.intino.konos.model.graph.realtime.RealtimeMounter;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.slackbot.SlackBotService;
import io.intino.konos.model.graph.sshmirrored.SshMirroredDatalake;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

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
			SshMirroredDatalake mirror = datalake.asSshMirrored();
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

	private void messageHub(FrameBuilder root, String boxName) {
		MessageHub hub = graph.messageHub();
		if (hub == null) return;
		FrameBuilder builder = new FrameBuilder("messagehub");
		if (hub.isJmsBus()) {
			JmsBusMessageHub jmsHub = hub.asJmsBus();
			builder.add("jms").add("parameter", parameter(jmsHub.url())).add("parameter", parameter(jmsHub.user())).add("parameter", parameter(jmsHub.password())).add("parameter", parameter(jmsHub.clientId()));
		}
		builder.add("package", packageName());
		Frame[] feederFrames = graph.feederList().stream().filter(f -> !f.sensorList().isEmpty()).map(this::frameOf).toArray(Frame[]::new);
		if (feederFrames.length != 0) builder.add("feeder", feederFrames);
		Frame[] mounterFrames = graph.mounterList().stream().filter(Mounter::isRealtime).map(m -> frameOf(m, boxName)).toArray(Frame[]::new);
		if (mounterFrames.length != 0) builder.add("mounter", mounterFrames);
		root.add("messagehub", builder.toFrame());
	}

	private void workflow(FrameBuilder root) {
		if (graph.workflow() == null || graph.workflow().processList().isEmpty()) return;
		root.add("workflow", buildBaseFrame().add("workflow"));
		konosParameters.add("workspace");
	}

	private Frame frameOf(Mounter m, String boxName) {
		RealtimeMounter mounter = m.asRealtime();
		FrameBuilder[] subscriptions = mounter.sourceList().stream().filter(s -> Objects.nonNull(s.channel())).map(RealtimeMounter.Source::channel).map(t ->
				new FrameBuilder("subscription").add("tankName", t).add("box", boxName).
						add("package", packageName()).add("name", mounter.name$()).add("subscriberId", mounter.subscriberId())).toArray(FrameBuilder[]::new);
		FrameBuilder mounterFrame = new FrameBuilder("mounter", "realtime");
		return mounterFrame.add("name", mounter.name$()).add("subscription", subscriptions).toFrame();
	}

	@NotNull
	private FrameBuilder parameter(String parameter, String... types) {
		return new FrameBuilder(types).add("parameter").add(isCustom(parameter) ? "custom" : "standard").add("value", parameter);
	}

	@NotNull
	private Frame parameter(SlackBotService service) {
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
		if (!graph.rESTServiceList().isEmpty() || !graph.uIServiceList().isEmpty()) builder.add("spark", "stop");
		ui(builder, name);
	}

	private void rest(FrameBuilder frame, String name) {
		for (RESTService service : graph.rESTServiceList())
			frame.add("service",
					new FrameBuilder("service", "rest")
							.add("name", service.name$())
							.add("configuration", name)
							.add("parameter", parameter(service.port())).toFrame());
	}

	private boolean hasAuthenticatedApis() {
		return graph.rESTServiceList().stream().anyMatch(restService -> restService.authenticatedWithToken() != null);
	}

	private void jms(FrameBuilder frame, String name) {
		for (MessagingService service : graph.messagingServiceList())
			frame.add("service", new FrameBuilder("service", "jms").add("name", service.name$()).add("configuration", name));
	}

	private void jmx(FrameBuilder frame, String name) {
		for (JMXService service : graph.jMXServiceList())
			frame.add("service", new FrameBuilder("service", "jmx").add("name", service.name$()).add("configuration", name).toFrame());
	}

	private void slackServices(FrameBuilder frame, String name) {
		for (SlackBotService service : graph.slackBotServiceList()) {
			frame.add("service", new FrameBuilder("service", "slack")
					.add("name", service.name$()).add("configuration", name)
					.add("parameter", parameter(service)).toFrame());
		}
	}

	private void ui(FrameBuilder builder, String name) {
		if (!graph.uIServiceList().isEmpty()) {
			final FrameBuilder uiFrame = new FrameBuilder();
			if (settings.parent() != null) uiFrame.add("parent", settings.parent());
			builder.add("hasUi", uiFrame);
			builder.add("uiAuthentication", uiFrame);
			builder.add("uiEdition", uiFrame);
			builder.add("service", graph.uIServiceList().stream().map(s -> ui(s, name)).toArray(Frame[]::new));
		}
	}

	private Frame ui(UIService service, String name) {
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
		if (parent() != null && configuration != null && !Platform.equals(configuration.level()))
			builder.add("parent", parent()).add("hasParent", "");
		else builder.add("hasntParent", "");
	}

	private Template template() {
		return Formatters.customize(new AbstractBoxTemplate());
	}
}
