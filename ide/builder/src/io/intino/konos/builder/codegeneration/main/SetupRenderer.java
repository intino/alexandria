package io.intino.konos.builder.codegeneration.main;

import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.dsl.Proteo;
import io.intino.tara.dsl.Verso;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SetupRenderer {


	private final File destination;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;
	private boolean isTara;

	public SetupRenderer(File destination, String packageName, Module module, boolean isTara) {
		this.destination = destination;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.isTara = isTara;
	}

	public void execute() {
		if (configuration == null || Commons.javaFile(destination, "Setup").exists())
			return;
		final String name = name();
		Frame frame = new Frame().addTypes("Setup").addSlot("package", packageName).addSlot("name", name);
		if (isTara) frame.addSlot("tara", fillTara());
		if (JavaPsiFacade.getInstance(module.getProject()).findClass("io.intino.konos.server.activity.services.AuthService", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)) != null)
			frame.addSlot("rest", name);
		Commons.writeFrame(destination, "Setup", template().format(frame));
	}

	private Frame fillTara() {
		Frame frame = new Frame();
		frame.addSlot("name", name());
		if (configuration.outDSL() != null) frame.addSlot("outDSL", configuration.outDSL());
		frame.addSlot("wrapper", dsls());
		return null;
	}

	private String[] dsls() {
		List<String> dsls = new ArrayList<>();
		for (Configuration.LanguageLibrary lang : configuration.languages())
			if (!lang.name().equals(Verso.class.getSimpleName()) && !lang.name().equals(Proteo.class.getSimpleName()))
				dsls.add(lang.generationPackage().toLowerCase() + "." + Formatters.firstUpperCase(lang.name()));
		if (configuration.level() != Configuration.Level.System)
			dsls.add(configuration.workingPackage().toLowerCase() + "." + Formatters.firstUpperCase(configuration.outDSL()));
		return dsls.toArray(new String[dsls.size()]);
	}

	private Template template() {
		return Formatters.customize(SetupTemplate.create());
	}

	private String name() {
		if (module != null) {
			final String dsl = configuration.outDSL();
			return dsl == null || dsl.isEmpty() ? module.getName() : dsl;
		} else return "System";
	}
}
