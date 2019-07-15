package io.intino.konos.builder.codegeneration;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class BoxConfigurationRenderer extends Renderer {
	private final Configuration configuration;
	private boolean isTara;

	public BoxConfigurationRenderer(Settings settings, KonosGraph graph, boolean isTara) {
		super(settings, Target.Service);
		this.configuration = module() != null ? TaraUtil.configurationOf(module()) : null;
		this.isTara = isTara;
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("boxconfiguration");
		final String boxName = fillFrame(builder);
		Commons.writeFrame(gen(), snakeCaseToCamelCase(boxName) + "Configuration", template().render(builder.toFrame()));
	}

	private String fillFrame(FrameBuilder builder) {
		final String boxName = name();
		builder.add("name", boxName).add("package", packageName());
		if (parent() != null && configuration != null && !Platform.equals(configuration.level())) builder.add("parent", parent());
		if (isTara) builder.add("tara", "");
		return boxName;
	}

	private Template template() {
		return Formatters.customize(new BoxConfigurationTemplate());
	}
}