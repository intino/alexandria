package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.ness.NessClient;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.slackbot.SlackBotService;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class AbstractBoxRenderer {
	private final File gen;
	private final String packageName;
	private final Module module;
	private final KonosGraph konos;
	private final Configuration configuration;
	private String parent;
	private final boolean hasModel;

	AbstractBoxRenderer(KonosGraph graph, File gen, String packageName, Module module, String parent, boolean hasModel) {
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
		Commons.writeFrame(gen, "AbstractBox", template().format(frame));
	}

	private void tasks(Frame frame, String name) {
		if (!konos.taskList().isEmpty())
			frame.addSlot("task", new Frame().addTypes("task").addSlot("configuration", name));
	}

	private void dataLake(Frame root, String name) {
		if (!konos.nessClientList().isEmpty()) {
			final NessClient client = konos.nessClientList().get(0);
			final Frame datalake = new Frame().addTypes("dataLake").addSlot("mode", konos.nessClient(0).mode().name()).addSlot("name", konos.nessClient(0).name$()).addSlot("package", packageName).addSlot("configuration", name);
			datalake.addSlot("parameter", new Frame(isCustom(client.url()) ? "custom" : "standard").addSlot("value", client.url()));
			datalake.addSlot("parameter", new Frame(isCustom(client.user()) ? "custom" : "standard").addSlot("value", client.user()));
			datalake.addSlot("parameter", new Frame(isCustom(client.password()) ? "custom" : "standard").addSlot("value", client.password()));
			datalake.addSlot("parameter", new Frame(isCustom(client.clientID()) ? "custom" : "standard").addSlot("value", client.clientID()));
			root.addSlot("dataLake", datalake);
		}
	}

	private void services(Frame frame, String name) {
		if (!konos.jMSServiceList().isEmpty()) frame.addSlot("jms", "");
		for (RESTService service : konos.rESTServiceList()) {
			final Frame serviceFrame = new Frame().addTypes("service", "rest").addSlot("name", service.name$()).addSlot("configuration", name);
			serviceFrame.addSlot("parameter", new Frame(isCustom(service.port()) ? "custom" : "standard").addSlot("value", service.port()));
			frame.addSlot("service", (Frame) serviceFrame);
		}
		for (JMSService service : konos.jMSServiceList()) {
			final Frame serviceFrame = new Frame().addTypes("service", "jms").addSlot("name", service.name$()).addSlot("configuration", name);
			serviceFrame.addSlot("parameter", new Frame(isCustom(service.user()) ? "custom" : "standard").addSlot("value", service.user()));
			serviceFrame.addSlot("parameter", new Frame(isCustom(service.password()) ? "custom" : "standard").addSlot("value", service.password()));
			serviceFrame.addSlot("parameter", new Frame(isCustom(service.url()) ? "custom" : "standard").addSlot("value", service.url()));
			frame.addSlot("service", (Frame) serviceFrame);
		}
		for (JMXService service : konos.jMXServiceList())
			frame.addSlot("service", (Frame) new Frame().addTypes("service", "jmx").addSlot("name", service.name$()).addSlot("configuration", name));
		for (SlackBotService service : konos.slackBotServiceList()) {
			final Frame slackFrame = new Frame().addTypes("service", "slack").addSlot("name", service.name$()).addSlot("configuration", name);
			slackFrame.addSlot("parameter", new Frame(isCustom(service.token()) ? "custom" : "standard").addSlot("value", service.token()));
			frame.addSlot("service", (Frame) slackFrame);
		}
		if (!konos.rESTServiceList().isEmpty() || !konos.uIServiceList().isEmpty()) frame.addSlot("spark", "stop");
		if (!konos.uIServiceList().isEmpty()) {
			final Frame uiFrame = new Frame();
			if (parent != null) uiFrame.addSlot("parent", parent);
			frame.addSlot("hasUi", uiFrame);
			frame.addSlot("uiAuthentication", uiFrame);
			frame.addSlot("uiEdition", uiFrame);
			frame.addSlot("service", konos.uIServiceList().stream().map(s -> uiServiceFrame(s, name)).toArray(Frame[]::new));
		}
	}

	private Frame uiServiceFrame(UIService service, String name) {
		final Frame frame = new Frame().addTypes("service", "ui").addSlot("name", service.name$()).addSlot("configuration", name)
				.addSlot("parameter", new Frame(isCustom(service.port()) ? "custom" : "standard").addSlot("value", service.port()));
		if (service.authentication() != null)
			frame.addSlot("authentication", new Frame(isCustom(service.authentication().by()) ? "custom" : "standard").addSlot("value", service.authentication().by()));
		if (service.edition() != null)
			frame.addSlot("edition", new Frame(isCustom(service.edition().by()) ? "custom" : "standard").addSlot("value", service.edition().by()));
		return frame;

	}

	private boolean isCustom(String value) {
		return value!= null && value.startsWith("{");
	}

	private void parent(Frame frame) {
		if (parent != null && configuration != null && !Platform.equals(configuration.level()))
			frame.addSlot("parent", parent).addSlot("hasParent", "");
		else frame.addSlot("hasntParent", "");
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
