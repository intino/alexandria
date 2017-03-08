package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Activity;
import io.intino.konos.model.Bus;
import io.intino.konos.model.Konos;
import io.intino.konos.model.Service;
import io.intino.konos.model.jms.JMSService;
import io.intino.konos.model.jmx.JMXService;
import io.intino.konos.model.rest.RESTService;
import io.intino.konos.model.slackbot.SlackBotService;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.magritte.Graph;
import io.intino.tara.magritte.Layer;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.tara.compiler.shared.Configuration.Level.Platform;

public class BoxConfigurationRenderer {

	private final Konos application;
	private final File gen;
	private final String packageName;
	private final Module module;
	private final Configuration configuration;
	private String parent;

	public BoxConfigurationRenderer(Graph graph, File gen, String packageName, Module module, String parent) {
		this.application = graph.wrapper(Konos.class);
		this.gen = gen;
		this.packageName = packageName;
		this.module = module;
		configuration = module != null ? TaraUtil.configurationOf(module) : null;
		this.parent = parent;
	}

	public Frame execute() {
		Frame frame = new Frame().addTypes("boxconfiguration");
		final String boxName = name();
		frame.addSlot("name", boxName);
		frame.addSlot("package", packageName);
		if (parent != null && configuration != null && !Platform.equals(configuration.level()))
			frame.addSlot("parent", parent);
		addRESTServices(frame, boxName);
		addJMSServices(frame, boxName);
		addJMXServices(frame, boxName);
		addSlackServices(frame, boxName);
		addBuses(frame, boxName);
		addEventHandlers(frame, boxName);
		addActivities(frame, boxName);
		if (module != null && TaraUtil.configurationOf(module) != null) frame.addSlot("tara", "");
		Commons.writeFrame(gen, snakeCaseToCamelCase(boxName) + "Configuration", template().format(frame));
		return frame;

	}

	private void addRESTServices(Frame frame, String boxName) {
		for (RESTService service : application.rESTServiceList()) {
			Frame restFrame = new Frame().addTypes("service", "rest").addSlot("name", service.name()).addSlot("configuration", boxName);
			if (service.authenticated() != null) restFrame.addTypes("auth");
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

	private void addBuses(Frame frame, String boxName) {
		Bus bus = application.bus();
		if (bus == null) return;
		Frame busFrame = new Frame().addTypes("service", "bus").addSlot("name", bus.name()).addSlot("configuration", boxName);
		frame.addSlot("service", busFrame);
	}

	private void addEventHandlers(Frame frame, String boxName) {
		Bus bus = application.bus();
		if (bus == null) return;
		for (Bus.EventHandler handler : bus.eventHandlerList()) {
			Frame channelFrame = new Frame().addTypes("service", "eventHandler").addSlot("name", handler.name()).addSlot("configuration", boxName);
			addUserVariables(handler, channelFrame, findCustomParameters(handler));
			if (handler.isDurable()) channelFrame.addSlot("clientID", handler.asDurable().clientID());
			frame.addSlot("service", channelFrame);
		}
	}

	private void addActivities(Frame frame, String boxName) {
		for (Activity activity : application.activityList()) {
			Frame activityFrame = new Frame().addTypes("service", "activity").addSlot("name", activity.name()).addSlot("configuration", boxName);
			frame.addSlot("service", activityFrame);
			if (activity.authenticated() != null) {
				activityFrame.addTypes("auth");
				activityFrame.addSlot("auth", activity.authenticated().by());
			}
			addUserVariables(activity, activityFrame, findCustomParameters(activity));
		}
	}

	private void addSlackServices(Frame frame, String boxName) {
		for (SlackBotService service : application.slackBotServiceList()) {
			frame.addSlot("service", new Frame().addTypes("service", "slack").addSlot("name", service.name()).addSlot("configuration", boxName));
		}
	}

	private void addUserVariables(Layer layer, Frame frame, Collection<String> userVariables) {
		for (String custom : userVariables)
			frame.addSlot("custom", new Frame().addTypes("custom").addSlot("conf", layer.name()).addSlot("name", custom).addSlot("type", "String"));
	}

	private Set<String> findCustomParameters(Bus.EventHandler channel) {
		Set<String> set = new LinkedHashSet<>();
		set.addAll(Commons.extractParameters(channel.topic()));
		if (channel.isDurable()) set.addAll(Commons.extractParameters(channel.asDurable().clientID()));
		return set;
	}

	private Set<String> findCustomParameters(JMSService service) {
		Set<String> set = new LinkedHashSet<>();
		for (JMSService.Request request : service.requestList())
			set.addAll(Commons.extractParameters(request.path()));
		return set;
	}

	private Set<String> findCustomParameters(RESTService service) {
		Set<String> set = new LinkedHashSet<>();
		for (RESTService.Resource resource : service.resourceList())
			set.addAll(Commons.extractParameters(resource.path()));
		return set;
	}

	private Set<String> findCustomParameters(Activity activity) {
		Set<String> set = new LinkedHashSet<>();
		if (activity.authenticated() != null)
			set.addAll(Commons.extractParameters(activity.authenticated().by()));
		for (Activity.AbstractPage page : activity.abstractPageList())
			for (String path : page.paths()) set.addAll(Commons.extractParameters(path));
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
		return Formatters.customize(BoxConfigurationTemplate.create());
	}
}
