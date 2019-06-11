package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.dsl.Meta;
import io.intino.tara.dsl.Proteo;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class BoxRenderer {
	private final KonosGraph graph;
	private final File destination;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;
	private boolean isTara;

	BoxRenderer(KonosGraph graph, File destination, String packageName, Module module, boolean isTara) {
		this.graph = graph;
		this.destination = destination;
		this.packageName = packageName;
		this.module = module;
		this.configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.isTara = isTara;
	}

	public void execute() {
		final String name = name();
		if (configuration == null || Commons.javaFile(destination, snakeCaseToCamelCase(name) + "Box").exists()) {
			return;
		}
		FrameBuilder builder = new FrameBuilder("Box").add("package", packageName).add("name", name);
		if (isTara) builder.add("tara", fillTara());
		DumbService.getInstance(module.getProject()).setAlternativeResolveEnabled(true);
		final JavaPsiFacade facade = JavaPsiFacade.getInstance(module.getProject());
		if (facade.findClass("io.intino.konos.server.ui.services.AuthService", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)) != null)
			builder.add("rest", name);

		if (hasAuthenticatedApis()) builder.add("authenticationValidator", new FrameBuilder().add("type", "Basic"));
		DumbService.getInstance(module.getProject()).setAlternativeResolveEnabled(false);
		Commons.writeFrame(destination, snakeCaseToCamelCase(name) + "Box", template().render(builder.toFrame()));
	}

	private boolean hasAuthenticatedApis() {
		return graph.rESTServiceList().stream().anyMatch(restService -> restService.authenticatedWithToken() != null);
	}

	private Frame fillTara() {
		FrameBuilder builder = new FrameBuilder();
		builder.add("name", name());
		if (configuration.outLanguage() != null) builder.add("outDSL", configuration.outLanguage());
		builder.add("wrapper", dsls());
		return builder.toFrame();
	}

	private String[] dsls() {
		List<String> dsls = new ArrayList<>();
		for (Configuration.LanguageLibrary lang : configuration.languages())
			if (!Meta.class.getSimpleName().equals(lang.name()) && !Proteo.class.getSimpleName().equals(lang.name())) {
				final String genPackage = lang.generationPackage();
				dsls.add((genPackage == null ? "" : genPackage.toLowerCase() + ".") + firstUpperCase(lang.name()));
			}
		if (configuration.level() != Configuration.Level.Solution)
			dsls.add(configuration.workingPackage().toLowerCase() + "." + firstUpperCase(configuration.outLanguage()));
		return dsls.toArray(new String[0]);
	}

	private Template template() {
		return Formatters.customize(new BoxTemplate());
	}

	private String name() {
		if (module != null) {
			final String dsl = configuration.outLanguage();
			return dsl == null || dsl.isEmpty() ? module.getName() : dsl;
		} else return Configuration.Level.Solution.name();
	}
}
