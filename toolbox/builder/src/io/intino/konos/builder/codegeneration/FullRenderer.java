package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import io.intino.konos.builder.codegeneration.bpm.BpmRenderer;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.codegeneration.datahub.DatalakeRenderer;
import io.intino.konos.builder.codegeneration.datahub.adapter.AdapterRenderer;
import io.intino.konos.builder.codegeneration.datahub.messagehub.MessageHubRenderer;
import io.intino.konos.builder.codegeneration.datahub.mounter.MounterRenderer;
import io.intino.konos.builder.codegeneration.datahub.subscriber.SubscriberRenderer;
import io.intino.konos.builder.codegeneration.exception.ExceptionRenderer;
import io.intino.konos.builder.codegeneration.feeder.FeederRenderer;
import io.intino.konos.builder.codegeneration.main.MainRenderer;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.codegeneration.sentinel.ListenerRenderer;
import io.intino.konos.builder.codegeneration.sentinel.SentinelsRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXOperationsServiceRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXServerRenderer;
import io.intino.konos.builder.codegeneration.services.messaging.MessagingRequestRenderer;
import io.intino.konos.builder.codegeneration.services.messaging.MessagingServiceRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTResourceRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTServiceRenderer;
import io.intino.konos.builder.codegeneration.services.slack.SlackRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.plugin.codeinsight.linemarkers.InterfaceToJavaImplementation;
import io.intino.plugin.dependencyresolution.LanguageResolver;
import io.intino.tara.compiler.shared.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.search.GlobalSearchScope.moduleWithDependenciesAndLibrariesScope;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
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
		datalake();
		messageHub();
		subscribers();
		mounters();
		adapters();
		feeders();
		processes();
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
		new MessagingRequestRenderer(settings, graph).execute();
		new MessagingServiceRenderer(settings, graph).execute();
	}

	private void tasks() {
		new ListenerRenderer(settings, graph).execute();
		new SentinelsRenderer(settings, graph).execute();
	}

	private void datalake() {
		new DatalakeRenderer(settings, graph).execute();
	}

	private void messageHub() {
		new MessageHubRenderer(settings, graph).execute();
	}

	private void mounters() {
		new MounterRenderer(settings, graph).execute();
	}

	private void subscribers() {
		new SubscriberRenderer(settings, graph).execute();
	}

	private void adapters() {
		new AdapterRenderer(settings, graph).execute();
	}

	private void processes() {
		new BpmRenderer(settings, graph).execute();
	}

	private void feeders() {
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
		AbstractBoxRenderer renderer = new AbstractBoxRenderer(settings, graph, hasModel);
		renderer.execute();

		new BoxRenderer(settings, graph, hasModel).execute();
		new BoxConfigurationRenderer(settings, hasModel, renderer.customParameters()).execute();
	}

	private void main() {
		new MainRenderer(settings, graph, hasModel).execute();
	}

	private String parent() {
		Module module = settings.module();
		try {
			if (module == null) return null;
			final JavaPsiFacade facade = JavaPsiFacade.getInstance(module.getProject());
			final io.intino.tara.compiler.shared.Configuration configuration = configurationOf(module);
			if (configuration.model() == null) return null;
			Configuration.Model.ModelLanguage language = configuration.model().language();
			if (language == null || language.generationPackage() == null) return null;
			final String workingPackage = language.generationPackage().replace(".graph", "");
			String artifact = LanguageResolver.languageId(language.name(), language.effectiveVersion()).split(":")[1];
			Application application = ApplicationManager.getApplication();
			PsiClass aClass = application.runReadAction((Computable<PsiClass>) () -> facade.findClass(parentBoxName(workingPackage, artifact), moduleWithDependenciesAndLibrariesScope(module)));
			if (aClass == null)
				aClass = application.runReadAction((Computable<PsiClass>) () -> facade.findClass(parentBoxName(workingPackage, language.name()), moduleWithDependenciesAndLibrariesScope(module)));
			if (aClass != null)
				return workingPackage.toLowerCase() + ".box." + firstUpperCase(language.name());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@NotNull
	private String parentBoxName(String workingPackage, String name) {
		return workingPackage + ".box." + firstUpperCase(snakeCaseToCamelCase().format(name).toString()) + "Box";
	}

	private boolean hasModel() {
		Module module = settings.module();
		return module != null && configurationOf(module) != null && hasModel(configurationOf(module));
	}

	private boolean hasModel(io.intino.tara.compiler.shared.Configuration configuration) {
		return configuration.model() != null && configuration.model().language() != null;
	}

}
