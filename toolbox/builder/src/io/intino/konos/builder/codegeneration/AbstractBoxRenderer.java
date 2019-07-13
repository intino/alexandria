package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.datahub.feeder.FeederRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.mirrored.MirroredDataHub;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.slackbot.SlackBotService;
import io.intino.konos.model.graph.standalone.StandAloneDataHub;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class AbstractBoxRenderer {
	private final File gen;
	private final String packageName;
	private final Module module;
	private final KonosGraph graph;
	private final Configuration configuration;
	private final String parent;
	private final boolean hasModel;
	private final Set<String> customParameters;

	AbstractBoxRenderer(KonosGraph graph, File gen, String packageName, Module module, String parent, boolean hasModel) {
		this.graph = graph;
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		this.configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.parent = parent;
		this.hasModel = hasModel;
		this.customParameters = new HashSet<>();
	}

	public void execute() {
		FrameBuilder root = new FrameBuilder("box");
		final String boxName = name();
		root.add("name", boxName).add("package", packageName);
		if (hasModel) root.add("tara", boxName);
		parent(root);
		services(root, boxName);
		tasks(root, boxName);
		dataHub(root, boxName);
		graph.datamartList().forEach(d -> datamart(root, d));
		Commons.writeFrame(gen, "AbstractBox", template().render(root.toFrame()));
		notifyNewParameters();
	}

	private void datamart(FrameBuilder root, Datamart datamart) {
		root.add("datamart", new FrameBuilder("datamart").add("name", datamart.name$()).add("value", datamart.path()));
	}

	private void notifyNewParameters() {
		new ParameterPublisher((LegioConfiguration) configuration).publish(customParameters);
	}

	private void tasks(FrameBuilder builder, String boxName) {
		if (!graph.taskList().isEmpty()) builder.add("task", new FrameBuilder("task").add("configuration", boxName).toFrame());
	}

	private void dataHub(FrameBuilder builder, String boxName) {
		DataHub dataHub = graph.dataHub();
		if (dataHub == null) return;
		final FrameBuilder dataHubFrame = new FrameBuilder("datahub", type(dataHub)).add("workspace", parameter(dataHub.workingDirectory()));
		if (!dataHub.tankList().isEmpty()) {
			FrameBuilder tanks = new FrameBuilder("tanks");
			for (DataHub.Split split : dataHub.splitList())
				tanks.add("split", frameOf(split));
			for (DataHub.Tank tank : dataHub.tankList())
				tanks.add("tank", frameOf(tank));
			dataHubFrame.add("tanks", tanks.toFrame());
		}
		if (dataHub.isStandAlone()) {
			StandAloneDataHub service = dataHub.asStandAlone();
			if (service.broker() != null) addBroker(dataHubFrame, service.broker());
			FrameBuilder frame = new FrameBuilder("standalone").add("path", parameter(service.datalakePath())).add("scale", service.scale());
			if (service.seal() != null) frame.add("sealing", service.seal().when());
			dataHubFrame.add("datasource", frame);
		} else if (dataHub.isMirrored()) mirroredDataSource(dataHub, dataHubFrame);
		else if (dataHub.isRemote()) remoteDataSource(dataHub, dataHubFrame);
		else if (dataHub.isLocal())
			dataHubFrame.add("datasource", new FrameBuilder("local").add("path", parameter(dataHub.asLocal().datalakePath())));
		Frame[] feederFrames = graph.dataHub().feederList().stream().filter(f -> !f.sensorList().isEmpty()).map(this::frameOf).toArray(Frame[]::new);
		if (feederFrames.length != 0) dataHubFrame.add("feeder", feederFrames);
		builder.add("dataHub", dataHubFrame.toFrame());
	}

	private void remoteDataSource(DataHub dataHub, FrameBuilder dataHubFrame) {
		FrameBuilder remote = new FrameBuilder("remote");
		if (dataHub.asRemote().messageHub() != null) remote.add("messageHub", messageHub());
		dataHubFrame.add("datasource", remote);
	}

	private void mirroredDataSource(DataHub dataHub, FrameBuilder dataHubFrame) {
		MirroredDataHub mirrored = dataHub.asMirrored();
		if (mirrored.broker() != null) addBroker(dataHubFrame, mirrored.broker());
		FrameBuilder mirror = new FrameBuilder("mirror").
				add("originUrl", parameter(mirrored.originSshUrl())).
				add("originPath", parameter(mirrored.originDatalakePath())).
				add("startingTimetag", mirrored.startingTimetag()).
				add("user", parameter(mirrored.user())).
				add("password", mirrored.password() == null ? "" : parameter(mirrored.password())).
				add("destinationPath", parameter(mirrored.destinationPath()));
		if (mirrored.messageHub() != null) mirror.add("messageHub", messageHub());
		dataHubFrame.add("datasource", mirror);
	}

	private Frame frameOf(DataHub.Split split) {
		return new FrameBuilder("split").add("name", split.name$()).add("value", split.splits().toArray(new String[0])).toFrame();
	}

	private Frame frameOf(DataHub.Tank tank) {
		String[] objects = tank.core$().conceptList().stream().map(s -> s.id().split("#")[0]).toArray(String[]::new);
		FrameBuilder builder = new FrameBuilder(objects).add("tank").add("name", tank.fullName()).add("type", objects[0]);
		if (tank.isSet() && tank.asSet().split() != null) builder.add("splitName", tank.asSet().split().name$());
		return builder.toFrame();
	}

	@NotNull
	private FrameBuilder messageHub() {
		return new FrameBuilder("messageHub").add("package", packageName);
	}

	private void addBroker(FrameBuilder dataHubFrame, Broker broker) {
		FrameBuilder brokerFrame = new FrameBuilder().
				add("port", parameter(broker.port() + "", "int")).
				add("mqtt_port", parameter(broker.mqtt_port() + "", "int")).
				add("connectorId", parameter(broker.connectorId()));
		broker.pipeList().forEach(pipe -> brokerFrame.add("pipe", new FrameBuilder().add("origin", pipe.origin()).add("destination", pipe.destination())));
		broker.userList().forEach(user -> brokerFrame.add("user", new FrameBuilder().add("name", parameter(user.name())).add("password", parameter(user.password()))));
		dataHubFrame.add("broker", brokerFrame);
	}

	private String type(DataHub dataHub) {
		return dataHub.core$().conceptList().stream().map(s -> s.id().split("#")[0]).toArray(String[]::new)[0];
	}

	@NotNull
	private FrameBuilder parameter(String parameter, String... types) {
		return new FrameBuilder(types).add("parameter").add(isCustom(parameter) ? "custom" : "standard").add("value", parameter);
	}

	private Frame frameOf(Feeder feeder) {
		return new FrameBuilder("feeder").add("package", packageName).add("name", FeederRenderer.name(feeder)).add("box", name()).toFrame();
	}

	private void services(FrameBuilder builder, String name) {
		if (!graph.jMSServiceList().isEmpty()) builder.add("jms", "");
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

	private void jms(FrameBuilder frame, String name) {
		for (JMSService service : graph.jMSServiceList())
			frame.add("service", new FrameBuilder("service", "jms").add("name", service.name$()).add("configuration", name)
					.add("parameter", parameter(service.user()).toFrame())
					.add("parameter", parameter(service.password()).toFrame())
					.add("parameter", parameter(service.url()).toFrame()).toFrame());
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
			if (parent != null) uiFrame.add("parent", parent);
			builder.add("hasUi", uiFrame);
			builder.add("uiAuthentication", uiFrame);
			builder.add("uiEdition", uiFrame);
			builder.add("service", graph.uIServiceList().stream().map(s -> ui(s, name)).toArray(Frame[]::new));
		}
	}

	private Frame ui(UIService service, String name) {
		final FrameBuilder builder = new FrameBuilder("service", "ui").add("name", service.name$()).add("configuration", name)
				.add("parameter", parameter(service.port()).toFrame());
		if (service.authentication() != null)
			builder.add("authentication", parameter(service.authentication().by()).toFrame());
		if (service.edition() != null)
			builder.add("edition", new FrameBuilder(isCustom(service.edition().by()) ? "custom" : "standard").add("value", service.edition().by()).toFrame());
		service.useList().forEach(use -> builder.add("use", use.className() + "Service"));
		return builder.toFrame();

	}

	@NotNull
	private Frame parameter(SlackBotService service) {
		return new FrameBuilder(isCustom(service.token()) ? "custom" : "standard").add("value", service.token()).toFrame();
	}

	private boolean isCustom(String value) {
		final boolean custom = value != null && value.startsWith("{");
		if (custom) customParameters.add(value.substring(1, value.length() - 1));
		return custom;
	}

	private void parent(FrameBuilder builder) {
		if (parent != null && configuration != null && !Platform.equals(configuration.level()))
			builder.add("parent", parent).add("hasParent", "");
		else builder.add("hasntParent", "");
	}

	private String name() {
		return module != null ? configuration.artifactId() : Configuration.Level.Solution.name();
	}


	private Template template() {
		return Formatters.customize(new AbstractBoxTemplate());
	}
}
