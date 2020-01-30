package io.intino.konos.builder.codegeneration;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.helpers.Commons;

import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class BoxConfigurationRenderer extends Renderer {
	private final CompilerConfiguration configuration;
	private final Set<String> params;
	private boolean isTara;

	BoxConfigurationRenderer(CompilationContext compilationContext, boolean isTara, Set<String> params) {
		super(compilationContext, Target.Owner);
		this.isTara = isTara;
		this.params = params;
		this.configuration = compilationContext.configuration();
		this.params.addAll(configuration.parameters());
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("boxconfiguration");
		final String boxName = fillFrame(builder);
		Commons.writeFrame(gen(), snakeCaseToCamelCase(boxName) + "Configuration", template().render(builder.toFrame()));
	}

	private String fillFrame(FrameBuilder builder) {
		final String boxName = compilationContext.boxName();
		builder.add("name", boxName).add("package", packageName());
		if (parent() != null && configuration != null && configuration.model() != null && !configuration.model().level().isPlatform())
			builder.add("parent", parent());
		if (isTara) builder.add("tara", "");
		for (String parameter : params) {
			if (parameter.equalsIgnoreCase("home")) continue;
			builder.add("parameter", new FrameBuilder().add("name", nameOf(parameter)).add("value", parameter));
		}
		return boxName;
	}

	private String nameOf(String parameter) {
		return parameter.replace("-", " ").replace("_", " ");
	}

	private Template template() {
		return Formatters.customize(new BoxConfigurationTemplate());
	}
}