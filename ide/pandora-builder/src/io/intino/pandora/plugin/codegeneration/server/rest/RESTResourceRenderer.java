package io.intino.pandora.plugin.codegeneration.server.rest;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.rest.RESTService;
import io.intino.pandora.model.rest.RESTService.Resource;
import io.intino.pandora.model.rest.RESTService.Resource.Operation;
import io.intino.pandora.model.rest.RESTService.Resource.Parameter;
import io.intino.pandora.plugin.codegeneration.Formatters;
import io.intino.pandora.plugin.codegeneration.action.RESTActionRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class RESTResourceRenderer {
	private static final String RESOURCES = "resources";
	private final Project project;
	private final List<RESTService> services;
	private File gen;
	private File src;
	private String packageName;
	private final String boxName;

	public RESTResourceRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		services = graph.find(RESTService.class);
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(RESTService service) {
		service.node().findNode(Resource.class).forEach(this::processResource);
	}

	private void processResource(Resource resource) {
		for (Operation operation : resource.operationList()) {
			Frame frame = fillResourceFrame(resource, operation);
			Commons.writeFrame(new File(gen, RESOURCES), snakeCaseToCamelCase(operation.concept().name() + "_" + resource.name()) + "Resource", template().format(frame));
			createCorrespondingAction(operation);
		}
	}

	private void createCorrespondingAction(Operation operation) {
		new RESTActionRenderer(project, operation, src, packageName, boxName).execute();
	}

	private Frame fillResourceFrame(Resource resource, Operation operation) {
		Frame frame = new Frame().addTypes("resource");
		frame.addSlot("name", resource.name());
		frame.addSlot("box", boxName);
		frame.addSlot("operation", operation.concept().name());
		frame.addSlot("package", packageName);
		frame.addSlot("throws", throwCodes(operation));
		frame.addSlot("returnType", Commons.returnType(operation.response()));
		frame.addSlot("parameter", (AbstractFrame[]) parameters(operation.parameterList()));
		if (!resource.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		return frame;
	}

	private String[] throwCodes(Operation resource) {
		String[] throwCodes = resource.exceptionList().stream().map(r -> r.code().toString()).toArray(String[]::new);
		return throwCodes.length == 0 ? new String[]{"Unknown"} : throwCodes;
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		final Frame parameterFrame = new Frame().addTypes("parameter", parameter.in().toString(), parameter.asType().getClass().getSimpleName(), (parameter.required() ? "required" : "optional"));
		if (parameter.isList()) parameterFrame.addTypes("List");
		return parameterFrame
				.addSlot("name", parameter.name())
				.addSlot("parameterType", parameter.asType().type())
				.addSlot("in", parameter.in().name());
	}

	private Template template() {
		return Formatters.customize(RestResourceTemplate.create());
	}
}
