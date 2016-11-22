package io.intino.pandora.plugin.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import cottons.utils.Files;
import io.intino.pandora.plugin.codegeneration.exception.ExceptionRenderer;
import io.intino.pandora.plugin.codegeneration.schema.SchemaRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.channel.ChannelRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.channel.SubscriptionModelRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.service.JMSRequestRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.service.JMSServiceRenderer;
import io.intino.pandora.plugin.codegeneration.server.jmx.JMXOperationsServiceRenderer;
import io.intino.pandora.plugin.codegeneration.server.jmx.JMXServerRenderer;
import io.intino.pandora.plugin.codegeneration.server.rest.RESTResourceRenderer;
import io.intino.pandora.plugin.codegeneration.server.rest.RESTServiceRenderer;
import io.intino.pandora.plugin.codegeneration.server.slack.SlackRenderer;
import io.intino.pandora.plugin.codegeneration.server.task.TaskRenderer;
import io.intino.pandora.plugin.codegeneration.server.task.TaskerRenderer;
import io.intino.pandora.plugin.codegeneration.server.ui.display.DisplayRenderer;
import io.intino.pandora.plugin.codegeneration.server.ui.web.ActivityRenderer;
import io.intino.pandora.plugin.codegeneration.server.ui.web.ResourceRenderer;
import org.jetbrains.annotations.Nullable;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;

import java.io.File;

public class FullRenderer {

	@Nullable
	private final Project project;
	@Nullable
	private final Module module;
	private final Graph graph;
	private final File gen;
	private final File src;
	private final String packageName;
	private final String boxName;

	public FullRenderer(@Nullable Module module, Graph graph, File src, File gen, String packageName) {
		this.project = module == null ? null : module.getProject();
		this.module = module;
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName();
	}

	public void execute() {
		Files.removeDir(gen);
		schemas();
		exceptions();
		rest();
		scheduling();
		jmx();
		jms();
		channels();
		slack();
		ui();
		box();
		main();
	}

	private void schemas() {
		new SchemaRenderer(graph, gen, packageName).execute();
	}

	private void exceptions() {
		new ExceptionRenderer(graph, gen, packageName).execute();
	}

	private void rest() {
		new RESTResourceRenderer(project, graph, src, gen, packageName, boxName).execute();
		new RESTServiceRenderer(graph, gen, packageName, boxName).execute();
	}

	private void jmx() {
		new JMXOperationsServiceRenderer(project, graph, src, gen, packageName, boxName).execute();
		new JMXServerRenderer(graph, gen, packageName, boxName).execute();
	}

	private void jms() {
		new JMSRequestRenderer(project, graph, src, gen, packageName, boxName).execute();
		new JMSServiceRenderer(graph, gen, packageName, boxName).execute();
	}

	private void scheduling() {
		new TaskRenderer(project, graph, src, gen, packageName, boxName).execute();
		new TaskerRenderer(graph, gen, packageName, boxName).execute();
	}

	private void channels() {
		new SubscriptionModelRenderer(project, graph, src, gen, packageName, boxName).execute();
		new ChannelRenderer(graph, gen, packageName, boxName).execute();
	}

	private void slack() {
		new SlackRenderer(graph, src, packageName, boxName).execute();
	}

	private void ui() {
		new DisplayRenderer(graph, src, gen, packageName, boxName).execute();
		new ResourceRenderer(project, graph, src, gen, packageName, boxName).execute();
		new ActivityRenderer(graph, gen, packageName, boxName).execute();
	}

	private void box() {
		new BoxRenderer(graph, gen, packageName, module).execute();
		new BoxConfigurationRenderer(graph, gen, packageName, module).execute();
	}

	private String boxName() {
		if (module != null) {
			final Configuration configuration = TaraUtil.configurationOf(module);
			if (configuration == null) return "";
			final String dsl = configuration.outDSL();
			if (dsl == null || dsl.isEmpty()) return module.getName();
			else return dsl;
		} else return "System";
	}

	private void main() {
		new MainRenderer(graph, src, packageName, module).execute();
	}
}
