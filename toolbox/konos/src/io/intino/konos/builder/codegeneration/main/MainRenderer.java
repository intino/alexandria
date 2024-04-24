package io.intino.konos.builder.codegeneration.main;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.builder.PostCompileConfigurationMainActionMessage;
import io.intino.konos.dsl.KonosGraph;
import io.intino.magritte.framework.Layer;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MainRenderer extends Renderer {
	private final CompilationContext context;
	private final File destination;
	private final boolean hasModel;
	private final CompilerConfiguration configuration;
	private final KonosGraph graph;

	public MainRenderer(CompilationContext context, boolean hasModel, KonosGraph graph) {
		super(context);
		this.context = context;
		this.destination = new File(context.configuration().srcDirectory(), packageName().replace(".", File.separator));
		this.hasModel = hasModel;
		this.configuration = context.configuration();
		this.graph = graph;
	}

	public void execute() {
		if (configuration == null) return;
		final String name = context.boxName();
		FrameBuilder builder = new FrameBuilder("main").add("package", context.packageName()).add("name", name);
		if (hasModel) builder.add("model", new FrameBuilder("model").add("name", name).toFrame());
		File mainFile = javaFile(destination, "Main");
		if (!mainFile.exists()) {
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(graph.core$().rootList().get(0).as(Layer.class)), mainFile.getAbsolutePath()));
			writeFrame(destination, "Main", template().render(builder.toFrame()));
			context.postCompileActionMessages().add(new PostCompileConfigurationMainActionMessage(configuration.module(), context.packageName() + ".Main"));
		}
	}

	@Override
	protected void render() {
		execute();
	}

	private Template template() {
		return Formatters.customize(new MainTemplate());
	}
}
