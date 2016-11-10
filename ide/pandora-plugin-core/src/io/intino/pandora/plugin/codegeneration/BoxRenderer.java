package io.intino.pandora.plugin.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import io.intino.pandora.plugin.Channel;
import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.jms.JMSService;
import io.intino.pandora.plugin.rest.RESTService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

class BoxRenderer {

	private final File gen;
	private final String packageName;
	private final Module module;
	private final PandoraApplication application;
	private final Configuration configuration;

	BoxRenderer(Graph graph, File gen, String packageName, Module module) {
		application = graph.application();
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("box");
		final String name = name();
		frame.addSlot("name", name);
		frame.addSlot("package", packageName);
		if (configuration != null && !configuration.level().equals(Configuration.Level.Platform) && parentExists())
			frame.addSlot("parent", configuration.dsl());
		if (!application.rESTServiceList().isEmpty())
			frame.addSlot("hasREST", "");
		for (RESTService service : application.rESTServiceList())
			frame.addSlot("service", new Frame().addTypes("service", "rest").addSlot("name", service.name()));
		for (JMSService service : application.jMSServiceList())
			frame.addSlot("service", new Frame().addTypes("service", "jms").addSlot("name", service.name()).addSlot("configuration", name));
		for (Channel channel : application.channelList())
			frame.addSlot("channel", new Frame().addTypes("channel").addSlot("name", channel.name()).addSlot("configuration", name));
		if (module != null && TaraUtil.configurationOf(module) != null) frame.addSlot("tara", name);
		writeFrame(gen, snakeCaseToCamelCase(name) + "Box", template().format(frame));
	}

	private boolean parentExists() {
		final JavaPsiFacade facade = JavaPsiFacade.getInstance(module.getProject());
		String workingPackage = configuration.dslWorkingPackage();
		return workingPackage != null && facade.findClass(workingPackage + ".pandora." + configuration.dsl() + "Box", GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)) != null;
	}

	private String name() {
		if (module != null) {
			final String dsl = configuration.outDSL();
			if (dsl == null || dsl.isEmpty()) return module.getName();
			else return dsl;
		} else return "System";
	}


	private Template template() {
		Template template = BoxTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
