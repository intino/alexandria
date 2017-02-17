package io.intino.konos.builder.codegeneration.main;

import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
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
		this.configuration = module != null ? TaraUtil.configurationOf(module) : null;
	}

	public void execute() {
		if (configuration == null || !System.equals(configuration.level())) return;
		Frame frame = new Frame().addTypes("main").addSlot("package", packageName).addSlot("name", name());
		if (JavaPsiFacade.getInstance(module.getProject()).findClass("spark.Spark", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)) != null)
			frame.addSlot("rest", "");
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
