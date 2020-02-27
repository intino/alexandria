package io.intino.konos.builder.codegeneration;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.magritte.dsl.Meta;
import io.intino.magritte.dsl.Proteo;

import java.util.ArrayList;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class BoxRenderer extends Renderer {
	private final KonosGraph graph;
	private boolean isTara;

	BoxRenderer(CompilationContext context, KonosGraph graph, boolean isTara) {
		super(context, Target.Owner);
		this.graph = graph;
		this.isTara = isTara;
	}

	@Override
	public void render() {
		if (configuration() == null) return;
		final String name = context.boxName();
		if (Commons.javaFile(src(), snakeCaseToCamelCase(name) + "Box").exists()) return;
		FrameBuilder builder = new FrameBuilder("Box").add("package", packageName()).add("name", name);
		if (isTara) builder.add("tara", fillTara());
//		DumbService.getInstance(module().getProject()).setAlternativeResolveEnabled(true);
//		final JavaPsiFacade facade = JavaPsiFacade.getInstance(module().getProject());
//		if (facade.findClass("io.intino.konos.server.ui.services.AuthService", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module())) != null)
//			builder.add("rest", name);TODO

		if (hasAuthenticatedApis()) builder.add("authenticationValidator", new FrameBuilder().add("type", "Basic"));
//		DumbService.getInstance(module().getProject()).setAlternativeResolveEnabled(false);
		Commons.writeFrame(src(), snakeCaseToCamelCase(name) + "Box", template().render(builder.toFrame()));
	}

	private boolean hasAuthenticatedApis() {
		return graph.serviceList(service -> service.isREST() && service.asREST().authenticatedWithToken() != null).findAny().isPresent();
	}

	private Frame fillTara() {
		FrameBuilder builder = new FrameBuilder();
		CompilerConfiguration configuration = configuration();
		builder.add("name", context.boxName());
		if (configuration.model() != null && configuration.model().outDsl() != null)
			builder.add("outDSL", configuration.model().outDsl());
		builder.add("wrapper", dsls());
		return builder.toFrame();
	}

	private String[] dsls() {
		CompilerConfiguration configuration = configuration();
		List<String> dsls = new ArrayList<>();
		if (configuration.model() == null) return new String[0];
		String language = configuration.model().language();
		if (!Meta.class.getSimpleName().equals(language) && !Proteo.class.getSimpleName().equals(language)) {
			final String genPackage = configuration.model().generationPackage();
			dsls.add((genPackage == null ? "" : genPackage.toLowerCase() + ".") + firstUpperCase(language));
		}
		if (!configuration.model().level().isSolution())
			dsls.add(configuration.generationPackage().toLowerCase() + "." + firstUpperCase(configuration.model().outDsl()));
		return dsls.toArray(new String[0]);
	}

	private Template template() {
		return Formatters.customize(new BoxTemplate());
	}

}
