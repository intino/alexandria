package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.slackbot.SlackBotService;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class AbstractBoxRenderer {

	private final File gen;
	private final String packageName;
	private final Module module;
	private final KonosGraph konos;
	private final Configuration configuration;
	private String parent;
	private final boolean hasModel;

	public AbstractBoxRenderer(KonosGraph graph, File gen, String packageName, Module module, String parent, boolean hasModel) {
		konos = graph;
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		this.configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.parent = parent;
		this.hasModel = hasModel;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("box");
		final String name = name();
		frame.addSlot("name", name);
		frame.addSlot("package", packageName);
		if (hasModel) frame.addSlot("tara", name);
		parent(frame);
		services(frame, name);
		tasks(frame, name);
		dataLake(frame, name);
		activities(frame, name);
		Commons.writeFrame(gen, "AbstractBox", template().format(frame));
	}


	private void activities(Frame frame, String name) {
		List<Activity> activities = konos.activityList();
		if (!activities.isEmpty()) {
			final Frame activityFrame = new Frame();
			if (parent != null) activityFrame.addSlot("parent", parent);
			frame.addSlot("hasActivity", activityFrame);
		}

		for (Activity activity : activities)
			frame.addSlot("activity", (Frame) activityFrame(activity, name));
	}

	private void tasks(Frame frame, String name) {
		if (!konos.taskList().isEmpty())
			frame.addSlot("task", new Frame().addTypes("task").addSlot("configuration", name));
	}

	private void dataLake(Frame frame, String name) {
		if (konos.dataLake() != null)
			frame.addSlot("dataLake", (Frame) new Frame().addTypes("dataLake").addSlot("name", konos.dataLake().name$()).addSlot("package", packageName).addSlot("configuration", name));
	}

	private void services(Frame frame, String name) {
		if (!konos.jMSServiceList().isEmpty()) frame.addSlot("jms", "");
		for (RESTService service : konos.rESTServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "rest").addSlot("name", service.name$()).addSlot("configuration", name));
		for (JMSService service : konos.jMSServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jms").addSlot("name", service.name$()).addSlot("configuration", name));
		for (JMXService service : konos.jMXServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jmx").addSlot("name", service.name$()).addSlot("configuration", name));
		for (SlackBotService service : konos.slackBotServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "slack").addSlot("name", service.name$()).addSlot("configuration", name));
		if (!konos.rESTServiceList().isEmpty() || !konos.activityList().isEmpty()) frame.addSlot("spark", "stop");
	}

	private void parent(Frame frame) {
		if (parent != null && configuration != null && !Platform.equals(configuration.level()))
			frame.addSlot("parent", parent).addSlot("hasParent", "");
		else frame.addSlot("hasntParent", "");
	}

	private Frame activityFrame(Activity activity, String name) {
		Frame frame = new Frame();
		frame.addTypes("activity").addSlot("name", activity.name$()).addSlot("configuration", name);
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
		return Formatters.customize(AbstractBoxTemplate.create());
	}
}
