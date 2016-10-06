package io.intino.pandora.plugin.codegeneration.server.rest;

import io.intino.pandora.plugin.Resource;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.rest.RESTService;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class RESTServiceRenderer {
	private final List<RESTService> services;
	private final File gen;
	private String packageName;

	public RESTServiceRenderer(Graph graph, File gen, String packageName) {
		services = graph.find(RESTService.class);
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		services.forEach((service) -> processService(service.as(RESTService.class), gen));
	}

	private void processService(RESTService service, File gen) {
		if (service.resourceList().isEmpty()) return;
		Frame frame = new Frame().addTypes("server").
				addSlot("name", service.name()).
				addSlot("package", packageName).
				addSlot("resources", (AbstractFrame[]) processResources(service.resourceList()));
		final RESTService.AuthenticatedWithCertificate secure = service.authenticatedWithCertificate();
		if (secure != null && secure.store() != null)
			frame.addSlot("secure", new Frame().addTypes("secure").addSlot("file", secure.store().getPath()).addSlot("password", secure.storePassword()));
		Commons.writeFrame(gen, snakeCaseToCamelCase(service.name()) + "Resources", template().format(frame));
	}

	private Frame[] processResources(List<Resource> resources) {
		return resources.stream().map(this::processResource).toArray(Frame[]::new);
	}

	private Frame processResource(Resource resource) {
		return new Frame().addTypes("resource", resource.type().toString())
				.addSlot("name", resource.name())
				.addSlot("path", Commons.path(resource))
				.addSlot("method", resource.type().toString());
	}

	private Template template() {
		Template template = RESTServerTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ValidPackage", Commons::validPackage);
		return template;
	}
}
