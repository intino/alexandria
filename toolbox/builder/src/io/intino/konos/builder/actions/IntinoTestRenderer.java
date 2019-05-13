package io.intino.konos.builder.actions;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
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
		final FrameBuilder builder = new FrameBuilder();
		builder.add("intinoTest").add("package", calculatePackage()).add("name", newName);
		addRESTServices(builder);
		addJMSServices(builder);
		addSlackServices(builder);
		addUIServices(builder);
		return template().render(builder);
	}

	private String calculatePackage() {
		final PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(directory);
		return aPackage == null ? "" : aPackage.getQualifiedName();
	}

	private void addRESTServices(FrameBuilder builder) {
		for (RESTService service : graph.rESTServiceList()) {
			FrameBuilder restBuilder = new FrameBuilder("service").add("rest").add("name", service.name$());
			addUserVariables(service.a$(Service.class), restBuilder, findCustomParameters(service));
			builder.add("service", restBuilder.toFrame());
		}
	}

	private void addJMSServices(FrameBuilder builder) {
		for (JMSService service : graph.jMSServiceList()) {
			FrameBuilder jmsBuilder = new FrameBuilder("service").add("jms").add("name", service.name$());
			addUserVariables(service.a$(Service.class), jmsBuilder, findCustomParameters(service));
			builder.add("service", jmsBuilder.toFrame());
		}
	}

	private void addUIServices(FrameBuilder builder) {
		for (UIService service : graph.uIServiceList()) {
			FrameBuilder serviceBuilder = new FrameBuilder("service").add("ui").add("name", service.name$());
			builder.add("service", serviceBuilder);
			if (service.authentication() != null) {
				serviceBuilder.add("auth");
				serviceBuilder.add("auth", service.authentication().by());
			}
			addUserVariables(service, serviceBuilder, findCustomParameters(service));
		}
	}

	private void addSlackServices(FrameBuilder builder) {
		for (SlackBotService service : graph.slackBotServiceList()) {
			builder.add("service", new FrameBuilder("service").add("slack").add("name", service.name$()));
		}
	}

	private void addUserVariables(Layer layer, FrameBuilder builder, Collection<String> userVariables) {
		for (String custom : userVariables)
			builder.add("custom", new FrameBuilder("custom").add("conf", layer.name$()).add("name", custom).add("type", "String"));
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
		return Formatters.customize(new IntinoTestTemplate());
	}

}
