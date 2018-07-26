package io.intino.konos.builder.codegeneration.accessor.rest;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.schema.SchemaRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.date.DateData;
import io.intino.konos.model.graph.datetime.DateTimeData;
import io.intino.konos.model.graph.file.FileData;
import io.intino.konos.model.graph.list.ListData;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.rest.RESTService.Resource;
import io.intino.konos.model.graph.rest.RESTService.Resource.Operation;
import io.intino.konos.model.graph.rest.RESTService.Resource.Parameter;
import io.intino.konos.model.graph.type.TypeData;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class RESTAccessorRenderer {
	private final RESTService service;
	private File destination;
	private String packageName;

	public RESTAccessorRenderer(RESTService restService, File destination, String packageName) {
		this.service = restService;
		this.destination = destination;
		this.packageName = packageName;
	}

	public void execute() {
		new SchemaRenderer(service.graph(), destination, packageName, new HashMap<>()).execute();
		processService(service);
	}

	private void processService(RESTService restService) {
		Frame frame = new Frame().addTypes("accessor");
		frame.addSlot("name", restService.name$());
		frame.addSlot("package", packageName);
		setupAuthentication(restService, frame);
		if (!restService.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		List<Frame> resourceFrames = new ArrayList<>();
		for (Resource resource : restService.core$().findNode(Resource.class))
			resourceFrames.addAll(resource.operationList().stream().
					map(operation -> processOperation(operation, restService.authenticated() != null,
							restService.authenticatedWithCertificate() != null)).collect(Collectors.toList()));
		frame.addSlot("resource", (AbstractFrame[]) resourceFrames.toArray(new AbstractFrame[0]));
		writeFrame(destination, snakeCaseToCamelCase(restService.name$()) + "Accessor", template().format(frame));
	}

	private void setupAuthentication(RESTService restService, Frame frame) {
		if (restService.authenticated() != null) frame.addSlot("auth", "");
		if (restService.authenticatedWithCertificate() != null) frame.addSlot("certificate", "");
		else if (restService.authenticatedWithToken() != null) frame.addSlot("token", "");
	}

	private Frame processOperation(Operation operation, boolean authenticated, boolean cert) {
		return new Frame().addTypes("resource")
				.addSlot("returnType", Commons.returnType(operation.response(), packageName))
				.addSlot("operation", operation.getClass().getSimpleName())
				.addSlot("name", operation.core$().owner().name())
				.addSlot("parameter", (AbstractFrame[]) parameters(operation.parameterList()))
				.addSlot("invokeSentence", invokeSentence(operation, authenticated, cert))
				.addSlot("exceptionResponses", exceptionResponses(operation));
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		return new Frame().addTypes("parameter", parameter.isList() ? "list" : "single", parameter.in().toString(), (parameter.isRequired() ? "required" : "optional"), parameter.asType().getClass().getSimpleName())
				.addSlot("name", parameter.name$())
				.addSlot("parameterType", parameterType(parameter));
	}

	private String parameterType(Parameter parameter) {
		String value = (parameter.isObject() && parameter.asObject().isComponent() ? String.join(".", packageName, "schemas.") : "") + parameter.asType().type();
		return parameter.i$(ListData.class) ? "List<" + value + ">" : value;
	}


	private Frame invokeSentence(Operation operation, boolean authenticated, boolean cert) {
		Response response = operation.response();
		Frame result;
		if (response.asType() == null) result = voidInvokeSentence();
		else if (response.isObject()) result = objectInvokeSentence(response);
		else if (response.isFile()) result = fileInvokeSentence(response.asFile());
		else if (response.isDate()) result = dateInvokeSentence(response.asDate());
		else if (response.isDateTime()) result = dateTimeInvokeSentence(response.asDateTime());
		else result = primitiveInvokeSentence(response.asType());
		if (response.isList()) result.addTypes("list");
		return result.addSlot("doInvoke", doInvoke(operation, authenticated, cert));
	}

	private Frame doInvoke(Operation operation, boolean authenticated, boolean cert) {
		final Frame frame = new Frame().addTypes("doInvoke")
				.addSlot("relativePath", processPath(Commons.path(operation.core$().ownerAs(Resource.class))))
				.addSlot("type", operation.response().isFile() ? "getResource" : operation.getClass().getSimpleName().toLowerCase());
		if (authenticated) frame.addTypes("auth");
		if (cert) frame.addTypes("cert");
		if (Commons.queryParameters(operation) > 0 || Commons.bodyParameters(operation) > 0)
			frame.addSlot("parameters", "parameters");
		if (Commons.fileParameters(operation) > 0)
			frame.addSlot("resource", operation.parameterList().stream().filter(p -> p.i$(FileData.class)).map(Layer::name$).toArray(String[]::new));
		return frame;
	}

	private String processPath(String path) {
		StringBuilder builder = new StringBuilder();
		for (String pathPortion : path.split("/")) {
			if (pathPortion.startsWith(":"))
				builder.append(" + \"/\" + ").append(asMethodParameter(pathPortion.substring(1)));
			else builder.append(" + \"/").append(pathPortion).append("\"");
		}
		return builder.toString().substring(3);
	}

	private String asMethodParameter(String parameter) {
		String toCamelCase = snakeCaseToCamelCase(parameter);
		return Character.toLowerCase(toCamelCase.charAt(0)) + toCamelCase.substring(1);
	}

	private Frame exceptionResponses(Operation operation) {
		List<io.intino.konos.model.graph.Exception> exceptions = operation.exceptionList();
		if (exceptions.isEmpty()) return new Frame().addTypes("exceptionResponses", "none");
		return new Frame().addTypes("exceptionResponses")
				.addSlot("exceptionResponse", (AbstractFrame[]) exceptionResponses(exceptions));
	}

	private Frame[] exceptionResponses(List<io.intino.konos.model.graph.Exception> responses) {
		return responses.stream().map(this::exceptionResponse).toArray(Frame[]::new);
	}

	private Frame finallyException(Operation operation) {
		final Frame frame = new Frame("io");
		if (operation.response() == null || operation.response().asType() == null) return frame;
		return frame.addSlot("return", "");
	}

	private Frame exceptionResponse(io.intino.konos.model.graph.Exception response) {
		return new Frame().addTypes("exceptionResponse")
				.addSlot("code", response.code().value())
				.addSlot("exceptionName", response.code().toString());
	}

	private Frame voidInvokeSentence() {
		return new Frame().addTypes("invokeSentence", "void");
	}

	private Frame objectInvokeSentence(Response response) {
		return new Frame().addTypes("invokeSentence", "object")
				.addSlot("returnType", Commons.returnType(response, packageName));
	}

	private Frame fileInvokeSentence(FileData fileData) {
		return new Frame().addTypes("invokeSentence", "file");
		//TODO
	}

	private Frame dateInvokeSentence(DateData dateData) {
		return new Frame().addTypes("invokeSentence", "date");
		//TODO
	}

	private Frame dateTimeInvokeSentence(DateTimeData dateTimeData) {
		return new Frame().addTypes("invokeSentence", "datetime");
		//TODO
	}

	private Frame primitiveInvokeSentence(TypeData typeData) {
		return new Frame().addTypes("invokeSentence", "primitive", typeData.type())
				.addSlot("returnType", typeData.type());
	}

	private Template template() {
		return Formatters.customize(RESTAccessorTemplate.create());
	}

	public static String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

}
