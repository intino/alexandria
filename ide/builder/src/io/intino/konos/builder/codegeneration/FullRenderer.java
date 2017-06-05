package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import cottons.utils.Files;
import io.intino.konos.builder.codegeneration.accessor.ui.ActivityAccessorCreator;
import io.intino.konos.builder.codegeneration.datalake.MessageHandlerRenderer;
import io.intino.konos.builder.codegeneration.datalake.NessEventsRenderer;
import io.intino.konos.builder.codegeneration.exception.ExceptionRenderer;
import io.intino.konos.builder.codegeneration.main.MainRenderer;
import io.intino.konos.builder.codegeneration.schema.SchemaRenderer;
import io.intino.konos.builder.codegeneration.server.activity.SchemaAdaptersRenderer;
import io.intino.konos.builder.codegeneration.server.activity.dialog.DialogRenderer;
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

import java.io.File;
import java.util.List;

public class FullRenderer {

	@Nullable
	private final Project project;
	@Nullable
	private final Module module;
	private final Graph graph;
	private final File gen;
	private final File src;
	private File res;
	private final String packageName;
	private final String boxName;
	private final String parent;
	private final boolean hasModel;

	public FullRenderer(@Nullable Module module, Graph graph, File src, File gen, File res, String packageName) {
		this.project = module == null ? null : module.getProject();
		this.module = module;
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.res = res;
		this.packageName = packageName;
		this.parent = parent();
		this.hasModel = hasModel();
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
		new NessEventsRenderer(graph, gen, packageName, boxName).execute();
		new MessageHandlerRenderer(graph, src, packageName, boxName).execute();
	}

	private void slack() {
		new SlackRenderer(project, graph, src, gen, packageName, boxName).execute();
	}

	private void ui() {
		new DisplayRenderer(project, graph, src, gen, packageName, boxName).execute();
		new DialogRenderer(project, graph, src, gen, packageName, boxName).execute();
		new ResourceRenderer(project, graph, src, gen, packageName, boxName).execute();
		new ActivityRenderer(graph, src, gen, packageName, boxName).execute();
		new ActivityAccessorCreator(module, graph).execute();
		new SchemaAdaptersRenderer(graph, gen, packageName).execute();
	}

	private void box() {
		new AbstractBoxRenderer(graph, gen, packageName, module, parent, hasModel).execute();
		new BoxRenderer(src, packageName, module, hasModel).execute();
		new BoxConfigurationRenderer(graph, gen, packageName, module, parent, hasModel).execute();
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
			final List<? extends Configuration.LanguageLibrary> languages = configuration.languages();
			if (languages.isEmpty() || languages.get(0).generationPackage() == null) return null;
			final String workingPackage = languages.get(0).generationPackage().replace(".graph", "");
			if (workingPackage != null && facade.findClass(workingPackage + ".box." + Formatters.firstUpperCase(languages.get(0).name()) + "Box", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)) != null)
				return workingPackage.toLowerCase() + ".box." + Formatters.firstUpperCase(languages.get(0).name());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	private boolean hasModel() {
		return module != null && TaraUtil.configurationOf(module) != null && hasModel(TaraUtil.configurationOf(module));
	}

	private boolean hasModel(Configuration configuration) {
		return !configuration.languages().isEmpty();
	}

	private void main() {
		new MainRenderer(src, packageName, module).execute();
	}
}
