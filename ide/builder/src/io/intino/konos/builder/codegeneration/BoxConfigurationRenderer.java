package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.DataLake;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.slackbot.SlackBotService;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.compiler.shared.Configuration;
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
		addRESTServices(frame, boxName);
		addJMSServices(frame, boxName);
		addSlackServices(frame, boxName);
		addDataLakes(frame, boxName);
		addEventHandlers(frame, boxName);
		addActivities(frame, boxName);
		if (isTara) frame.addSlot("tara", "");
		return boxName;
	}

	private void addRESTServices(Frame frame, String boxName) {
		for (RESTService service : graph.rESTServiceList()) {
			Frame restFrame = new Frame().addTypes("service", "rest").addSlot("name", service.name$()).addSlot("configuration", boxName);
			if (service.authenticated() != null) restFrame.addTypes("auth");
			addUserVariables(service.a$(Service.class), restFrame, findCustomParameters(service));
			frame.addSlot("service", restFrame);
		}
	}

	private void addJMSServices(Frame frame, String boxName) {
		for (JMSService service : graph.jMSServiceList()) {
			Frame jmsFrame = new Frame().addTypes("service", "jms").addSlot("name", service.name$()).addSlot("configuration", boxName);
			addUserVariables(service.a$(Service.class), jmsFrame, findCustomParameters(service));
			frame.addSlot("service", jmsFrame);
		}
	}

	private void addJMXServices(Frame frame, String boxName) {
		for (JMXService service : graph.jMXServiceList()) {
			Frame jmsFrame = new Frame().addTypes("service", "jmx").addSlot("name", service.name$()).addSlot("configuration", boxName);
			frame.addSlot("service", jmsFrame);
		}
	}

	private void addDataLakes(Frame frame, String boxName) {
		DataLake dataLake = graph.dataLake();
		if (dataLake == null) return;
		Frame dataLakeFrame = new Frame().addTypes("service", "dataLake").addSlot("name", dataLake.name$()).addSlot("configuration", boxName);
		frame.addSlot("service", dataLakeFrame);
	}

	private void addEventHandlers(Frame frame, String boxName) {
		DataLake dataLake = graph.dataLake();
		if (dataLake == null) return;
		for (DataLake.Tank handler : dataLake.tankList()) {
			Frame channelFrame = new Frame().addTypes("service", "eventHandler").addSlot("name", handler.schema() != null ? handler.schema().name$() : handler.name$()).addSlot("configuration", boxName);
			frame.addSlot("service", channelFrame);
		}
	}

	private void addActivities(Frame frame, String boxName) {
		for (UIService service : graph.uIServiceList()) {
			Frame activityFrame = new Frame().addTypes("service", "ui").addSlot("name", service.name$()).addSlot("configuration", boxName);
			frame.addSlot("service", activityFrame);
			if (service.authentication() != null) {
				activityFrame.addTypes("auth");
				activityFrame.addSlot("authURL", new Frame().addSlot("name", service.name$()).addSlot("configuration", boxName));
				activityFrame.addSlot("auth", service.authentication().by());
			}
			addUserVariables(service, activityFrame, findCustomParameters(service));
		}
	}

	private void addSlackServices(Frame frame, String boxName) {
		for (SlackBotService service : graph.slackBotServiceList()) {
			frame.addSlot("service", new Frame().addTypes("service", "slack").addSlot("name", service.name$()).addSlot("configuration", boxName));
		}
	}

	private void addUserVariables(Layer layer, Frame frame, Collection<String> userVariables) {
		for (String custom : userVariables)
			frame.addSlot("custom", new Frame().addTypes("custom").addSlot("conf", layer.name$()).addSlot("name", custom).addSlot("type", "String"));
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

	private Set<String> findCustomParameters(UIService service) {
		Set<String> set = new LinkedHashSet<>();
		if (service.authentication() != null)
			set.addAll(Commons.extractParameters(service.authentication().by()));
		for (UIService.Resource resource : service.resourceList())
			set.addAll(Commons.extractParameters(resource.path()));
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
