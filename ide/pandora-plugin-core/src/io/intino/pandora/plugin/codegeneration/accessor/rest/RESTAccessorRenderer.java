package io.intino.pandora.plugin.codegeneration.accessor.rest;

import io.intino.pandora.plugin.Response;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.codegeneration.schema.SchemaRenderer;
import io.intino.pandora.plugin.date.DateData;
import io.intino.pandora.plugin.datetime.DateTimeData;
import io.intino.pandora.plugin.file.FileData;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.object.ObjectData;
import io.intino.pandora.plugin.rest.RESTService;
import io.intino.pandora.plugin.rest.RESTService.Resource;
import io.intino.pandora.plugin.rest.RESTService.Resource.Operation;
import io.intino.pandora.plugin.rest.RESTService.Resource.Parameter;
import io.intino.pandora.plugin.type.TypeData;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class RESTAccessorRenderer {
	private final RESTService service;
	private File destination;
	private String packageName;

	public RESTAccessorRenderer(RESTService application, File destination, String packageName) {
		this.service = application;
		this.destination = destination;
		this.packageName = packageName;
	}

	public void execute() {
		new SchemaRenderer(service.graph(), destination, packageName).execute();
		processService(service);
	}

	private void processService(RESTService restService) {
		Frame frame = new Frame().addTypes("accessor");
		frame.addSlot("name", restService.name());
		frame.addSlot("package", packageName);
		setupAuthentication(restService, frame);
		if (!restService.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		List<Frame> resourceFrames = new ArrayList<>();
		for (Resource resource : restService.node().findNode(Resource.class))
			resourceFrames.addAll(resource.operationList().stream().
					map(operation -> processResource(operation, restService.authenticated() != null,
							restService.authenticatedWithCertificate() != null)).collect(Collectors.toList()));
		frame.addSlot("resource", (AbstractFrame[]) resourceFrames.toArray(new AbstractFrame[resourceFrames.size()]));
		Commons.writeFrame(destination, snakeCaseToCamelCase(restService.name()) + "Accessor", getTemplate().format(frame));
	}

	private void setupAuthentication(RESTService restService, Frame frame) {
		if (restService.authenticated() != null) frame.addSlot("auth", "");
		if (restService.authenticatedWithCertificate() != null) frame.addSlot("certificate", "");
		else if (restService.authenticatedWithPassword() != null) frame.addSlot("user", "");
	}

	private Frame processResource(Operation operation, boolean authenticated, boolean cert) {
		return new Frame().addTypes("resource")
				.addSlot("returnType", Commons.returnType(operation.response()))
				.addSlot("operation", operation.concept().name())
				.addSlot("name", operation.owner().name())
				.addSlot("parameter", (AbstractFrame[]) parameters(operation.parameterList()))
				.addSlot("invokeSentence", invokeSentence(operation, authenticated, cert))
				.addSlot("exceptionResponses", exceptionResponses(operation));
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		return new Frame().addTypes("parameter", parameter.in().toString(), (parameter.required() ? "required" : "optional"), parameter.asType().getClass().getSimpleName())
				.addSlot("name", parameter.name())
				.addSlot("parameterType", parameter.asType().type());
	}

	private Frame invokeSentence(Operation operation, boolean authenticated, boolean cert) {
		Response response = operation.response();
		Frame result;
		if (response.asType() == null) result = voidInvokeSentence();
		else if (response.isObject()) result = objectInvokeSentence(response.asObject());
		else if (response.isFile()) result = fileInvokeSentence(response.asFile());
		else if (response.isDate()) result = dateInvokeSentence(response.asDate());
		else if (response.isDateTime()) result = dateTimeInvokeSentence(response.asDateTime());
		else result = primitiveInvokeSentence(response.asType());
		if (response.isList()) result.addTypes("list");
		return result.addSlot("doInvoke", doInvoke(operation, authenticated, cert));
	}

	private Frame doInvoke(Operation resource, boolean authenticated, boolean cert) {
		final Frame frame = new Frame().addTypes("doInvoke")
				.addSlot("relativePath", processPath(Commons.path(resource.owner().as(Resource.class))))
				.addSlot("type", resource.concept().name().toLowerCase());
		if (authenticated) frame.addTypes("auth");
		if (cert) frame.addTypes("cert");
		if (Commons.queryParameters(resource) > 0) frame.addSlot("parameters", "parameters");
		else if (Commons.fileParameters(resource) > 0) frame.addSlot("parameters", "resource");
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
		List<io.intino.pandora.plugin.Exception> exceptions = operation.exceptionList();
		if (exceptions.isEmpty()) return new Frame().addTypes("exceptionResponses", "none");
		return new Frame().addTypes("exceptionResponses")
				.addSlot("exceptionResponse", (AbstractFrame[]) exceptionResponses(exceptions));
	}

	private Frame[] exceptionResponses(List<io.intino.pandora.plugin.Exception> responses) {
		return responses.stream().map(this::exceptionResponse).toArray(Frame[]::new);
	}

	private Frame exceptionResponse(io.intino.pandora.plugin.Exception response) {
		return new Frame().addTypes("exceptionResponse")
				.addSlot("code", response.code().value())
				.addSlot("exceptionName", response.code().toString());
	}

	private Frame voidInvokeSentence() {
		return new Frame().addTypes("invokeSentence", "void");
	}

	private Frame objectInvokeSentence(ObjectData objectData) {
		return new Frame().addTypes("invokeSentence", "object")
				.addSlot("returnType", objectData.type());
	}

	private Frame fileInvokeSentence(FileData fileData) {
		return new Frame().addTypes("invokeSentence", "file");
		//TODO
	}

	private Frame dateInvokeSentence(DateData dateData) {
		return new Frame().addTypes("invokeSentence", "file");
		//TODO
	}

	private Frame dateTimeInvokeSentence(DateTimeData dateTimeData) {
		return new Frame().addTypes("invokeSentence", "file");
		//TODO
	}

	private Frame primitiveInvokeSentence(TypeData typeData) {
		return new Frame().addTypes("invokeSentence", "primitive", typeData.type())
				.addSlot("returnType", typeData.type());
	}

	private Template getTemplate() {
		Template template = RESTAccessorTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("ValidPackage", Commons::validPackage);
		return template;
	}

}
