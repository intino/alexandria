package io.intino.pandora.plugin.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.pandora.plugin.Channel;
import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.Service;
import io.intino.pandora.plugin.jms.JMSService;
import io.intino.pandora.plugin.rest.RESTService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.extractParameters;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

class BoxConfigurationRenderer {

	private final PandoraApplication application;
	private final File gen;
	private final String packageName;
	private final Module module;

	BoxConfigurationRenderer(Graph graph, File gen, String packageName, Module module) {
		this.application = graph.application();
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("boxconfiguration");
		final String boxName = name();
		frame.addSlot("name", boxName);
		frame.addSlot("package", packageName);
		addRESTServices(frame, boxName);
		addJMSServices(frame, boxName);
		addChannels(frame, boxName);
		if (module != null && TaraUtil.configurationOf(module) != null) frame.addSlot("tara", "");
		writeFrame(gen, snakeCaseToCamelCase(boxName) + "Configuration", template().format(frame));

	}

	private void addRESTServices(Frame frame, String name) {
		for (RESTService service : application.rESTServiceList()) {
			Frame restFrame = new Frame().addTypes("service", "rest").addSlot("name", service.name()).addSlot("configuration", name);
			addCustomVariables(service.as(Service.class), restFrame);
			frame.addSlot("service", restFrame);
		}
	}

	private void addJMSServices(Frame frame, String name) {
		for (JMSService service : application.jMSServiceList()) {
			Frame jmsFrame = new Frame().addTypes("service", "jms").addSlot("name", service.name()).addSlot("configuration", name);
			addCustomVariables(service.as(Service.class), jmsFrame);
			frame.addSlot("service", jmsFrame);
		}
	}

	private void addChannels(Frame frame, String name) {
		for (Channel channel : application.channelList()) {
			Frame channelFrame = new Frame().addTypes("service", "channel").addSlot("name", channel.name()).addSlot("configuration", name);
			if (channel.isDurable()) channelFrame.addSlot("clientID", channel.asDurable().clientID());
			frame.addSlot("service", channelFrame);
		}
	}

	private void addCustomVariables(Service service, Frame jmsFrame) {
		for (String custom : findCustomParameters(service))
			jmsFrame.addSlot("custom", new Frame().addTypes("custom").addSlot("conf", service.name()).
					addSlot("name", custom).addSlot("type", "String"));
	}

	private Set<String> findCustomParameters(Service service) {
		return service.isJMS() ? findCustomParameters(service.asJMS()) : findCustomParameters(service.asREST());
	}

	private Set<String> findCustomParameters(JMSService service) {
		Set<String> list = new LinkedHashSet<>();
		for (JMSService.Request request : service.requestList())
			list.addAll(extractParameters(request.path()));
		return list;
	}

	private Set<String> findCustomParameters(RESTService service) {
		Set<String> list = new LinkedHashSet<>();
		for (RESTService.Resource resource : service.resourceList())
			list.addAll(extractParameters(resource.path()));
		return list;
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
