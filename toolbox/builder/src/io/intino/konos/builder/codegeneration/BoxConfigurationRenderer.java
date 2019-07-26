package io.intino.konos.builder.codegeneration;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.helpers.Commons;
import io.intino.legio.graph.Parameter;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class BoxConfigurationRenderer extends Renderer {
	private final Configuration configuration;
	private final Set<String> params;
	private boolean isTara;

	BoxConfigurationRenderer(Settings settings, boolean isTara, Set<String> params) {
		super(settings, Target.Owner);
		this.isTara = isTara;
		this.params = params;
		this.configuration = module() != null ? TaraUtil.configurationOf(module()) : null;
		if (configuration != null)
			for (Parameter parameter : ((LegioConfiguration) configuration).graph().artifact().parameterList())
				this.params.add(parameter.name());
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("boxconfiguration");
		final String boxName = fillFrame(builder);
		Commons.writeFrame(gen(), snakeCaseToCamelCase(boxName) + "Configuration", template().render(builder.toFrame()));
	}

	private String fillFrame(FrameBuilder builder) {
		final String boxName = settings.boxName();
		builder.add("name", boxName).add("package", packageName());
		if (parent() != null && configuration != null && !Platform.equals(configuration.level()))
			builder.add("parent", parent());
		if (isTara) builder.add("tara", "");
		for (String parameter : params)
			builder.add("parameter", new FrameBuilder().add("name", nameOf(parameter)).add("value", parameter));
		return boxName;
	}

	private String nameOf(String parameter) {
		return parameter.replace("-", " ").replace("_", " ");
	}

	private Template template() {
		return Formatters.customize(new BoxConfigurationTemplate());
	}
}