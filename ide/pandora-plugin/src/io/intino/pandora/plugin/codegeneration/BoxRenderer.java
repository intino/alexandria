package io.intino.pandora.plugin.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.pandora.plugin.Activity;
import io.intino.pandora.plugin.Channel;
import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.jms.JMSService;
import io.intino.pandora.plugin.jmx.JMXService;
import io.intino.pandora.plugin.rest.RESTService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class BoxRenderer {

	private final File gen;
	private final String packageName;
	private final Module module;
	private final PandoraApplication application;
	private final Configuration configuration;
	private boolean parentExists;

	public BoxRenderer(Graph graph, File gen, String packageName, Module module, boolean parentExists) {
		application = graph.application();
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.parentExists = parentExists;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("box");
		final String name = name();
		frame.addSlot("name", name);
		frame.addSlot("package", packageName);
		if (configuration != null && !configuration.level().equals(Configuration.Level.Platform) && parentExists) {
			frame.addSlot("parent", configuration.dsl());
			frame.addSlot("parentPackage", configuration.dslWorkingPackage());
			frame.addSlot("hasParent", "");
		}
		for (RESTService service : application.rESTServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "rest").addSlot("name", service.name()));
		for (JMSService service : application.jMSServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jms").addSlot("name", service.name()).addSlot("configuration", name));
		for (JMXService service : application.jMXServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jmx").addSlot("name", service.name()).addSlot("configuration", name));
		for (Channel channel : application.channelList()) {
			final Frame channelFrame = new Frame().addTypes("channel").addSlot("name", channel.name());
			if (channel.isDurable()) channelFrame.addSlot("durable", channel.name());
			frame.addSlot("channel", (Frame) channelFrame.addSlot("configuration", name));
		}
		for (Activity activity : application.activityList())
			frame.addSlot("activity", (Frame) activityFrame(activity));
		if (module != null && TaraUtil.configurationOf(module) != null) frame.addSlot("tara", name);
		writeFrame(gen, snakeCaseToCamelCase(name) + "Box", template().format(frame));
	}

	private Frame activityFrame(Activity activity) {
		Frame frame = new Frame();
		frame.addTypes("activity").addSlot("name", activity.name());
		return frame;
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
