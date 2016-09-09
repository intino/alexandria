package teseo.codegeneration.server.rest;

import cottons.utils.Files;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Resource;
import teseo.Service;
import teseo.codegeneration.schema.SchemaRenderer;
import teseo.codegeneration.server.jmx.JMXOperationsServiceRenderer;
import teseo.codegeneration.server.jmx.JMXServerRenderer;
import teseo.codegeneration.server.scheduling.ScheduledTriggerRenderer;
import teseo.codegeneration.server.scheduling.SchedulerRenderer;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.*;

public class JavaServerRenderer {
	private final Graph graph;
	private final List<Service> services;
	private String packageName;

	public JavaServerRenderer(Graph graph) {
		this.graph = graph;
		services = this.graph.find(Service.class);
	}

	public void execute(File gen, File src, String packageName) {
		this.packageName = packageName;
		Files.removeDir(gen);
		rest(src, gen, packageName);
		scheduling(src, gen, packageName);
		jmx(src, gen, packageName);
	}

	private void rest(File src, File gen, String packageName) {
		new SchemaRenderer(graph).execute(gen, packageName);
		new RestResourceRenderer(graph).execute(gen, src, packageName);
		services.forEach((service) -> processService(service, gen));
	}

	private void scheduling(File src, File gen, String packageName) {
		new ScheduledTriggerRenderer(graph).execute(src, gen, packageName);
		new SchedulerRenderer(graph).execute(gen, packageName);
	}

	private void jmx(File src, File gen, String packageName) {
		new JMXOperationsServiceRenderer(graph).execute(src, gen, packageName);
		new JMXServerRenderer(graph).execute(gen, packageName);
	}

	private void processService(Service service, File gen) {
		List<Resource> resources = service.node().findNode(Resource.class);
		if (resources.isEmpty()) return;
		Frame frame = new Frame().addTypes("server");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		frame.addSlot("resources", (AbstractFrame[]) processResources(resources));
		writeFrame(gen, snakeCaseToCamelCase(service.name()) + "Resources", template().format(frame));
	}

	private Frame[] processResources(List<Resource> resources) {
		return resources.stream().map(this::processResource).toArray(Frame[]::new);
	}

	private Frame processResource(Resource resource) {
		return new Frame().addTypes("resource", resource.type().toString())
				.addSlot("name", resource.name())
				.addSlot("path", path(resource))
				.addSlot("parameters", pathParameters(resource))
				.addSlot("method", resource.type().toString());
	}

	private Template template() {
		Template template = JavaServerTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
