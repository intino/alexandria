package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import cottons.utils.Files;
import io.intino.konos.builder.codegeneration.accessor.ui.ActivityAccessorCreator;
import io.intino.konos.builder.codegeneration.eventhandling.BusRenderer;
import io.intino.konos.builder.codegeneration.eventhandling.EventHandlerRenderer;
import io.intino.konos.builder.codegeneration.exception.ExceptionRenderer;
import io.intino.konos.builder.codegeneration.main.LauncherRenderer;
import io.intino.konos.builder.codegeneration.main.MainRenderer;
import io.intino.konos.builder.codegeneration.main.SetupRenderer;
import io.intino.konos.builder.codegeneration.schema.SchemaRenderer;
import io.intino.konos.builder.codegeneration.server.activity.SchemaAdaptersRenderer;
import io.intino.konos.builder.codegeneration.server.activity.display.DisplayRenderer;
import io.intino.konos.builder.codegeneration.server.activity.web.ActivityRenderer;
import io.intino.konos.builder.codegeneration.server.activity.web.ResourceRenderer;
import io.intino.konos.builder.codegeneration.server.jms.service.JMSRequestRenderer;
import io.intino.konos.builder.codegeneration.server.jms.service.JMSServiceRenderer;
import io.intino.konos.builder.codegeneration.server.jmx.JMXOperationsServiceRenderer;
import io.intino.konos.builder.codegeneration.server.jmx.JMXServerRenderer;
import io.intino.konos.builder.codegeneration.server.rest.RESTResourceRenderer;
import io.intino.konos.builder.codegeneration.server.rest.RESTServiceRenderer;
import io.intino.konos.builder.codegeneration.server.slack.SlackRenderer;
import io.intino.konos.builder.codegeneration.task.TaskRenderer;
import io.intino.konos.builder.codegeneration.task.TaskerRenderer;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.magritte.Graph;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.Nullable;
import org.siani.itrules.model.Frame;

import java.io.File;

public class FullRenderer {

	@Nullable
	private final Project project;
	@Nullable
	private final Module module;
	private final Graph graph;
	private final File gen;
	private final File src;
	private File res;
	private File test;
	private final String packageName;
	private final String boxName;
	private final String parent;
	private final boolean isTara;

	public FullRenderer(@Nullable Module module, Graph graph, File src, File gen, File res, File test, String packageName) {
		this.project = module == null ? null : module.getProject();
		this.module = module;
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.res = res;
		this.test = test;
		this.packageName = packageName;
		this.parent = parent();
		this.isTara = isTara();
		this.boxName = boxName();
	}

	public void execute() {
		Files.removeDir(gen);
		schemas();
		exceptions();
		rest();
		tasks();
		jmx();
		jms();
		bus();
		slack();
		ui();
		main(box());
	}

	private void schemas() {
		new SchemaRenderer(graph, gen, packageName).execute();
	}

	private void exceptions() {
		new ExceptionRenderer(graph, gen, packageName).execute();
	}

	private void rest() {
		new RESTResourceRenderer(project, graph, src, gen, packageName, boxName).execute();
		new RESTServiceRenderer(graph, gen, res, packageName, boxName).execute();
	}

	private void jmx() {
		new JMXOperationsServiceRenderer(project, graph, src, gen, packageName, boxName).execute();
		new JMXServerRenderer(graph, gen, packageName, boxName).execute();
	}

	private void jms() {
		new JMSRequestRenderer(project, graph, src, gen, packageName, boxName).execute();
		new JMSServiceRenderer(graph, gen, packageName, boxName).execute();
	}

	private void tasks() {
		new TaskRenderer(project, graph, src, gen, packageName, boxName).execute();
		new TaskerRenderer(graph, gen, packageName, boxName).execute();
	}

	private void bus() {
		new BusRenderer(graph, gen, packageName, boxName).execute();
		new EventHandlerRenderer(graph, src, packageName, boxName).execute();
	}

	private void slack() {
		new SlackRenderer(project, graph, src, gen, packageName, boxName).execute();
	}

	private void ui() {
		new DisplayRenderer(project, graph, src, gen, packageName, boxName).execute();
		new ResourceRenderer(project, graph, src, gen, packageName, boxName).execute();
		new ActivityRenderer(graph, gen, packageName, boxName).execute();
		new ActivityAccessorCreator(module, graph).execute();
		new SchemaAdaptersRenderer(graph, gen, packageName).execute();
	}

	private Frame box() {
		new BoxRenderer(graph, gen, packageName, module, parent, isTara).execute();
		return new BoxConfigurationRenderer(graph, gen, packageName, module, parent, isTara).execute();
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

	private String parent() {
		try {
			if (module == null) return null;
			final JavaPsiFacade facade = JavaPsiFacade.getInstance(module.getProject());
			final Configuration configuration = TaraUtil.configurationOf(module);
			for (Configuration.LanguageLibrary languageLibrary : configuration.languages()) {
				String workingPackage = languageLibrary.generationPackage();
				if (workingPackage != null && facade.findClass(workingPackage + ".konos." + Formatters.firstUpperCase(languageLibrary.name()) + "Box", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)) != null)
					return workingPackage.toLowerCase() + ".konos." + Formatters.firstUpperCase(languageLibrary.name());

			}
		} catch (Exception ignored) {
		}
		return null;

	}

	private boolean isTara() {
		return module != null && TaraUtil.configurationOf(module) != null && hasModel(TaraUtil.configurationOf(module));
	}

	private boolean hasModel(Configuration configuration) {
		return !configuration.languages().isEmpty();
	}

	private void main(Frame frame) {
		new SetupRenderer(src, packageName, module, isTara).execute();
		new MainRenderer(gen, packageName, module, isTara).execute();
		new LauncherRenderer(test, frame).execute();
	}
}
