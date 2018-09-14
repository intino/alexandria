package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import cottons.utils.Files;
import io.intino.konos.builder.codegeneration.accessor.ui.UIAccessorCreator;
import io.intino.konos.builder.codegeneration.datalake.NessJMXOperationsRenderer;
import io.intino.konos.builder.codegeneration.datalake.TanksConnectorsRenderer;
import io.intino.konos.builder.codegeneration.datalake.feeder.FeederRenderer;
import io.intino.konos.builder.codegeneration.datalake.mounter.MounterRenderer;
import io.intino.konos.builder.codegeneration.datalake.process.ProcessRenderer;
import io.intino.konos.builder.codegeneration.exception.ExceptionRenderer;
import io.intino.konos.builder.codegeneration.main.MainRenderer;
import io.intino.konos.builder.codegeneration.schema.SchemaRenderer;
import io.intino.konos.builder.codegeneration.services.jms.JMSRequestRenderer;
import io.intino.konos.builder.codegeneration.services.jms.JMSServiceRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXOperationsServiceRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXServerRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTResourceRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTServiceRenderer;
import io.intino.konos.builder.codegeneration.services.slack.SlackRenderer;
import io.intino.konos.builder.codegeneration.services.ui.UIResourcesRenderer;
import io.intino.konos.builder.codegeneration.services.ui.dialog.DialogRenderer;
import io.intino.konos.builder.codegeneration.services.ui.dialog.DialogsRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.DisplayRenderer;
import io.intino.konos.builder.codegeneration.services.ui.display.DisplaysRenderer;
import io.intino.konos.builder.codegeneration.services.ui.resource.ResourceRenderer;
import io.intino.konos.builder.codegeneration.task.TaskRenderer;
import io.intino.konos.builder.codegeneration.task.TaskerRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.plugin.codeinsight.linemarkers.InterfaceToJavaImplementation;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.plugin.project.Safe.safe;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.configurationOf;

public class FullRenderer {
	@Nullable
	private final Project project;
	@Nullable
	private final Module module;
	private final KonosGraph graph;
	private final File gen;
	private final File src;
	private File res;
	private final String packageName;
	private final String boxName;
	private final String parent;
	private final boolean hasModel;
	private final Map<String, String> classes;

	public FullRenderer(@Nullable Module module, KonosGraph graph, File src, File gen, File res, String packageName) {
		this.project = module == null ? null : module.getProject();
		this.module = module;
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.res = res;
		this.packageName = packageName;
		this.parent = parent();
		this.hasModel = hasModel();
		this.boxName = snakeCaseToCamelCase(boxName());
		this.classes = new HashMap<>();
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
		InterfaceToJavaImplementation.nodeMap.clear();
		InterfaceToJavaImplementation.nodeMap.putAll(classes);
	}

	private void schemas() {
		new SchemaRenderer(graph, gen, packageName, classes).execute();
	}

	private void exceptions() {
		new ExceptionRenderer(graph, gen, packageName).execute();
	}

	private void rest() {
		new RESTResourceRenderer(project, graph, src, gen, packageName, boxName, classes).execute();
		new RESTServiceRenderer(graph, gen, res, packageName, boxName, classes).execute();
	}

	private void jmx() {
		new JMXOperationsServiceRenderer(project, graph, src, gen, packageName, boxName, classes).execute();
		new JMXServerRenderer(graph, gen, packageName, boxName).execute();
	}

	private void jms() {
		new JMSRequestRenderer(project, graph, src, gen, packageName, boxName, classes).execute();
		new JMSServiceRenderer(graph, gen, packageName, boxName, classes).execute();
	}

	private void tasks() {
		new TaskRenderer(project, graph, src, gen, packageName, boxName, classes).execute();
		new TaskerRenderer(graph, gen, packageName, boxName, classes).execute();
	}

	private void bus() {
		if (graph.nessClientList().isEmpty()) return;
		new ProcessRenderer(graph, src, packageName, boxName, classes).execute();
		new MounterRenderer(graph, src, packageName, boxName, classes).execute();
		new FeederRenderer(graph, gen, src, packageName, boxName, classes).execute();
		new TanksConnectorsRenderer(graph, gen, packageName, boxName).execute();
		if (module != null && safe(() -> ((LegioConfiguration) configurationOf(module)).graph().artifact().asLevel().model()) != null)
			new NessJMXOperationsRenderer(gen, src, packageName, boxName, classes).execute();
	}

	private void slack() {
		new SlackRenderer(project, graph, src, gen, packageName, boxName, classes).execute();
	}

	private void ui() {
		new DisplayRenderer(project, graph, src, gen, packageName, parent, boxName, classes).execute();
		new DisplaysRenderer(graph, gen, packageName, boxName).execute();
		new DialogsRenderer(graph, gen, packageName, boxName).execute();
		new DialogRenderer(graph, src, gen, packageName, boxName, classes).execute();
		new ResourceRenderer(project, graph, src, gen, packageName, boxName, classes).execute();
		new UIResourcesRenderer(graph, gen, packageName, boxName).execute();
		new UIAccessorCreator(module, graph, parent).execute();
	}

	private void box() {
		new AbstractBoxRenderer(graph, gen, packageName, module, parent, hasModel).execute();
		new BoxRenderer(graph, src, packageName, module, hasModel).execute();
		new BoxConfigurationRenderer(graph, gen, packageName, module, parent, hasModel).execute();
	}

	private String boxName() {
		if (module != null) {
			final Configuration configuration = configurationOf(module);
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
			final Configuration configuration = configurationOf(module);
			final List<? extends Configuration.LanguageLibrary> languages = configuration.languages();
			if (languages.isEmpty() || languages.get(0).generationPackage() == null) return null;
			final String workingPackage = languages.get(0).generationPackage().replace(".graph", "");
			if (facade.findClass(workingPackage + ".box." + Formatters.firstUpperCase(languages.get(0).name()) + "Box", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)) != null)
				return workingPackage.toLowerCase() + ".box." + Formatters.firstUpperCase(languages.get(0).name());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	private boolean hasModel() {
		return module != null && configurationOf(module) != null && hasModel(configurationOf(module));
	}

	private boolean hasModel(Configuration configuration) {
		return !configuration.languages().isEmpty();
	}

	private void main() {
		new MainRenderer(src, hasModel, packageName, module).execute();
		final File file = new File(res, "log4j.properties");
		if (!file.exists()) {
			try {
				java.nio.file.Files.write(file.toPath(), getBytes());
			} catch (IOException e) {
				Logger.getRootLogger().error(e.getMessage(), e);
			}
		}
	}

	private byte[] getBytes() throws IOException {
		return IOUtils.toByteArray(this.getClass().getResourceAsStream("/log4j_model.properties"));

	}
}
