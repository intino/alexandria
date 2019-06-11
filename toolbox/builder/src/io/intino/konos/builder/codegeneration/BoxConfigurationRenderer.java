package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.helpers.Commons;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class BoxConfigurationRenderer {

	private final File gen;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;
	private String parent;
	private boolean isTara;

	public BoxConfigurationRenderer(File gen, String packageName, Module module, String parent, boolean isTara) {
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		this.configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.parent = parent;
		this.isTara = isTara;
	}

	public void execute() {
		FrameBuilder builder = new FrameBuilder("boxconfiguration");
		final String boxName = fillFrame(builder);
		Commons.writeFrame(gen, snakeCaseToCamelCase(boxName) + "Configuration", template().render(builder.toFrame()));
	}

	private String fillFrame(FrameBuilder builder) {
		final String boxName = name();
		builder.add("name", boxName).add("package", packageName);
		if (parent != null && configuration != null && !Platform.equals(configuration.level())) builder.add("parent", parent);
		if (isTara) builder.add("tara", "");
		return boxName;
	}

	private String name() {
		if (module != null) {
			final Configuration configuration = TaraUtil.configurationOf(module);
			final String dsl = configuration.outLanguage();
			if (dsl == null || dsl.isEmpty()) return module.getName();
			else return dsl;
		} else return "System";
	}

	private Template template() {
		return Formatters.customize(new BoxConfigurationTemplate());
	}
}