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
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.slackbot.SlackBotService;
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
		FrameBuilder builder = new FrameBuilder("box");
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

	private void dataLake(FrameBuilder root, String name) {
		if (graph.datalake() == null) return;
		final FrameBuilder frame = new FrameBuilder().add("package", packageName).add("configuration", name);
		frame.add("feeder", graph.feederList().stream().map(this::frameOf).toArray(Frame[]::new));
		if (!graph.procedureList().isEmpty())
			frame.add("procedure", new FrameBuilder().add("package", packageName).add("configuration", name).toFrame()); //TODO
		root.add("datalake", frame);
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
							.add("parameter", new FrameBuilder(isCustom(service.port()) ? "custom" : "standard").add("value", service.port())).toFrame());
	}

	private void jms(FrameBuilder frame, String name) {
		for (JMSService service : graph.jMSServiceList())
			frame.add("service", new FrameBuilder("service", "jms").add("name", service.name$()).add("configuration", name)
					.add("parameter", new FrameBuilder(isCustom(service.user()) ? "custom" : "standard").add("value", service.user()).toFrame())
					.add("parameter", new FrameBuilder(isCustom(service.password()) ? "custom" : "standard").add("value", service.password()).toFrame())
					.add("parameter", new FrameBuilder(isCustom(service.url()) ? "custom" : "standard").add("value", service.url()).toFrame()).toFrame());
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
				.add("parameter", new FrameBuilder(isCustom(service.port()) ? "custom" : "standard").add("value", service.port()).toFrame());
		if (service.authentication() != null)
			builder.add("authentication", new FrameBuilder(isCustom(service.authentication().by()) ? "custom" : "standard").add("value", service.authentication().by()).toFrame());
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
