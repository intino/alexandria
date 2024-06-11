package io.intino.konos.builder.codegeneration;

import io.intino.builder.CompilerConfiguration;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.magritte.framework.Layer;

import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class BoxConfigurationRenderer extends Renderer {
	private final CompilerConfiguration configuration;
	private final Set<String> params;
	private final boolean isTara;

	BoxConfigurationRenderer(CompilationContext context, boolean isTara, Set<String> params) {
		super(context);
		this.isTara = isTara;
		this.params = params;
		this.configuration = context.configuration();
		this.params.addAll(configuration.parameters());
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("boxconfiguration");
		final String boxName = fillFrame(builder);
		Commons.writeFrame(gen(Target.Service), snakeCaseToCamelCase(boxName) + "Configuration", new BoxConfigurationTemplate().render(builder.toFrame(), Formatters.all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf((Layer) null), javaFile(gen(Target.Service), snakeCaseToCamelCase(boxName) + "Configuration").getAbsolutePath()));
	}

	private String fillFrame(FrameBuilder builder) {
		final String boxName = context.boxName();
		builder.add("name", boxName).add("package", packageName());
		if (parent() != null && configuration != null && configuration.dsl() != null && !configuration.dsl().level().isMetaMetaModel())
			builder.add("parent", parent());
		if (isTara) builder.add("tara", "");
		params.stream()
				.filter(parameter -> !parameter.equalsIgnoreCase("home"))
				.forEach(parameter -> builder.add("parameter", new FrameBuilder(isFile(parameter) ? "file" : "regular")
						.add("name", nameOf(parameter))
						.add("value", parameter)));
		return boxName;
	}

	private boolean isFile(String parameter) {
		return parameter.endsWith("file") || parameter.endsWith("directory") || parameter.endsWith("folder");
	}

	private String nameOf(String parameter) {
		return parameter.replace("-", " ").replace("_", " ");
	}
}