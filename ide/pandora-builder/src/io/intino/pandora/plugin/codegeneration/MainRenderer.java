package io.intino.pandora.plugin.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static tara.compiler.shared.Configuration.Level.System;

public class MainRenderer {

	private final File gen;
	private final String packageName;
	private final Module module;
	private final PandoraApplication application;
	private final Configuration configuration;

	public MainRenderer(Graph graph, File gen, String packageName, Module module) {
		application = graph.application();
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("main");
		frame.addSlot("package", packageName);
		frame.addSlot("name", name());
		if (configuration != null && System.equals(configuration.level())) {
			if (configuration.dslWorkingPackage() != null) frame.addSlot("dslPackage", configuration.dslWorkingPackage());
			frame.addSlot("language", configuration.dsl());
			Commons.writeFrame(gen, "Main", template().format(frame));
		}
	}


	private Template template() {
		Template template = MainTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private String name() {
		if (module != null) {
			final String dsl = configuration.outDSL();
			if (dsl == null || dsl.isEmpty()) return module.getName();
			else return dsl;
		} else return "System";
	}

}
