package io.intino.pandora.plugin.codegeneration.server.rest;

import io.intino.pandora.model.rest.RESTService;
import io.intino.pandora.model.rest.RESTService.Resource;
import io.intino.pandora.plugin.codegeneration.Formatters;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class RESTServiceRenderer {
	private final List<RESTService> services;
	private final File gen;
	private String packageName;
	private final String boxName;

	public RESTServiceRenderer(Graph graph, File gen, String packageName, String boxName) {
		services = graph.find(RESTService.class);
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		services.forEach((service) -> processService(service.as(RESTService.class), gen));
	}

	private void processService(RESTService service, File gen) {
		if (service.resourceList().isEmpty()) return;
		Frame frame = new Frame().addTypes("server").
				addSlot("name", service.name()).
				addSlot("box", boxName).
				addSlot("package", packageName).
				addSlot("resource", (AbstractFrame[]) processResources(service.resourceList()));
		final RESTService.AuthenticatedWithCertificate secure = service.authenticatedWithCertificate();
		if (secure != null && secure.store() != null)
			frame.addSlot("secure", new Frame().addTypes("secure").addSlot("file", secure.store().getPath()).addSlot("password", secure.storePassword()));
		Commons.writeFrame(gen, snakeCaseToCamelCase(service.name()) + "Resources", template().format(frame));
	}

	private Frame[] processResources(List<Resource> resources) {
		List<Frame> list = new ArrayList<>();
		for (Resource resource : resources) {
			list.addAll(processResource(resource, resource.operationList()));
		}
		return list.toArray(new Frame[list.size()]);
	}

	private List<Frame> processResource(Resource resource, List<Resource.Operation> operations) {
		return operations.stream().map(operation -> new Frame().addTypes("resource", operation.concept().name())
				.addSlot("name", resource.name())
				.addSlot("operation", operation.concept().name())
				.addSlot("path", customize(Commons.path(resource)))
				.addSlot("method", operation.concept().name())).collect(Collectors.toList());
	}

	private Frame customize(String path) {
		Frame frame = new Frame().addTypes("path");
		frame.addSlot("name", path);
		for (String parameter : Commons.extractParameters(path)) frame.addSlot("custom", parameter);
		return frame;
	}


	private Template template() {
		return Formatters.customize(RESTServiceTemplate.create());
	}
}
