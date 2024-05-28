package io.intino.konos.builder.codegeneration;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.KonosGraph;

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
		Commons.writeFrame(src(Target.Server), snakeCaseToCamelCase(name) + "Box", new BoxTemplate().render(builder.toFrame(), Formatters.all));
	}

	private Frame fillTara() {
		FrameBuilder builder = new FrameBuilder();
		CompilerConfiguration configuration = configuration();
		builder.add("name", context.boxName());
		if (configuration.dsl() != null && configuration.dsl().outDsl() != null)
			builder.add("outDSL", configuration.dsl().outDsl());
		builder.add("wrapper", dsls());
		return builder.toFrame();
	}

	private String[] dsls() {
		CompilerConfiguration configuration = configuration();
		List<String> dsls = new ArrayList<>();
		if (configuration.dsl() == null) return new String[0];
		String dsl = configuration.dsl().name();
		if (configuration.dsl().generationPackage() != null) {
			final String genPackage = configuration.dsl().generationPackage();
			dsls.add((genPackage == null ? "" : genPackage.toLowerCase() + ".") + firstUpperCase(dsl));
		}
		if (!configuration.dsl().level().isModel())
			dsls.add(configuration.generationPackage().toLowerCase() + "." + firstUpperCase(configuration.dsl().outDsl()));
		return dsls.toArray(new String[0]);
	}

}
