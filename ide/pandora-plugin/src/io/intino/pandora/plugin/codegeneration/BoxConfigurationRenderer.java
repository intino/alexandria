package io.intino.pandora.plugin.codegeneration;

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
import tara.magritte.Layer;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.extractParameters;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

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

	private void addRESTServices(Frame frame, String boxName) {
		for (RESTService service : application.rESTServiceList()) {
			Frame restFrame = new Frame().addTypes("service", "rest").addSlot("name", service.name()).addSlot("configuration", boxName);
			addUserVariables(service.as(Service.class), restFrame, findCustomParameters(service));
			frame.addSlot("service", restFrame);
		}
	}

	private void addJMSServices(Frame frame, String boxName) {
		for (JMSService service : application.jMSServiceList()) {
			Frame jmsFrame = new Frame().addTypes("service", "jms").addSlot("name", service.name()).addSlot("configuration", boxName);
			addUserVariables(service.as(Service.class), jmsFrame, findCustomParameters(service));
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
			addUserVariables(channel, channelFrame, findCustomParameters(channel));
			if (channel.isDurable()) channelFrame.addSlot("clientID", channel.asDurable().clientID());
			frame.addSlot("service", channelFrame);
		}
	}

	private void addActivities(Frame frame, String boxName) {
		for (Activity activity : application.activityList()) {
			Frame activityFrame = new Frame().addTypes("service", "activity").addSlot("name", activity.name()).addSlot("configuration", boxName);
			frame.addSlot("service", activityFrame);
			if (activity.authenticated() != null) activityFrame.addSlot("auth", activity.authenticated().by());
			addUserVariables(activity, activityFrame, findCustomParameters(activity));
		}
	}

	private void addUserVariables(Layer activity, Frame frame, Collection<String> userVariables) {
		for (String custom : userVariables)
			frame.addSlot("custom", new Frame().addTypes("custom").addSlot("conf", activity.name()).addSlot("name", custom).addSlot("type", "String"));
	}

	private Set<String> findCustomParameters(Channel channel) {
		Set<String> set = new LinkedHashSet<>();
		set.addAll(extractParameters(channel.path()));
		if (channel.isDurable()) set.addAll(extractParameters(channel.asDurable().clientID()));
		return set;
	}

	private Set<String> findCustomParameters(JMSService service) {
		Set<String> set = new LinkedHashSet<>();
		for (JMSService.Request request : service.requestList())
			set.addAll(extractParameters(request.path()));
		return set;
	}

	private Set<String> findCustomParameters(RESTService service) {
		Set<String> set = new LinkedHashSet<>();
		for (RESTService.Resource resource : service.resourceList())
			set.addAll(extractParameters(resource.path()));
		return set;
	}

	private Set<String> findCustomParameters(Activity activity) {
		Set<String> set = new LinkedHashSet<>();
		if (activity.authenticated() != null)
			set.addAll(extractParameters(activity.authenticated().by()));
		return set;
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
		template.add("validname", value -> snakeCaseToCamelCase(value.toString().replace(".", "-")));
		return template;
	}
}
