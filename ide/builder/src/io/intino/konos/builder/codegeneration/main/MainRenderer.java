package io.intino.konos.builder.codegeneration.main;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class MainRenderer {

	private final File destination;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;

	public MainRenderer(File destination, String packageName, Module module) {
		this.destination = destination;
		this.packageName = packageName;
		this.module = module;
		this.configuration = module != null ? TaraUtil.configurationOf(module) : null;
	}

	public void execute() {
		if (configuration == null) return;
		Frame frame = new Frame().addTypes("main").addSlot("package", packageName).addSlot("name", name());
		if (!Commons.javaFile(destination, "Main").exists())
			Commons.writeFrame(destination, "Main", template().format(frame));
	}

	private Template template() {
		return Formatters.customize(MainTemplate.create());
	}

	private String name() {
		if (module != null) {
			final String dsl = configuration.outDSL();
			if (dsl == null || dsl.isEmpty()) return module.getName();
			else return dsl;
		} else return "System";
	}

}
