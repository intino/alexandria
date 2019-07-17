package io.intino.konos.builder.codegeneration.main;

import com.intellij.openapi.module.Module;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

import java.io.File;

public class MainRenderer {
	private final File destination;
	private final boolean hasModel;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;

	public MainRenderer(File destination, boolean hasModel, String packageName, Module module) {
		this.destination = destination;
		this.hasModel = hasModel;
		this.packageName = packageName;
		this.module = module;
		this.configuration = module != null ? TaraUtil.configurationOf(module) : null;
	}

	public void execute() {
		if (configuration == null) return;
		final String name = name();
		FrameBuilder builder = new FrameBuilder("main").add("package", packageName).add("name", name);
		if (hasModel) builder.add("model", new FrameBuilder("model").add("name", name).toFrame());
		if (!Commons.javaFile(destination, "Main").exists())
			Commons.writeFrame(destination, "Main", template().render(builder.toFrame()));
	}

	private Template template() {
		return Formatters.customize(new MainTemplate());
	}

	private String name() {
		return module != null ? configuration.artifactId() : Configuration.Level.Solution.name();
	}
}
