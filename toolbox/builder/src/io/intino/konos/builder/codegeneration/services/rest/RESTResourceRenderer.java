package io.intino.konos.builder.codegeneration.services.rest;

import cottons.utils.MimeTypes;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.RESTNotificationActionRenderer;
import io.intino.konos.builder.codegeneration.action.RESTResourceActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Service.REST.Notification;
import io.intino.konos.model.graph.Service.REST.Resource;
import io.intino.konos.model.graph.Service.REST.Resource.Parameter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.model.graph.Service.REST.Resource.Operation;

public class RESTResourceRenderer extends Renderer {
	private static final String RESOURCES_PACKAGE = "rest/resources";
	private static final String NOTIFICATIONS_PACKAGE = "rest/notifications";
	private final List<Service.REST> services;

	public RESTResourceRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
		this.services = graph.serviceList(Service::isREST).map(Service::asREST).collect(Collectors.toList());
	}

	public void render() {
		services.forEach(this::processService);
	}

	private void processService(Service.REST service) {
		service.core$().findNode(Resource.class).forEach(this::processResource);
		service.core$().findNode(Notification.class).forEach(this::processNotification);
	}

	private void processResource(Resource resource) {
		for (Operation operation : resource.operationList()) {
			Frame frame = frameOf(resource, operation);
			final String className = snakeCaseToCamelCase(operation.getClass().getSimpleName() + "_" + resource.name$()) + "Resource";
			Commons.writeFrame(new File(gen(), RESOURCES_PACKAGE), className, template().render(frame));
			createCorrespondingAction(operation);
		}
	}

	private void processNotification(Notification notification) {
		final String className = snakeCaseToCamelCase(notification.name$()) + "Notification";
		Service.REST service = notification.core$().ownerAs(Service.REST.class);
		FrameBuilder builder = new FrameBuilder("notification").add("path", notification.path());
		addCommons(notification.name$(), builder);
		builder.add("parameter", notificationParameters(notification.parameterList()));
		builder.add("returnType", notificationResponse());
		authenticated(service, builder);
		if (service.authenticatedWithToken() != null) builder.add("throws", "Unauthorized");
		Commons.writeFrame(new File(gen(), NOTIFICATIONS_PACKAGE), className, template().render(builder.toFrame()));
		createCorrespondingAction(notification);
	}

	private void createCorrespondingAction(Operation operation) {
		new RESTResourceActionRenderer(settings, operation).execute();
	}

	private void createCorrespondingAction(Notification notification) {
		new RESTNotificationActionRenderer(settings, notification).execute();
	}

	private Frame frameOf(Resource resource, Operation operation) {
		FrameBuilder builder = new FrameBuilder("resource");
		addCommons(resource.name$(), builder);
		builder.add("operation", operation.getClass().getSimpleName());
		builder.add("throws", throwCodes(operation));
		builder.add("parameter", parameters(operation.parameterList()));
		if (hasResponse(operation)) builder.add("returnType", frameOf(operation.response()));
		if (!resource.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		authenticated(resource.core$().ownerAs(Service.REST.class), builder);
		return builder.toFrame();
	}

	private void authenticated(Service.REST service, FrameBuilder builder) {
		final Service.REST.AuthenticatedWithToken authenticated = service.authenticatedWithToken();
		if (authenticated != null)
			builder.add("authenticationValidator", new FrameBuilder("authenticationValidator").add("type", "Basic").toFrame());
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
		FrameBuilder builder = new FrameBuilder(response.getClass().getSimpleName()).add("value", Commons.returnType(response, packageName()));
		if (response.isText() && response.dataFormat() != Response.DataFormat.html)
			builder.add("format", MimeTypes.get(response.dataFormat().toString()));
		return builder.toFrame();
	}

	private Frame notificationResponse() {
		return new FrameBuilder("Response").add("value", String.class.getSimpleName()).add("format", MimeTypes.get("text")).toFrame();
	}

	private String[] throwCodes(Operation resource) {
		final Service.REST.AuthenticatedWithToken authenticated = resource.core$().ownerAs(Service.REST.class).authenticatedWithToken();
		List<String> throwCodes = resource.exceptionList().stream().map(r -> r.code().toString()).collect(Collectors.toList());
		if (authenticated != null) throwCodes.add("Unauthorized");
		return throwCodes.isEmpty() ? new String[]{"Unknown"} : throwCodes.toArray(new String[0]);
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
		builder
				.add("name", parameter.name$())
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

	private Frame parameterType(io.intino.konos.model.graph.Parameter parameter) {
		String innerPackage = parameter.isObject() && parameter.asObject().isComponent() ? String.join(".", packageName(), "schemas.") : "";
		final FrameBuilder builder = new FrameBuilder().add("value", innerPackage + parameter.asType().type());
		if (parameter.i$(Data.List.class)) builder.add("list");
		return builder.toFrame();
	}

	private Template template() {
		return Formatters.customize(new RestResourceTemplate());
	}
}
