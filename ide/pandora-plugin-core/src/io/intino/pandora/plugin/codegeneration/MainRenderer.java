package io.intino.pandora.plugin.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.pandora.plugin.PandoraApplication;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class MainRenderer {

	private final File src;
	private final String packageName;
	private final Module module;
	private final PandoraApplication application;
	private final Configuration configuration;

	public MainRenderer(Graph graph, File src, String packageName, Module module) {
		application = graph.application();
		this.src = src;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("main");
		if (configuration != null && configuration.level().equals(Configuration.Level.System))
			writeFrame(src, "Main", template().format(frame));
	}


	private Template template() {
		Template template = MainTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
