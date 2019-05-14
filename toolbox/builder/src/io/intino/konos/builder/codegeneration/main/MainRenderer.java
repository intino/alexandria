package io.intino.konos.builder.codegeneration.main;

import com.intellij.openapi.module.Module;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

public class MainRenderer extends Renderer {
	private final boolean hasModel;
	private final Configuration configuration;

	public MainRenderer(Settings settings, KonosGraph graph, boolean hasModel) {
		super(settings, Target.Service);
		this.hasModel = hasModel;
		this.configuration = module() != null ? TaraUtil.configurationOf(module()) : null;
	}

	@Override
	public void render() {
		if (configuration == null) return;
		final String name = name();
		FrameBuilder builder = new FrameBuilder("main").add("package", packageName()).add("name", name);
		if (hasModel) builder.add("model", new FrameBuilder("model").add("name", name).toFrame());
		if (!Commons.javaFile(src(), "Main").exists())
			Commons.writeFrame(src(), "Main", template().render(builder.toFrame()));
	}

	private Template template() {
		return Formatters.customize(new MainTemplate());
	}

	private String name() {
		Module module = module();
		if (module != null) {
			final String dsl = configuration.outDSL();
			if (dsl == null || dsl.isEmpty()) return module.getName();
			else return dsl;
		} else return "System";
	}

}
