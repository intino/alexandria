package io.intino.pandora.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.pandora.builder.helpers.Commons;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static io.intino.tara.compiler.shared.Configuration.Level.System;

public class MainRenderer {

	private final File destination;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;

	public MainRenderer(File destination, String packageName, Module module) {
		this.destination = destination;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
	}

	public void execute() {
		if (Commons.javaFile(destination, "Main").exists()) return;
		Frame frame = new Frame().addTypes("main");
		frame.addSlot("package", packageName);
		frame.addSlot("name", name());
		if (configuration != null && System.equals(configuration.level())) {
			if (configuration.dslWorkingPackage() != null)
				frame.addSlot("dslPackage", configuration.dslWorkingPackage());
			frame.addSlot("language", configuration.dsl());
			Commons.writeFrame(destination, "Main", template().format(frame));
		}
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
