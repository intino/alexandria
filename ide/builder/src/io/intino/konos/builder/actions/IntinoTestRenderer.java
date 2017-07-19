package io.intino.konos.builder.actions;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.model.Activity;
import io.intino.konos.model.DataLake;
import io.intino.konos.model.Konos;
import io.intino.konos.model.Service;
import io.intino.konos.model.jms.JMSService;
import io.intino.konos.model.rest.RESTService;
import io.intino.konos.model.slackbot.SlackBotService;
import io.intino.tara.magritte.Graph;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.getSourceRoots;

public class IntinoTestRenderer {


	private Konos graph;
	private Module module;
	private PsiDirectory directory;
	private String newName;

	IntinoTestRenderer(Module module, PsiDirectory directory, String newName) {
		this.module = module;
		this.directory = directory;
		this.newName = newName;
		final Graph graph = GraphLoader.loadGraph(module);
		if (graph != null) this.graph = graph.wrapper(Konos.class);
	}


	public String execute() {
		if (graph == null) return null;
		final Frame frame = new Frame();
		frame.addTypes("intinoTest").addSlot("package", calculatePackage()).addSlot("name", newName);
		addRESTServices(frame);
		addJMSServices(frame);
		addSlackServices(frame);
		addDataLakes(frame);
		addMessageHandlers(frame);
		addActivities(frame);
		return template().format(frame);
	}

	private String calculatePackage() {
		final PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(directory);
		return aPackage == null ? "" : aPackage.getQualifiedName();
	}

	private void addRESTServices(Frame frame) {
		for (RESTService service : graph.rESTServiceList()) {
			Frame restFrame = new Frame().addTypes("service", "rest").addSlot("name", service.name());
			addUserVariables(service.as(Service.class), restFrame, findCustomParameters(service));
			frame.addSlot("service", restFrame);
		}
	}

	private void addJMSServices(Frame frame) {
		for (JMSService service : graph.jMSServiceList()) {
			Frame jmsFrame = new Frame().addTypes("service", "jms").addSlot("name", service.name());
			addUserVariables(service.as(Service.class), jmsFrame, findCustomParameters(service));
			frame.addSlot("service", jmsFrame);
		}
	}

	private void addDataLakes(Frame frame) {
		DataLake dataLake = graph.dataLake();
		if (dataLake == null) return;
		Frame dataLakeFrame = new Frame().addTypes("service", "dataLake").addSlot("name", dataLake.name());
		frame.addSlot("service", dataLakeFrame);
	}

	private void addMessageHandlers(Frame frame) {
		DataLake dataLake = graph.dataLake();
		if (dataLake == null) return;
		for (DataLake.Tank handler : dataLake.tankList()) {
			Frame channelFrame = new Frame().addTypes("service", "eventHandler").addSlot("name", handler.name());
			addUserVariables(handler, channelFrame, findCustomParameters(handler));
			frame.addSlot("service", channelFrame);
		}
	}

	private void addActivities(Frame frame) {
		for (Activity activity : graph.activityList()) {
			Frame activityFrame = new Frame().addTypes("service", "activity").addSlot("name", activity.name());
			frame.addSlot("service", activityFrame);
			if (activity.authenticated() != null) {
				activityFrame.addTypes("auth");
				activityFrame.addSlot("auth", activity.authenticated().by());
			}
			addUserVariables(activity, activityFrame, findCustomParameters(activity));
		}
	}

	private void addSlackServices(Frame frame) {
		for (SlackBotService service : graph.slackBotServiceList()) {
			frame.addSlot("service", new Frame().addTypes("service", "slack").addSlot("name", service.name()));
		}
	}

	private void addUserVariables(Layer layer, Frame frame, Collection<String> userVariables) {
		for (String custom : userVariables)
			frame.addSlot("custom", new Frame().addTypes("custom").addSlot("conf", layer.name()).addSlot("name", custom).addSlot("type", "String"));
	}

	private Set<String> findCustomParameters(DataLake.Tank channel) {
		Set<String> set = new LinkedHashSet<>();
		set.addAll(Commons.extractParameters(channel.topic()));
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

	private VirtualFile getTestRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "test".equals(file.getName())) return file;
		return null;
	}

	private Template template() {
		return Formatters.customize(IntinoTestTemplate.create());
	}

}
