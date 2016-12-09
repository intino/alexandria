package io.intino.pandora.model.codegeneration.server.rest;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.codegeneration.action.RESTActionRenderer;
import io.intino.pandora.model.helpers.Commons;
import io.intino.pandora.model.rest.RESTService;
import io.intino.pandora.model.rest.RESTService.Resource;
import io.intino.pandora.model.rest.RESTService.Resource.Operation;
import io.intino.pandora.model.rest.RESTService.Resource.Parameter;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.model.helpers.Commons.writeFrame;

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
			writeFrame(new File(gen, RESOURCES), snakeCaseToCamelCase(operation.concept().name() + "_" + resource.name()) + "Resource", template().format(frame));
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
		Template template = RestResourceTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> {
			if (value.equals("Void")) return "void";
			else if (value.toString().contains(".")) return firstLowerCase(value.toString());
			else return value;
		});
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	public static String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

}
