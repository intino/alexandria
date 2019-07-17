package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.codegeneration.datahub.adapter.AdapterRenderer;
import io.intino.konos.builder.codegeneration.datahub.feeder.FeederRenderer;
import io.intino.konos.builder.codegeneration.datahub.messagehub.MessageHubRenderer;
import io.intino.konos.builder.codegeneration.datahub.mounter.MounterRenderer;
import io.intino.konos.builder.codegeneration.datahub.process.ProcessRenderer;
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
import io.intino.konos.builder.codegeneration.task.SchedulerRenderer;
import io.intino.konos.builder.codegeneration.task.TaskRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.plugin.codeinsight.linemarkers.InterfaceToJavaImplementation;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.configurationOf;

public class FullRenderer {
	@Nullable
	private final KonosGraph graph;
	private final Settings settings;
	private final boolean hasModel;

	public FullRenderer(KonosGraph graph, Settings settings) {
		this.graph = graph;
		this.settings = settings;
		this.settings.parent(parent());
		this.hasModel = hasModel();
	}

	public void execute() {
		clean();
		render();
	}

	private void clean() {
		new FullCleaner(settings, graph).execute();
	}

	private void render() {
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
		InterfaceToJavaImplementation.nodeMap.putAll(settings.classes());
	}

	private void schemas() {
		new SchemaListRenderer(settings, graph).execute();
	}

	private void exceptions() {
		new ExceptionRenderer(settings, graph).execute();
	}

	private void rest() {
		new RESTResourceRenderer(settings, graph).execute();
		new RESTServiceRenderer(settings, graph).execute();
	}

	private void jmx() {
		new JMXOperationsServiceRenderer(settings, graph).execute();
		new JMXServerRenderer(settings, graph).execute();
	}

	private void jms() {
		new JMSRequestRenderer(settings, graph).execute();
		new JMSServiceRenderer(settings, graph).execute();
	}

	private void tasks() {
		new TaskRenderer(settings, graph).execute();
		new SchedulerRenderer(settings, graph).execute();
	}

	private void bus() {
		if (graph.dataHub() == null) return;
		new ProcessRenderer(settings, graph).execute();
		new MessageHubRenderer(settings, graph).execute();
		new MounterRenderer(settings, graph).execute();
		new AdapterRenderer(settings, graph).execute();
		new FeederRenderer(settings, graph).execute();
	}

	private void slack() {
		new SlackRenderer(settings, graph).execute();
	}

	private void ui() {
		ElementCache cache = settings.cache();
		ComponentRenderer.clearCache();
		io.intino.konos.builder.codegeneration.cache.ElementCache serverCache = cache.clone();
		uiServer(serverCache);
		uiClient(cache.clone());
		cache.putAll(serverCache);
	}

	private void uiServer(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new io.intino.konos.builder.codegeneration.services.ui.ServiceListRenderer(settings.clone().cache(cache), graph).execute();
	}

	private void uiClient(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new io.intino.konos.builder.codegeneration.accessor.ui.ServiceListRenderer(settings.clone().cache(cache), graph).execute();
	}

	private void box() {
		new AbstractBoxRenderer(settings, graph, hasModel).execute();
		new BoxRenderer(settings, graph, hasModel).execute();
		new BoxConfigurationRenderer(settings, graph, hasModel).execute();
	}

	private void main() {
		new MainRenderer(settings, graph, hasModel).execute();
		final File file = new File(settings.res(Target.Owner), "log4j.properties");
		if (!file.exists()) {
			try {
				java.nio.file.Files.write(file.toPath(), getBytes());
			} catch (IOException e) {
				Logger.getRootLogger().error(e.getMessage(), e);
			}
		}
	}

	private String parent() {
		Module module = settings.module();
		try {
			if (module == null) return null;
			final JavaPsiFacade facade = JavaPsiFacade.getInstance(module.getProject());
			final io.intino.tara.compiler.shared.Configuration configuration = configurationOf(module);
			final List<? extends io.intino.tara.compiler.shared.Configuration.LanguageLibrary> languages = configuration.languages();
			if (languages.isEmpty() || languages.get(0).generationPackage() == null) return null;
			final String workingPackage = languages.get(0).generationPackage().replace(".graph", "");
			PsiClass aClass = ApplicationManager.getApplication().runReadAction((Computable<PsiClass>) () -> facade.findClass(workingPackage + ".box." + Formatters.firstUpperCase(languages.get(0).name()) + "Box", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)));
			if (aClass != null) return workingPackage.toLowerCase() + ".box." + Formatters.firstUpperCase(languages.get(0).name());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	private boolean hasModel() {
		Module module = settings.module();
		return module != null && configurationOf(module) != null && hasModel(configurationOf(module));
	}

	private boolean hasModel(io.intino.tara.compiler.shared.Configuration configuration) {
		return !configuration.languages().isEmpty();
	}

	private byte[] getBytes() throws IOException {
		return IOUtils.toByteArray(this.getClass().getResourceAsStream("/log4j_model.properties"));

	}

}
