package io.intino.pandora.model.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.pandora.model.Activity;
import io.intino.pandora.model.Channel;
import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.model.Service;
import io.intino.pandora.model.jms.JMSService;
import io.intino.pandora.model.jmx.JMXService;
import io.intino.pandora.model.rest.RESTService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.model.helpers.Commons.extractParameters;
import static io.intino.pandora.model.helpers.Commons.writeFrame;

public class BoxConfigurationRenderer {

	private final PandoraApplication application;
	private final File gen;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;
	private boolean parentExists;

	public BoxConfigurationRenderer(Graph graph, File gen, String packageName, Module module, boolean parentExists) {
		this.application = graph.application();
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.parentExists = parentExists;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("boxconfiguration");
		final String boxName = name();
		frame.addSlot("name", boxName);
		frame.addSlot("package", packageName);
		if (configuration != null && !configuration.level().equals(Configuration.Level.Platform) && parentExists) {
			frame.addSlot("parent", configuration.dsl());
			frame.addSlot("parentPackage", configuration.dslWorkingPackage());
		}
		addRESTServices(frame, boxName);
		addJMSServices(frame, boxName);
		addJMXServices(frame, boxName);
		addChannels(frame, boxName);
		addActivities(frame, boxName);
		if (module != null && TaraUtil.configurationOf(module) != null) frame.addSlot("tara", "");
		writeFrame(gen, snakeCaseToCamelCase(boxName) + "Configuration", template().format(frame));

	}

	private void addActivities(Frame frame, String boxName) {
		for (Activity activity : application.activityList()) {
			Frame restFrame = new Frame().addTypes("service", "activity").addSlot("name", activity.name()).addSlot("configuration", boxName);
			frame.addSlot("service", restFrame);
		}
	}

	private void addRESTServices(Frame frame, String boxName) {
		for (RESTService service : application.rESTServiceList()) {
			Frame restFrame = new Frame().addTypes("service", "rest").addSlot("name", service.name()).addSlot("configuration", boxName);
			addUserVariables(service.as(Service.class), restFrame);
			frame.addSlot("service", restFrame);
		}
	}

	private void addJMSServices(Frame frame, String boxName) {
		for (JMSService service : application.jMSServiceList()) {
			Frame jmsFrame = new Frame().addTypes("service", "jms").addSlot("name", service.name()).addSlot("configuration", boxName);
			addUserVariables(service.as(Service.class), jmsFrame);
			frame.addSlot("service", jmsFrame);
		}
	}

	private void addJMXServices(Frame frame, String boxName) {
		for (JMXService service : application.jMXServiceList()) {
			Frame jmsFrame = new Frame().addTypes("service", "jmx").addSlot("name", service.name()).addSlot("configuration", boxName);
			frame.addSlot("service", jmsFrame);
		}
	}

	private void addChannels(Frame frame, String boxName) {
		for (Channel channel : application.channelList()) {
			Frame channelFrame = new Frame().addTypes("service", "channel").addSlot("name", channel.name()).addSlot("configuration", boxName);
			if (channel.isDurable()) channelFrame.addSlot("clientID", channel.asDurable().clientID());
			frame.addSlot("service", channelFrame);
		}
	}

	private void addUserVariables(Service service, Frame jmsFrame) {
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
