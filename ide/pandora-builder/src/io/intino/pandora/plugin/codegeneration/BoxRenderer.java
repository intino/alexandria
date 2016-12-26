package io.intino.pandora.plugin.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.pandora.model.Activity;
import io.intino.pandora.model.Channel;
import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.model.jms.JMSService;
import io.intino.pandora.model.jmx.JMXService;
import io.intino.pandora.model.rest.RESTService;
import io.intino.pandora.plugin.helpers.Commons;
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
		if (module != null && TaraUtil.configurationOf(module) != null) frame.addSlot("tara", name);
		parent(frame);
		services(frame, name);
		channels(frame, name);
		activities(frame);
		writeFrame(gen, snakeCaseToCamelCase(name) + "Box", template().format(frame));
	}

	private void activities(Frame frame) {
		for (Activity activity : application.activityList())
			frame.addSlot("activity", (Frame) activityFrame(activity));
	}

	private void channels(Frame frame, String name) {
		for (Channel channel : application.channelList()) {
			final Frame channelFrame = new Frame().addTypes("channel").addSlot("name", channel.name());
			if (channel.isDurable()) channelFrame.addSlot("durable", customizeDurable(channel.asDurable().clientID(), channel.name()));
			frame.addSlot("channel", (Frame) channelFrame.addSlot("configuration", name));
		}
	}

	private Frame customizeDurable(String clientId, String channelName) {
		Frame frame = new Frame().addTypes("durable");
		frame.addSlot("channel", channelName);
		for (String parameter : Commons.extractParameters(clientId)) {
			frame.addSlot("custom", new Frame().addSlot("value", parameter).addSlot("channel", channelName));
		}
		return frame;
	}

	private void services(Frame frame, String name) {
		for (RESTService service : application.rESTServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "rest").addSlot("name", service.name()));
		for (JMSService service : application.jMSServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jms").addSlot("name", service.name()).addSlot("configuration", name));
		for (JMXService service : application.jMXServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jmx").addSlot("name", service.name()).addSlot("configuration", name));
	}

	private void parent(Frame frame) {
		if (parentExists && configuration != null && !Configuration.Level.Platform.equals(configuration.level())) {
			frame.addSlot("parent", configuration.dsl());
			frame.addSlot("parentPackage", configuration.dslWorkingPackage());
			frame.addSlot("hasParent", "");
		}
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
		return Formatters.customize(BoxTemplate.create());
	}
}
