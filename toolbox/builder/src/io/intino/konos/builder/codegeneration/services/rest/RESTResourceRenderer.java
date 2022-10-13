package io.intino.konos.builder.codegeneration.services.rest;

import cottons.utils.MimeTypes;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.RESTNotificationActionRenderer;
import io.intino.konos.builder.codegeneration.action.RESTResourceActionRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.*;
import io.intino.konos.model.Service.REST.Notification;
import io.intino.konos.model.Service.REST.Resource;
import io.intino.konos.model.Service.REST.Resource.Parameter;
import io.intino.magritte.framework.Node;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.model.Service.REST.Resource.Operation;

public class RESTResourceRenderer extends Renderer {
	public static final String RESOURCES_PACKAGE = "rest/resources";
	public static final String NOTIFICATIONS_PACKAGE = "rest/notifications";
	private final List<Service.REST> services;

	public RESTResourceRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.services = graph.serviceList(Service::isREST).map(Service::asREST).collect(Collectors.toList());
	}

	public void render() throws KonosException {
		for (Service.REST service : services) processService(service);
	}

	private void processService(Service.REST service) throws KonosException {
		for (Resource resource : service.core$().findNode(Resource.class)) processResource(resource);
		for (Notification notification : service.core$().findNode(Notification.class))
			processNotification(notification);
	}

	private void processResource(Resource resource) throws KonosException {
		for (Operation operation : resource.operationList()) {
			Frame frame = frameOf(resource, operation);
			final String className = snakeCaseToCamelCase(operation.getClass().getSimpleName() + "_" + resource.name$()) + "Resource";
			File resourcesPackage = new File(gen(), RESOURCES_PACKAGE);
			Commons.writeFrame(resourcesPackage, className, template().render(frame));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(resource), javaFile(resourcesPackage, className).getAbsolutePath()));
			createCorrespondingAction(operation);
		}
	}

	private void processNotification(Notification notification) throws KonosException {
		final String className = snakeCaseToCamelCase(notification.name$()) + "Notification";
		Service.REST service = notification.core$().ownerAs(Service.REST.class);
		FrameBuilder builder = new FrameBuilder("notification").add("path", notification.path());
		addCommons(notification.name$(), builder);
		builder.add("parameter", notificationParameters(notification.parameterList()));
		builder.add("returnType", notificationResponse());
		if (service.authentication() != null) builder.add("throws", "Unauthorized");
		File genNotifications = new File(gen(), NOTIFICATIONS_PACKAGE);
		Commons.writeFrame(genNotifications, className, template().render(builder.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(genNotifications, className).getAbsolutePath()));

		createCorrespondingAction(notification);
	}

	private void createCorrespondingAction(Operation operation) throws KonosException {
		new RESTResourceActionRenderer(context, operation).execute();
	}

	private void createCorrespondingAction(Notification notification) throws KonosException {
		new RESTNotificationActionRenderer(context, notification).execute();
	}

	private Frame frameOf(Resource resource, Operation operation) {
		FrameBuilder builder = new FrameBuilder("resource");
		addCommons(resource.name$(), builder);
		builder.add("word", resource.parameterList().stream().filter(Data::isWord).map(RESTResourceRenderer::wordFrame).toArray(Frame[]::new));
		builder.add("word", operation.parameterList().stream().filter(Data::isWord).map(RESTResourceRenderer::wordFrame).toArray(Frame[]::new));
		builder.add("operation", operation.getClass().getSimpleName());
		builder.add("throws", throwCodes(operation));
		authenticated(resource.core$().ownerAs(Service.REST.class), builder);
		builder.add("parameter", parameters(operation.parameterList()));
		builder.add("parameter", parameters(resource.parameterList()));
		if (hasResponse(operation)) builder.add("returnType", frameOf(operation.response()));
		if (!resource.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		return builder.toFrame();
	}

	private static Frame wordFrame(Parameter p) {
		return new FrameBuilder("word")
				.add("words", p.asWord().values().toArray())
				.add("name", p.name$()).toFrame();
	}

	private void authenticated(Service.REST service, FrameBuilder builder) {
		if (service.authentication() != null) {
			FrameBuilder b = new FrameBuilder("authenticationValidator");
			if (service.authentication().isBasic()) b.add("type", "Basic");
			else b.add("type", "Bearer");
			builder.add("authenticationValidator", b.toFrame());
		}
	}

	private void addCommons(String name, FrameBuilder builder) {
		builder.add("package", packageName());
		builder.add("name", name);
		builder.add("box", boxName());
	}

	private boolean hasResponse(Operation operation) {
		return (operation.response() != null && (operation.response().isType() || operation.response().i$(Redirect.class)));
	}

	private Frame frameOf(Response response) {
		FrameBuilder builder = new FrameBuilder(response.getClass().getSimpleName());
		if (response.asType() != null) builder.add(response.asType().getClass().getSimpleName());
		builder.add("value", Commons.returnType(response, packageName()));
		if (response.isText() && response.dataFormat() != Response.DataFormat.html)
			builder.add("format", MimeTypes.get(response.dataFormat().toString()));
		return builder.toFrame();
	}

	private Frame notificationResponse() {
		return new FrameBuilder("Response").add("value", String.class.getSimpleName()).add("format", MimeTypes.get("text")).toFrame();
	}

	private String[] throwCodes(Operation resource) {
		List<String> throwCodes = resource.exceptionList().stream().map(r -> r.code().toString()).collect(Collectors.toList());
		return throwCodes.isEmpty() ? new String[]{"InternalServerError"} : throwCodes.toArray(new String[0]);
	}

	private Frame[] parameters(List<? extends Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame[] notificationParameters(List<Notification.Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		final FrameBuilder builder = new FrameBuilder("parameter", parameter.in().toString(), parameter.asType().getClass().getSimpleName(), (parameter.isRequired() ? "required" : "optional"));
		if (parameter.isList()) builder.add("List");
		builder.add("name", parameter.name$())
				.add("parameterType", parameterType(parameter))
				.add("in", parameter.in().name());
		if (parameter.isRequired()) return builder.toFrame();
		if (parameter.isBool()) builder.add("defaultValue").add("defaultValue", parameter.asBool().defaultValue());
		else if (parameter.isText() && parameter.asText().defaultValue() != null)
			builder.add("defaultValue").add("defaultValue", parameter.asText().defaultValue());
		else if (parameter.isReal()) builder.add("defaultValue").add("defaultValue", parameter.asReal().defaultValue());
		else if (parameter.isInteger())
			builder.add("defaultValue").add("defaultValue", parameter.asInteger().defaultValue());
		else if (parameter.isLongInteger())
			builder.add("defaultValue").add("defaultValue", parameter.asLongInteger().defaultValue());
		return builder.toFrame();
	}

	private Frame parameter(Notification.Parameter parameter) {
		final FrameBuilder builder = new FrameBuilder("parameter", parameter.in().toString(), parameter.asType().getClass().getSimpleName(), (parameter.isRequired() ? "required" : "optional"));
		if (parameter.isList()) builder.add("List");
		return builder
				.add("name", parameter.name$())
				.add("parameterType", parameterType(parameter))
				.add("in", parameter.in().name()).toFrame();
	}

	private Frame parameterType(io.intino.konos.model.Parameter parameter) {
		String innerPackage = parameter.isObject() && parameter.asObject().isComponent() ? String.join(".", packageName(), "schemas.") : "";
		final FrameBuilder builder = new FrameBuilder();
		if (parameter.isWord()) builder.add("value", owner(parameter) + "." + firstUpperCase(parameter.name$()));
		else builder.add("value", innerPackage + parameter.asType().type());
		if (parameter.i$(Data.List.class)) builder.add("list");
		return builder.toFrame();
	}

	private Object owner(io.intino.konos.model.Parameter parameter) {
		Node owner = parameter.core$().owner();
		if (owner.is(Notification.class)) {
			return firstUpperCase(owner.name());
		} else if (owner.is(Operation.class))
			return snakeCaseToCamelCase(owner.as(Operation.class).getClass().getSimpleName() + "_" + owner.owner().name()) + "Resource";
		return firstUpperCase(owner.owner().name());
	}

	private Template template() {
		return Formatters.customize(new RestResourceTemplate());
	}
}
