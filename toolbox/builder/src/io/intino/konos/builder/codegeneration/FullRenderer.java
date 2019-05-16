package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import io.intino.konos.builder.codegeneration.cache.CacheReader;
import io.intino.konos.builder.codegeneration.cache.CacheWriter;
import io.intino.konos.builder.codegeneration.datalake.DatalakeRenderer;
import io.intino.konos.builder.codegeneration.datalake.NessJMXOperationsRenderer;
import io.intino.konos.builder.codegeneration.datalake.feeder.FeederRenderer;
import io.intino.konos.builder.codegeneration.datalake.mounter.MounterRenderer;
import io.intino.konos.builder.codegeneration.datalake.process.ProcessRenderer;
import io.intino.konos.builder.codegeneration.exception.ExceptionRenderer;
import io.intino.konos.builder.codegeneration.main.MainRenderer;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.codegeneration.services.jms.JMSRequestRenderer;
import io.intino.konos.builder.codegeneration.services.jms.JMSServiceRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXOperationsServiceRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXServerRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTResourceRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTServiceRenderer;
import io.intino.konos.builder.codegeneration.services.slack.SlackRenderer;
import io.intino.konos.builder.codegeneration.services.ui.ServiceListCleaner;
import io.intino.konos.builder.codegeneration.task.TaskRenderer;
import io.intino.konos.builder.codegeneration.task.TaskerRenderer;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.PrivateComponents;
import io.intino.plugin.codeinsight.linemarkers.InterfaceToJavaImplementation;
import io.intino.plugin.project.LegioConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.plugin.project.Safe.safe;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.configurationOf;
import static java.util.stream.Collectors.toList;

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

	private static final String ElementCache = "element.cache";

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
		io.intino.konos.builder.codegeneration.cache.ElementCache cache = loadCache();
		clean(cache);
		render(cache);
		saveCache(cache);
	}

	private void clean(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new ServiceListCleaner(settings(cache), graph).execute();
	}

	private void render(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		schemas(cache);
		exceptions(cache);
		rest(cache);
		tasks(cache);
		jmx(cache);
		jms(cache);
		bus(cache);
		slack(cache);
		ui(cache);
		box(cache);
		main(cache);

		InterfaceToJavaImplementation.nodeMap.clear();
		InterfaceToJavaImplementation.nodeMap.putAll(classes);
	}

	private void schemas(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new SchemaListRenderer(settings(cache), graph).execute();
	}

	private void exceptions(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new ExceptionRenderer(settings(cache), graph).execute();
	}

	private void rest(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new RESTResourceRenderer(settings(cache), graph).execute();
		new RESTServiceRenderer(settings(cache), graph).execute();
	}

	private void jmx(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new JMXOperationsServiceRenderer(settings(cache), graph).execute();
		new JMXServerRenderer(settings(cache), graph).execute();
	}

	private void jms(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new JMSRequestRenderer(settings(cache), graph).execute();
		new JMSServiceRenderer(settings(cache), graph).execute();
	}

	private void tasks(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new TaskRenderer(settings(cache), graph).execute();
		new TaskerRenderer(settings(cache), graph).execute();
	}

	private void bus(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		if (graph.nessClientList().isEmpty()) return;
		new ProcessRenderer(settings(cache), graph).execute();
		new MounterRenderer(settings(cache), graph).execute();
		new FeederRenderer(settings(cache), graph).execute();
		new DatalakeRenderer(settings(cache), graph).execute();
		new NessJMXOperationsRenderer(settings(cache), graph, (module != null && safe(() -> ((LegioConfiguration) configurationOf(module)).graph().artifact().asLevel().model()) != null)).execute();
	}

	private void slack(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new SlackRenderer(settings(cache), graph).execute();
	}

	private void ui(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		prepareGraphForUi();
		cache.addAll(uiServer(cache).entrySet());
		cache.addAll(uiClient(cache).entrySet());
	}

	private io.intino.konos.builder.codegeneration.cache.ElementCache uiServer(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		io.intino.konos.builder.codegeneration.cache.ElementCache newCache = cache.clone();
		new io.intino.konos.builder.codegeneration.accessor.ui.ServiceListRenderer(settings(newCache), graph).execute();
		return newCache;
	}

	private io.intino.konos.builder.codegeneration.cache.ElementCache uiClient(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		io.intino.konos.builder.codegeneration.cache.ElementCache newCache = cache.clone();
		new io.intino.konos.builder.codegeneration.services.ui.ServiceListRenderer(settings(newCache), graph).execute();
		return newCache;
	}

	private void box(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new AbstractBoxRenderer(settings(cache), graph, hasModel).execute();
		new BoxRenderer(settings(cache), graph, hasModel).execute();
		new BoxConfigurationRenderer(settings(cache), graph, hasModel).execute();
	}

	private void main(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new MainRenderer(settings(cache), graph, hasModel).execute();
		final File file = new File(res, "log4j.properties");
		if (!file.exists()) {
			try {
				java.nio.file.Files.write(file.toPath(), getBytes());
			} catch (IOException e) {
				Logger.getRootLogger().error(e.getMessage(), e);
			}
		}
	}

	private String boxName() {
		if (module != null) {
			final io.intino.tara.compiler.shared.Configuration configuration = configurationOf(module);
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
			final io.intino.tara.compiler.shared.Configuration configuration = configurationOf(module);
			final List<? extends io.intino.tara.compiler.shared.Configuration.LanguageLibrary> languages = configuration.languages();
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

	private boolean hasModel(io.intino.tara.compiler.shared.Configuration configuration) {
		return !configuration.languages().isEmpty();
	}

	private byte[] getBytes() throws IOException {
		return IOUtils.toByteArray(this.getClass().getResourceAsStream("/log4j_model.properties"));

	}

	private Settings settings(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		return new Settings().project(project).module(module).parent(parent)
							 .res(res).src(src).gen(gen).boxName(boxName).packageName(packageName)
							 .classes(classes).cache(cache);
	}

	protected void prepareGraphForUi() {
		createUiTableRows();
	}

	protected void createUiTableRows() {
		graph.serviceList().forEach(service -> service.graph().core$().find(CatalogComponents.Table.class).forEach(this::createUiTableRow));
	}

	protected void createUiTableRow(CatalogComponents.Table element) {
		List<CatalogComponents.Collection.Mold.Item> itemList = element.moldList().stream().map(CatalogComponents.Collection.Mold::item).collect(toList());
		String name = firstUpperCase(element.name$()) + "Row";
		if (element.graph().privateComponentsList().size() <= 0) element.graph().create().privateComponents();
		PrivateComponents.Row row = element.graph().privateComponents(0).rowList().stream().filter(c -> c.name$().equals(name)).findFirst().orElse(null);
		if (row == null) element.graph().privateComponents(0).create(name).row(itemList);
	}

	private io.intino.konos.builder.codegeneration.cache.ElementCache loadCache() {
		return new CacheReader(cacheFile()).load();
	}

	private void saveCache(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new CacheWriter(cacheFile()).save(cache);
	}

	@NotNull
	private File cacheFile() {
		return new File(res, ElementCache);
	}

}
