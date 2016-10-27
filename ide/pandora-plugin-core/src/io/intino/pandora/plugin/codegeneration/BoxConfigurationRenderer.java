package io.intino.pandora.plugin.codegeneration;

import com.intellij.openapi.module.Module;
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

public class BoxConfigurationRenderer {

	private final PandoraApplication application;
	private final File gen;
	private final String packageName;
	private final Module module;

	public BoxConfigurationRenderer(Graph graph,  File gen, String packageName, Module module) {
		this.application = graph.application();
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("boxconfiguration");
		final String name = name();
		frame.addSlot("name", name);
		frame.addSlot("package", packageName);
		for (RESTService service : application.rESTServiceList())
			frame.addSlot("service", new Frame().addTypes("service", "rest").addSlot("name", service.name()).addSlot("configuration", name));
		for (JMSService service : application.jMSServiceList())
			frame.addSlot("service", new Frame().addTypes("service", "jms").addSlot("name", service.name()).addSlot("configuration", name));
		if (module != null && TaraUtil.configurationOf(module) != null) frame.addSlot("tara", "");
		writeFrame(gen, snakeCaseToCamelCase(name) + "Configuration", template().format(frame));

	}

	private String name() {
		if (module != null) {
			final Configuration configuration = TaraUtil.configurationOf(module);
			final String dsl = configuration.outDSL();
			if (dsl == null || dsl.isEmpty()) return module.getName();
			else return dsl;
		} else return "System";
	}


	private Template template() {
		Template template = BoxConfigurationTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
