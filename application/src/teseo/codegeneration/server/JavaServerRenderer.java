package teseo.codegeneration.server;

import cottons.utils.Files;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Application;
import teseo.Resource;
import teseo.codegeneration.server.action.JavaActionRenderer;
import teseo.codegeneration.schema.SchemaRenderer;
import teseo.codegeneration.server.scheduling.JavaScheduledTriggerRenderer;
import teseo.codegeneration.server.scheduling.JavaSchedulerRenderer;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.*;

public class JavaServerRenderer {
	private final Graph graph;
	private final List<Application> applications;
	private File destination;
	private String packageName;

	public JavaServerRenderer(Graph graph) {
		this.graph = graph;
		applications = this.graph.find(Application.class);
	}

	public void execute(File gen, File src, String packageName) {
		this.destination = gen;
		this.packageName = packageName;
		Files.removeDir(gen);
		web(gen, src, packageName);
		scheduling(gen, packageName);
		action(src, packageName);
	}

	private void action(File destiny, String packageName) {
		new JavaActionRenderer(graph).execute(destiny, packageName);
	}

	private void web(File gen, File src, String packageName) {
		new SchemaRenderer(graph).execute(destination, packageName);
		new JavaServerActionRenderer(graph).execute(gen, src, packageName);
		applications.forEach(this::processApplication);
	}

	private void scheduling(File gen, String packageName) {
		new JavaScheduledTriggerRenderer(graph).execute(gen, packageName);
		new JavaSchedulerRenderer(graph).execute(gen, packageName);
	}

	private void processApplication(Application application) {
		Frame frame = new Frame().addTypes("server");
		frame.addSlot("name", application.name());
		frame.addSlot("package", packageName);
		frame.addSlot("resources", (AbstractFrame[]) processResources(application.node().findNode(Resource.class)));
		writeFrame(destination, snakeCaseToCamelCase(application.name()) + "Actions", template().format(frame));
	}

	private Frame[] processResources(List<Resource> resources) {
		return resources.stream().map(this::processResource).toArray(Frame[]::new);
	}

	private Frame processResource(Resource resource) {
		return new Frame().addTypes("resource", resource.method().toString())
				.addSlot("name", resource.name())
				.addSlot("path", path(resource))
				.addSlot("parameters", pathParameters(resource))
				.addSlot("method", resource.method().toString());
	}

	private Template template() {
		Template template = JavaServerTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-","").toLowerCase());
		return template;
	}
}
