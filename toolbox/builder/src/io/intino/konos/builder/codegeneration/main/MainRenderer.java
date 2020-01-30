package io.intino.konos.builder.codegeneration.main;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;

import java.io.File;

public class MainRenderer {
	private final CompilationContext compilationContext;
	private final File destination;
	private final boolean hasModel;
	private final CompilerConfiguration configuration;

	public MainRenderer(CompilationContext compilationContext, boolean hasModel) {
		this.compilationContext = compilationContext;
		this.destination = compilationContext.src(Target.Owner);
		this.hasModel = hasModel;
		this.configuration = compilationContext.configuration();
	}

	public void execute() {
		if (configuration == null) return;
		final String name = compilationContext.boxName();
		FrameBuilder builder = new FrameBuilder("main").add("package", compilationContext.packageName()).add("name", name);
		if (hasModel) builder.add("model", new FrameBuilder("model").add("name", name).toFrame());
		if (!Commons.javaFile(destination, "Main").exists())
			Commons.writeFrame(destination, "Main", template().render(builder.toFrame()));
	}

	private Template template() {
		return Formatters.customize(new MainTemplate());
	}

}
