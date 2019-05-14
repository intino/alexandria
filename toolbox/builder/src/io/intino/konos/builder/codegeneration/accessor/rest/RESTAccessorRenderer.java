package io.intino.konos.builder.codegeneration.accessor.rest;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.date.DateData;
import io.intino.konos.model.graph.datetime.DateTimeData;
import io.intino.konos.model.graph.file.FileData;
import io.intino.konos.model.graph.list.ListData;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.rest.RESTService.Notification;
import io.intino.konos.model.graph.rest.RESTService.Resource;
import io.intino.konos.model.graph.rest.RESTService.Resource.Operation;
import io.intino.konos.model.graph.rest.RESTService.Resource.Parameter;
import io.intino.konos.model.graph.type.TypeData;
import io.intino.tara.magritte.Layer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class RESTAccessorRenderer extends Renderer {
	private final RESTService service;
	private File destination;

	public RESTAccessorRenderer(Settings settings, RESTService restService, File destination) {
		super(settings, Target.Service);
		this.service = restService;
		this.destination = destination;
	}

	public static String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	@Override
	public void render() {
		new SchemaListRenderer(settings, service.graph(), destination).execute();
		processService(service);
	}

	private void processService(RESTService service) {
		FrameBuilder frame = new FrameBuilder("accessor");
		frame.add("name", service.name$());
		frame.add("package", packageName());
		setupAuthentication(service, frame);
		if (!service.graph().schemaList().isEmpty())
			frame.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()));
		List<Frame> resourceFrames = new ArrayList<>();
		service.core$().findNode(Resource.class).stream().map(resource -> framesOf(service, resource)).forEach(resourceFrames::addAll);
		frame.add("resource", resourceFrames.toArray(new Frame[0]));
		frame.add("notification", service.notificationList().stream().map(notification -> frameOf(service, notification)).toArray(Frame[]::new));
		writeFrame(destination, snakeCaseToCamelCase(service.name$()) + "Accessor", template().render(frame));
	}

	private void setupAuthentication(RESTService restService, FrameBuilder builder) {
		if (restService.authenticated() != null) builder.add("auth", "");
		if (restService.authenticatedWithCertificate() != null) builder.add("certificate", "");
		else if (restService.authenticatedWithToken() != null) builder.add("token", "");
	}

	@NotNull
	private List<Frame> framesOf(RESTService restService, Resource resource) {
		return resource.operationList().stream().
				map(operation -> processOperation(operation, restService.authenticated() != null,
						restService.authenticatedWithCertificate() != null)).collect(Collectors.toList());
	}

	private Frame frameOf(RESTService restService, Notification notification) {
		FrameBuilder notificationBuilder = new FrameBuilder("notification").add("path", notification.path()).add("name", notification.name$());
		if (restService.authenticatedWithToken() != null) notificationBuilder.add("secure", "");
		if (Commons.queryParameters(notification) > 0 || Commons.bodyParameters(notification) > 0)
			notificationBuilder.add("parameters", "parameters");
		notificationBuilder.add("parameter", notificationParameters(notification.parameterList()));
		return notificationBuilder.toFrame();
	}

	private Frame processOperation(Operation operation, boolean authenticated, boolean cert) {
		return new FrameBuilder("resource")
				.add("returnType", Commons.returnType(operation.response(), packageName()))
				.add("operation", operation.getClass().getSimpleName())
				.add("name", operation.core$().owner().name())
				.add("parameter", parameters(operation.parameterList()))
				.add("invokeSentence", invokeSentence(operation, authenticated, cert))
				.add("exceptionResponses", exceptionResponses(operation)).toFrame();
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame[] notificationParameters(List<Notification.Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		return new FrameBuilder("parameter", parameter.isList() ? "list" : "single", parameter.in().toString(), (parameter.isRequired() ? "required" : "optional"), parameter.asType().getClass().getSimpleName())
				.add("name", parameter.name$())
				.add("parameterType", parameterType(parameter)).toFrame();
	}

	private Frame parameter(Notification.Parameter parameter) {
		return new FrameBuilder("parameter", parameter.isList() ? "list" : "single", parameter.in().toString(), (parameter.isRequired() ? "required" : "optional"), parameter.asType().getClass().getSimpleName())
				.add("name", parameter.name$())
				.add("parameterType", parameterType(parameter)).toFrame();
	}

	private String parameterType(Parameter parameter) {
		String value = (parameter.isObject() && parameter.asObject().isComponent() ? String.join(".", packageName(), "schemas.") : "") + parameter.asType().type();
		return parameter.i$(ListData.class) ? "List<" + value + ">" : value;
	}

	private String parameterType(Notification.Parameter parameter) {
		String value = (parameter.isObject() && parameter.asObject().isComponent() ? String.join(".", packageName(), "schemas.") : "") + parameter.asType().type();
		return parameter.i$(ListData.class) ? "List<" + value + ">" : value;
	}

	private Frame invokeSentence(Operation operation, boolean authenticated, boolean cert) {
		Response response = operation.response();
		FrameBuilder result;
		if (response.asType() == null) result = voidInvokeSentence();
		else if (response.isObject()) result = objectInvokeSentence(response);
		else if (response.isFile()) result = fileInvokeSentence(response.asFile());
		else if (response.isDate()) result = dateInvokeSentence(response.asDate());
		else if (response.isDateTime()) result = dateTimeInvokeSentence(response.asDateTime());
		else result = primitiveInvokeSentence(response.asType());
		if (response.isList()) result.add("list");
		return result.add("doInvoke", doInvoke(operation, authenticated, cert)).toFrame();
	}

	private Frame doInvoke(Operation operation, boolean authenticated, boolean cert) {
		final FrameBuilder builder = new FrameBuilder("doInvoke")
				.add("relativePath", processPath(Commons.path(operation.core$().ownerAs(Resource.class))))
				.add("type", operation.response().isFile() ? "getResource" : operation.getClass().getSimpleName().toLowerCase());
		if (authenticated) builder.add("auth");
		if (cert) builder.add("cert");
		if (Commons.queryParameters(operation) > 0 || Commons.bodyParameters(operation) > 0)
			builder.add("parameters", "parameters");
		if (Commons.fileParameters(operation) > 0)
			builder.add("resource", operation.parameterList().stream().filter(p -> p.i$(FileData.class)).map(Layer::name$).toArray(String[]::new));
		return builder.toFrame();
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

	private FrameBuilder exceptionResponses(Operation operation) {
		List<io.intino.konos.model.graph.Exception> exceptions = operation.exceptionList();
		if (exceptions.isEmpty()) return new FrameBuilder("exceptionResponses", "none");
		return new FrameBuilder("exceptionResponses")
				.add("exceptionResponse", exceptionResponses(exceptions));
	}

	private Frame[] exceptionResponses(List<io.intino.konos.model.graph.Exception> responses) {
		return responses.stream().map(this::exceptionResponse).toArray(Frame[]::new);
	}

	private FrameBuilder finallyException(Operation operation) {
		final FrameBuilder builder = new FrameBuilder("io");
		if (operation.response() == null || operation.response().asType() == null) return builder;
		return builder.add("return", "");
	}

	private Frame exceptionResponse(Exception response) {
		return new FrameBuilder("exceptionResponse")
				.add("code", response.code().value())
				.add("exceptionName", response.code().toString()).toFrame();
	}

	private FrameBuilder voidInvokeSentence() {
		return new FrameBuilder("invokeSentence", "void");
	}

	private FrameBuilder objectInvokeSentence(Response response) {
		return new FrameBuilder("invokeSentence", "object")
				.add("returnType", Commons.returnType(response, packageName()));
	}

	private FrameBuilder fileInvokeSentence(FileData fileData) {
		return new FrameBuilder("invokeSentence", "file");
		//TODO
	}

	private FrameBuilder dateInvokeSentence(DateData dateData) {
		return new FrameBuilder("invokeSentence", "date");
		//TODO
	}

	private FrameBuilder dateTimeInvokeSentence(DateTimeData dateTimeData) {
		return new FrameBuilder("invokeSentence", "datetime");
		//TODO
	}

	private FrameBuilder primitiveInvokeSentence(TypeData typeData) {
		return new FrameBuilder("invokeSentence", "primitive", typeData.type())
				.add("returnType", typeData.type());
	}

	private Template template() {
		return Formatters.customize(new RESTAccessorTemplate());
	}

}
