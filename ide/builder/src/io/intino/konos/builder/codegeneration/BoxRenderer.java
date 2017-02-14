package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Activity;
import io.intino.konos.model.Bus;
import io.intino.konos.model.Konos;
import io.intino.konos.model.jms.JMSService;
import io.intino.konos.model.jmx.JMXService;
import io.intino.konos.model.rest.RESTService;
import io.intino.konos.model.slackbot.SlackBotService;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.magritte.Graph;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class BoxRenderer {

	private final File gen;
	private final String packageName;
	private final Module module;
	private final Konos konos;
	private final Configuration configuration;
	private String parent;
	private final boolean isTara;

	public BoxRenderer(Graph graph, File gen, String packageName, Module module, String parent, boolean isTara) {
		konos = graph.wrapper(Konos.class);
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.parent = parent;
		this.isTara = isTara;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("box");
		final String name = name();
		frame.addSlot("name", name);
		frame.addSlot("package", packageName);
		if (module != null && isTara) frame.addSlot("tara", name);
		parent(frame);
		services(frame, name);
		tasks(frame, name);
		bus(frame, name);
		activities(frame);
		Commons.writeFrame(gen, snakeCaseToCamelCase(name) + "Box", template().format(frame));
	}


	private void activities(Frame frame) {
		for (Activity activity : konos.activityList())
			frame.addSlot("activity", (Frame) activityFrame(activity));
	}

	private void tasks(Frame frame, String name) {
		if (!konos.taskList().isEmpty())
			frame.addSlot("task", new Frame().addTypes("task"));
	}

	private void bus(Frame frame, String name) {
		for (Bus bus : konos.busList())
			frame.addSlot("bus", (Frame) new Frame().addTypes("bus").addSlot("name", bus.name()).addSlot("package", packageName).addSlot("configuration", name));
	}

	private void services(Frame frame, String name) {
		if (!konos.jMSServiceList().isEmpty()) frame.addSlot("jms", "");
		for (RESTService service : konos.rESTServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "rest").addSlot("name", service.name()));
		for (JMSService service : konos.jMSServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jms").addSlot("name", service.name()).addSlot("configuration", name));
		for (JMXService service : konos.jMXServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jmx").addSlot("name", service.name()).addSlot("configuration", name));
		for (SlackBotService service : konos.slackBotServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "slack").addSlot("name", service.name()).addSlot("configuration", name));
	}

	private void parent(Frame frame) {
		if (parent != null && configuration != null && !Platform.equals(configuration.level()))
			frame.addSlot("parent", parent).addSlot("hasParent", "");
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
