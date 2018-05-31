package io.intino.konos.builder.codegeneration.services.rest;

import com.intellij.openapi.project.Project;
import cottons.utils.MimeTypes;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.action.RESTActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.list.ListData;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.rest.RESTService.Resource;
import io.intino.konos.model.graph.rest.RESTService.Resource.Operation;
import io.intino.konos.model.graph.rest.RESTService.Resource.Parameter;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

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

	public RESTResourceRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.services = graph.rESTServiceList();
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(RESTService service) {
		service.core$().findNode(Resource.class).forEach(this::processResource);
	}

	private void processResource(Resource resource) {
		for (Operation operation : resource.operationList()) {
			Frame frame = fillResourceFrame(resource, operation);
			Commons.writeFrame(new File(gen, RESOURCES), snakeCaseToCamelCase(operation.getClass().getSimpleName() + "_" + resource.name$()) + "Resource", template().format(frame));
			createCorrespondingAction(operation);
		}
	}

	private void createCorrespondingAction(Operation operation) {
		new RESTActionRenderer(project, operation, src, packageName, boxName).execute();
	}

	private Frame fillResourceFrame(Resource resource, Operation operation) {
		Frame frame = new Frame().addTypes("resource");
		frame.addSlot("name", resource.name$());
		frame.addSlot("box", boxName);
		frame.addSlot("operation", operation.getClass().getSimpleName());
		frame.addSlot("package", packageName);
		frame.addSlot("throws", throwCodes(operation));
		if (hasResponse(operation)) frame.addSlot("returnType", frameOf(operation.response()));
		frame.addSlot("parameter", (AbstractFrame[]) parameters(operation.parameterList()));
		if (!resource.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		return frame;
	}

	private boolean hasResponse(Operation operation) {
		return operation.response() != null && operation.response().isType();
	}

	private Frame frameOf(Response response) {
		Frame frame = new Frame().addSlot("value", Commons.returnType(response, packageName));
		if (response.isText() && response.dataFormat() != Response.DataFormat.html)
			frame.addSlot("format", MimeTypes.get(response.dataFormat().toString()));
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
		final Frame parameterFrame = new Frame().addTypes("parameter", parameter.in().toString(), parameter.asType().getClass().getSimpleName(), (parameter.isRequired() ? "required" : "optional"));
		if (parameter.isList()) parameterFrame.addTypes("List");
		return parameterFrame
				.addSlot("name", parameter.name$())
				.addSlot("parameterType", parameterType(parameter))
				.addSlot("in", parameter.in().name());
	}

	private Frame parameterType(Parameter parameter) {
		String innerPackage = parameter.isObject() && parameter.asObject().isComponent() ? String.join(".", packageName, "schemas.") : "";
		final Frame frame = new Frame().addSlot("value", innerPackage + parameter.asType().type());
		if (parameter.i$(ListData.class)) frame.addTypes("list");
		return frame;
	}

	private Template template() {
		return Formatters.customize(RestResourceTemplate.create());
	}
}
