package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.datalake.feeder.FeederRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Feeder;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.ness.NessClient;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.slackbot.SlackBotService;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

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
		customParameters = new HashSet<>();
	}

	public void execute() {
		FrameBuilder builder = new FrameBuilder().add("box");
		final String name = name();
		builder.add("name", name);
		builder.add("package", packageName);
		if (hasModel) builder.add("tara", name);
		parent(builder);
		services(builder, name);
		tasks(builder, name);
		dataLake(builder, name);
		Commons.writeFrame(gen, "AbstractBox", template().render(builder.toFrame()));
		notifyNewParameters();
	}

	private void notifyNewParameters() {
		new ParameterPublisher((LegioConfiguration) configuration).publish(customParameters);
	}

	private void tasks(FrameBuilder builder, String name) {
		if (!graph.taskList().isEmpty()) builder.add("task", new FrameBuilder("task").add("configuration", name).toFrame());
	}

	private void dataLake(FrameBuilder rootBuilder, String name) {
		if (!graph.nessClientList().isEmpty()) {
			final NessClient client = graph.nessClientList().get(0);
			final FrameBuilder datalake = new FrameBuilder("dataLake").
					add("mode", graph.nessClient(0).mode().name()).
					add("name", graph.nessClient(0).name$()).
					add("package", packageName).add("configuration", name);
			datalake.add("parameter", new FrameBuilder(isCustom(client.url()) ? "custom" : "standard").add("value", client.url()).toFrame());
			datalake.add("parameter", new FrameBuilder(isCustom(client.user()) ? "custom" : "standard").add("value", client.user()).toFrame());
			datalake.add("parameter", new FrameBuilder(isCustom(client.password()) ? "custom" : "standard").add("value", client.password()).toFrame());
			datalake.add("parameter", new FrameBuilder(isCustom(client.clientID()) ? "custom" : "standard").add("value", client.clientID()).toFrame());
			datalake.add("feeder", client.feederList().stream().map(this::frameOf).toArray(Frame[]::new));
			if (client.requireConnection()) datalake.add("requireConnection");
			if (!graph.procedureList().isEmpty())
				datalake.add("procedure", new FrameBuilder().add("package", packageName).add("configuration", name)); //TODO
			if (hasModel)
				datalake.add("nessOperations", new FrameBuilder().add("package", packageName).add("configuration", name));
			rootBuilder.add("dataLake", datalake);
		}
	}

	private Frame frameOf(Feeder feeder) {
		return new FrameBuilder("feeder").add("package", packageName).add("name", FeederRenderer.name(feeder)).add("box", name()).toFrame();
	}

	private void services(FrameBuilder builder, String name) {
		if (!graph.jMSServiceList().isEmpty()) builder.add("jms", "");
		for (RESTService service : graph.rESTServiceList()) {
			final FrameBuilder serviceFrame = new FrameBuilder("service").add("rest").add("name", service.name$()).add("configuration", name);
			serviceFrame.add("parameter", new FrameBuilder(isCustom(service.port()) ? "custom" : "standard").add("value", service.port()));
			builder.add("service", serviceFrame.toFrame());
		}
		for (JMSService service : graph.jMSServiceList()) {
			final FrameBuilder serviceFrame = new FrameBuilder("service").add("jms").add("name", service.name$()).add("configuration", name);
			serviceFrame.add("parameter", new FrameBuilder(isCustom(service.user()) ? "custom" : "standard").add("value", service.user()));
			serviceFrame.add("parameter", new FrameBuilder(isCustom(service.password()) ? "custom" : "standard").add("value", service.password()));
			serviceFrame.add("parameter", new FrameBuilder(isCustom(service.url()) ? "custom" : "standard").add("value", service.url()));
			builder.add("service", serviceFrame);
		}
		for (JMXService service : graph.jMXServiceList())
			builder.add("service", new FrameBuilder("service").add("jmx").add("name", service.name$()).add("configuration", name));
		for (SlackBotService service : graph.slackBotServiceList()) {
			final FrameBuilder slackFrame = new FrameBuilder("service").add("slack").add("name", service.name$()).add("configuration", name);
			slackFrame.add("parameter", new FrameBuilder(isCustom(service.token()) ? "custom" : "standard").add("value", service.token()));
			builder.add("service", slackFrame);
		}
		if (!graph.rESTServiceList().isEmpty() || !graph.uIServiceList().isEmpty()) builder.add("spark", "stop");
		if (!graph.uIServiceList().isEmpty()) {
			final FrameBuilder uiFrame = new FrameBuilder();
			if (parent != null) uiFrame.add("parent", parent);
			builder.add("hasUi", uiFrame);
			builder.add("uiAuthentication", uiFrame);
			builder.add("uiEdition", uiFrame);
			builder.add("service", graph.uIServiceList().stream().map(s -> uiServiceFrame(s, name)).toArray(Frame[]::new));
		}
	}

	private Frame uiServiceFrame(UIService service, String name) {
		final FrameBuilder builder = new FrameBuilder("service", "ui");
		builder.add("name", service.name$());
		builder.add("package", packageName);
		builder.add("configuration", name);
		builder.add("parameter", new FrameBuilder(isCustom(service.port()) ? "custom" : "standard").add("value", service.port()).toFrame());
		if (service.authentication() != null)
			builder.add("authentication", new FrameBuilder(isCustom(service.authentication().by()) ? "custom" : "standard").add("value", service.authentication().by()).toFrame());
		if (service.edition() != null)
			builder.add("edition", new FrameBuilder(isCustom(service.edition().by()) ? "custom" : "standard").add("value", service.edition().by()).toFrame());
		service.useList().forEach(use -> builder.add("use", use.className() + "Service"));
		return builder.toFrame();

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
		if (module != null) {
			final String dsl = configuration.outDSL();
			if (dsl == null || dsl.isEmpty()) return module.getName();
			else return dsl;
		} else return "System";
	}


	private Template template() {
		return Formatters.customize(new AbstractBoxTemplate());
	}
}
