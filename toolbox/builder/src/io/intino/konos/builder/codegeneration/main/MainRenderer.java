package io.intino.konos.builder.codegeneration.main;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;

import java.io.File;

public class MainRenderer extends Renderer {
	private final CompilationContext context;
	private final File destination;
	private final boolean hasModel;
	private final CompilerConfiguration configuration;

	public MainRenderer(CompilationContext context, boolean hasModel) {
		super(context, Target.Owner);
		this.context = context;
		this.destination = context.src(Target.Owner);
		this.hasModel = hasModel;
		this.configuration = context.configuration();
	}

	public void execute() {
		if (configuration == null) return;
		final String name = context.boxName();
		FrameBuilder builder = new FrameBuilder("main").add("package", context.packageName()).add("name", name);
		if (hasModel) builder.add("model", new FrameBuilder("model").add("name", name).toFrame());
		if (!Commons.javaFile(destination, "Main").exists())
			Commons.writeFrame(destination, "Main", template().render(builder.toFrame()));
	}

	@Override
	protected void render() {
		execute();
	}

	private Template template() {
		return Formatters.customize(new MainTemplate());
	}
}
