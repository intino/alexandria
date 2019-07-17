package io.intino.konos.builder.codegeneration.main;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

import java.io.File;

public class MainRenderer {
	private final Settings settings;
	private final File destination;
	private final boolean hasModel;
	private final Configuration configuration;

	public MainRenderer(Settings settings, KonosGraph graph, boolean hasModel) {
		this.settings = settings;
		this.destination = settings.src(Target.Owner);
		this.hasModel = hasModel;
		this.configuration = settings.module() != null ? TaraUtil.configurationOf(settings.module()) : null;
	}

	public void execute() {
		if (configuration == null) return;
		final String name = settings.boxName();
		FrameBuilder builder = new FrameBuilder("main").add("package", settings.packageName()).add("name", name);
		if (hasModel) builder.add("model", new FrameBuilder("model").add("name", name).toFrame());
		if (!Commons.javaFile(destination, "Main").exists())
			Commons.writeFrame(destination, "Main", template().render(builder.toFrame()));
	}

	private Template template() {
		return Formatters.customize(new MainTemplate());
	}

}
