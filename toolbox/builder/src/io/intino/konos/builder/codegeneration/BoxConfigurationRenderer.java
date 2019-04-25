package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class BoxConfigurationRenderer {

	private final KonosGraph graph;
	private final File gen;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;
	private String parent;
	private boolean isTara;

	public BoxConfigurationRenderer(KonosGraph graph, File gen, String packageName, Module module, String parent, boolean isTara) {
		this.graph = graph;
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.parent = parent;
		this.isTara = isTara;
	}

	public Frame execute() {
		Frame frame = new Frame().addTypes("boxconfiguration");
		final String boxName = fillFrame(frame);
		Commons.writeFrame(gen, snakeCaseToCamelCase(boxName) + "Configuration", template().format(frame));
		return frame;
	}

	private String fillFrame(Frame frame) {
		final String boxName = name();
		frame.addSlot("name", boxName);
		frame.addSlot("package", packageName);
		if (parent != null && configuration != null && !Platform.equals(configuration.level())) frame.addSlot("parent", parent);
		if (isTara) frame.addSlot("tara", "");
		return boxName;
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
		return Formatters.customize(BoxConfigurationTemplate.create());
	}
}