package io.intino.konos.builder.codegeneration.accessor.rest;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Exception;
import io.intino.konos.model.Response;
import io.intino.konos.model.Service;
import io.intino.konos.model.Service.REST.Authentication;
import io.intino.konos.model.Service.REST.Notification;
import io.intino.konos.model.Service.REST.Resource;
import io.intino.konos.model.Service.REST.Resource.Operation;
import io.intino.konos.model.Service.REST.Resource.Parameter;
import io.intino.magritte.framework.Concept;
import io.intino.magritte.framework.Predicate;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.rules.ExceptionCodes.Unauthorized;

public class RESTAccessorRenderer extends Renderer {
	private final Service.REST service;
	private final File destination;
	private final Map<String, List<Parameter>> enumParameters;

	public RESTAccessorRenderer(CompilationContext compilationContext, Service.REST restService, File destination) {
		super(compilationContext);
		this.service = restService;
		this.destination = destination;
		this.destination.mkdirs();
		this.enumParameters = new HashMap<>();
	}

	@Override
	public void render() throws KonosException {
		new SchemaListRenderer(context, service.graph(), destination, true).execute();
		processService(service);
	}

	private void processService(Service.REST service) {
		FrameBuilder builder = new FrameBuilder("accessor");
		builder.add("name", service.name$());
		builder.add("package", packageName());
		setupAuthentication(service, builder);
		if (!service.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()));
		builder.add("resource", service.core$().findNode(Resource.class).stream().map(this::framesOf).flatMap(List::stream).toArray(Frame[]::new));
		builder.add("notification", service.notificationList().stream().map(this::frameOf).toArray(Frame[]::new));
		for (Parameter enumParameter : enumParameters.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
			builder.add("enumParameter", new FrameBuilder("enumParameter")
					.add("name", enumParameter.name$())
					.add("class", enumParameter.core$().ownerAs(Resource.class).name$() + firstUpperCase(enumParameter.name$()))
					.add("value", enumParameter.asWord().values().toArray(String[]::new)));
		writeFrame(destination, snakeCaseToCamelCase(service.name$()) + "Accessor", template().render(builder));
	}

	private void setupAuthentication(Service.REST restService, FrameBuilder builder) {
		if (restService.authentication() != null)
			builder.add("authentication", new FrameBuilder("authentication", authentication()));
	}

	private String authentication() {
		return service.authentication().core$().conceptList().stream().filter(Concept::isAspect).map(Predicate::name).findFirst().orElse(null);
	}

	private List<Frame> framesOf(Resource resource) {
		return resource.operationList().stream().
				map(operation -> processOperation(operation, service.authentication())).collect(Collectors.toList());
	}

	private Frame frameOf(Notification notification) {
		FrameBuilder builder = new FrameBuilder("notification").add("path", notification.path()).add("name", notification.name$());
		if (service.authentication() != null) builder.add("auth", new FrameBuilder("authentication", authentication()));
		builder.add("parameter", notificationParameters(notification.parameterList()));
		return builder.toFrame();
	}

	private Frame processOperation(Operation operation, Authentication authentication) {
		FrameBuilder builder = new FrameBuilder("resource")
				.add("path", processPath(Commons.path(operation.core$().ownerAs(Resource.class))))
				.add("response", new FrameBuilder(responseType(operation.response())).
						add("value", customizeMultipart(operation.response(), Commons.returnType(operation.response(), packageName()))))
				.add("method", operation.getClass().getSimpleName())
				.add("name", operation.core$().owner().name())
				.add("parameter", parameters(operation.core$().ownerAs(Resource.class).parameterList()))
				.add("parameter", parameters(operation.parameterList()))
				.add("exceptionResponses", exceptionResponses(operation, authentication));
		if (authentication != null) builder.add("auth", new FrameBuilder("authentication", authentication()));
		return builder.toFrame();
	}

	private String customizeMultipart(Response response, String returnType) {
		return response != null && response.isMultiPart() ? returnType.replace(".rest.", ".restaccessor.") : returnType;
	}

	private String[] responseType(Response response) {
		List<String> types = new ArrayList<>(List.of(response != null && response.isType() ? response.asType().type() : "void"));
		if (response != null)
			types.addAll(response.core$().layerList().stream().map(l -> l.contains("$") ? l.substring(l.indexOf("$") + 1) : l).toList());
		return types.toArray(new String[0]);
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
		String type;
		if (parameter.isWord()) {
			String resource = parameter.core$().ownerAs(Resource.class).name$();
			enumParameters.putIfAbsent(resource, new ArrayList<>());
			List<Parameter> parameters = enumParameters.get(resource);
			if (parameters.stream().noneMatch(p -> p.name$().equals(parameter.name$())))
				enumParameters.get(resource).add(parameter);
			type = firstUpperCase(snakeCaseToCamelCase(resource) + firstUpperCase(snakeCaseToCamelCase(parameter.name$())));
		} else type = (parameter.isObject() && parameter.asObject().isComponent() ?
				String.join(".", packageName(), "schemas.") : "") + parameter.asType().type();
		if (parameter.isList()) return "List<" + type + ">";
		if (parameter.isSet()) return "Set<" + type + ">";
		return type;
	}

	private String parameterType(Notification.Parameter parameter) {
		String type = (parameter.isObject() && parameter.asObject().isComponent() ? String.join(".", packageName(), "schemas.") : "") + parameter.asType().type();
		if (parameter.isList()) return "List<" + type + ">";
		if (parameter.isSet()) return "Set<" + type + ">";
		return type;
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

	private FrameBuilder exceptionResponses(Operation operation, Authentication authentication) {
		List<io.intino.konos.model.Exception> exceptions = operation.exceptionList();
		if (exceptions.isEmpty()) {
			if (authentication == null) return new FrameBuilder("exceptionResponses", "none");
			return new FrameBuilder("exceptionResponses").add("exceptionResponse", unauthorizedExceptionResponse());
		}
		return new FrameBuilder("exceptionResponses")
				.add("exceptionResponse", exceptionResponses(exceptions, authentication));
	}

	private Frame[] exceptionResponses(List<io.intino.konos.model.Exception> exceptions, Authentication authentication) {
		final List<Frame> collect = exceptions.stream().map(this::exceptionResponse).collect(Collectors.toList());
		if (authentication != null && exceptions.stream().noneMatch(e -> e.code().equals(Unauthorized)))
			collect.add(unauthorizedExceptionResponse());
		return collect.toArray(new Frame[0]);
	}

	private Frame exceptionResponse(Exception exception) {
		return new FrameBuilder("exceptionResponse")
				.add("code", exception.code().value())
				.add("exceptionName", exception.code().toString()).toFrame();
	}


	private Frame unauthorizedExceptionResponse() {
		return new FrameBuilder("exceptionResponse")
				.add("code", Unauthorized.value())
				.add("exceptionName", Unauthorized.name()).toFrame();
	}

	private Template template() {
		return Formatters.customize(new RESTAccessorTemplate());
	}

	public static String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}
}
