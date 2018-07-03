package io.intino.konos.builder.actions;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.slackbot.SlackBotService;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.getSourceRoots;

public class IntinoTestRenderer {
	private KonosGraph graph;
	private PsiDirectory directory;
	private String newName;

	IntinoTestRenderer(Module module, PsiDirectory directory, String newName) {
		this.directory = directory;
		this.newName = newName;
		this.graph = new GraphLoader().loadGraph(module);
	}


	public String execute() {
		if (graph == null) return null;
		final Frame frame = new Frame();
		frame.addTypes("intinoTest").addSlot("package", calculatePackage()).addSlot("name", newName);
		addRESTServices(frame);
		addJMSServices(frame);
		addSlackServices(frame);
		addUIServices(frame);
		return template().format(frame);
	}

	private String calculatePackage() {
		final PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(directory);
		return aPackage == null ? "" : aPackage.getQualifiedName();
	}

	private void addRESTServices(Frame frame) {
		for (RESTService service : graph.rESTServiceList()) {
			Frame restFrame = new Frame().addTypes("service", "rest").addSlot("name", service.name$());
			addUserVariables(service.a$(Service.class), restFrame, findCustomParameters(service));
			frame.addSlot("service", restFrame);
		}
	}

	private void addJMSServices(Frame frame) {
		for (JMSService service : graph.jMSServiceList()) {
			Frame jmsFrame = new Frame().addTypes("service", "jms").addSlot("name", service.name$());
			addUserVariables(service.a$(Service.class), jmsFrame, findCustomParameters(service));
			frame.addSlot("service", jmsFrame);
		}
	}

	private void addUIServices(Frame frame) {
		for (UIService service : graph.uIServiceList()) {
			Frame serviceFrame = new Frame().addTypes("service", "ui").addSlot("name", service.name$());
			frame.addSlot("service", serviceFrame);
			if (service.authentication() != null) {
				serviceFrame.addTypes("auth");
				serviceFrame.addSlot("auth", service.authentication().by());
			}
			addUserVariables(service, serviceFrame, findCustomParameters(service));
		}
	}

	private void addSlackServices(Frame frame) {
		for (SlackBotService service : graph.slackBotServiceList()) {
			frame.addSlot("service", new Frame().addTypes("service", "slack").addSlot("name", service.name$()));
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

	private VirtualFile getTestRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "test".equals(file.getName())) return file;
		return null;
	}

	private Template template() {
		return Formatters.customize(IntinoTestTemplate.create());
	}

}
