package io.intino.konos.builder.codegeneration.main;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.intino.tara.compiler.shared.Configuration.Level.System;

public class GraphProviderRenderer {


	private final File destination;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;

	public GraphProviderRenderer(File destination, String packageName, Module module) {
		this.destination = destination;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
	}

	public void execute() {
		if (configuration == null || !System.equals(configuration.level()) || Commons.javaFile(destination, "GraphProvider").exists())
			return;
		Frame frame = new Frame().addTypes("GraphProvider");
		frame.addSlot("package", packageName);
		frame.addSlot("name", name());
		frame.addSlot("wrapper", dsls());
		Commons.writeFrame(destination, "GraphProvider", template().format(frame));
	}

	private String[] dsls() {
		List<String> dsls = new ArrayList<>();
		for (Configuration.LanguageLibrary lang : configuration.languages())
			dsls.add(lang.generationPackage().toLowerCase() + "." + Formatters.firstUpperCase(lang.name()));
		//TODO add platform language
		return dsls.toArray(new String[dsls.size()]);
	}

	private Template template() {
		return Formatters.customize(GraphProviderTemplate.create());
	}

	private String name() {
		if (module != null) {
			final String dsl = configuration.outDSL();
			return dsl == null || dsl.isEmpty() ? module.getName() : dsl;
		} else return "System";
	}
}
