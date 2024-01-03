package io.intino.konos.builder.codegeneration;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.KonosGraph;
import io.intino.tara.dsls.Meta;
import io.intino.tara.dsls.Proteo;

import java.util.ArrayList;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class BoxRenderer extends Renderer {
	private final KonosGraph graph;
	private final boolean hasModel;

	BoxRenderer(CompilationContext context, KonosGraph graph, boolean hasModel) {
		super(context);
		this.graph = graph;
		this.hasModel = hasModel;
	}

	@Override
	public void render() {
		if (configuration() == null) return;
		final String name = context.boxName();
		if (Commons.javaFile(src(Target.Server), snakeCaseToCamelCase(name) + "Box").exists()) return;
		FrameBuilder builder = new FrameBuilder("Box").add("package", packageName()).add("name", name);
		if (hasModel) builder.add("tara", fillTara());
		if (!graph.uiServiceList().isEmpty()) builder.add("hasUi", new FrameBuilder().add("package", packageName()));
		context.compiledFiles().add(new OutputItem(src(Target.Server).getAbsolutePath(), javaFile(src(Target.Server), snakeCaseToCamelCase(name) + "Box").getAbsolutePath()));
		Commons.writeFrame(src(Target.Server), snakeCaseToCamelCase(name) + "Box", template().render(builder.toFrame()));
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
