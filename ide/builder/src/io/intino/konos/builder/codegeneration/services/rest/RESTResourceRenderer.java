package io.intino.konos.builder.codegeneration.services.rest;

import com.intellij.openapi.project.Project;
import cottons.utils.MimeTypes;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.action.RESTNotificationActionRenderer;
import io.intino.konos.builder.codegeneration.action.RESTResourceActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Redirect;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.list.ListData;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.rest.RESTService.Notification;
import io.intino.konos.model.graph.rest.RESTService.Resource;
import io.intino.konos.model.graph.rest.RESTService.Resource.Operation;
import io.intino.konos.model.graph.rest.RESTService.Resource.Parameter;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class RESTResourceRenderer {
	private static final String RESOURCES_PACKAGE = "rest/resources";
	private static final String NOTIFICATIONS_PACKAGE = "rest/notifications";
	private final Project project;
	private final List<RESTService> services;
	private File gen;
	private File src;
	private String packageName;
	private final String boxName;
	private final Map<String, String> classes;

	public RESTResourceRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName, Map<String, String> classes) {
		this.project = project;
		this.services = graph.rESTServiceList();
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(RESTService service) {
		service.core$().findNode(Resource.class).forEach(this::processResource);
		service.core$().findNode(Notification.class).forEach(this::processNotification);
	}

	private void processResource(Resource resource) {
		for (Operation operation : resource.operationList()) {
			Frame frame = frameOf(resource, operation);
			final String className = snakeCaseToCamelCase(operation.getClass().getSimpleName() + "_" + resource.name$()) + "Resource";
			Commons.writeFrame(new File(gen, RESOURCES_PACKAGE), className, template().format(frame));
			createCorrespondingAction(operation);
		}
	}

	private void processNotification(Notification notification) {
		final String className = snakeCaseToCamelCase(notification.name$()) + "Notification";
		RESTService service = notification.core$().ownerAs(RESTService.class);
		Frame frame = new Frame("notification").addSlot("path", notification.path());
		addCommonSlots(notification.name$(), frame);
		frame.addSlot("parameter", (AbstractFrame[]) notificationParameters(notification.parameterList()));
		frame.addSlot("returnType", notificationResponse());
		authenticated(service, frame);
		if (service.authenticatedWithToken() != null) frame.addSlot("throws", "Unauthorized");
		Commons.writeFrame(new File(gen, NOTIFICATIONS_PACKAGE), className, template().format(frame));
		createCorrespondingAction(notification);
	}

	private void createCorrespondingAction(Operation operation) {
		new RESTResourceActionRenderer(project, operation, src, packageName, boxName, classes).execute();
	}

	private void createCorrespondingAction(Notification notification) {
		new RESTNotificationActionRenderer(project, notification, src, packageName, boxName, classes).execute();
	}

	private Frame frameOf(Resource resource, Operation operation) {
		Frame frame = new Frame().addTypes("resource");
		addCommonSlots(resource.name$(), frame);
		frame.addSlot("operation", operation.getClass().getSimpleName());
		frame.addSlot("throws", throwCodes(operation));
		frame.addSlot("parameter", (AbstractFrame[]) parameters(operation.parameterList()));
		if (hasResponse(operation)) frame.addSlot("returnType", frameOf(operation.response()));
		if (!resource.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		authenticated(resource.core$().ownerAs(RESTService.class), frame);
		return frame;
	}

	private void authenticated(RESTService service, Frame frame) {
		final RESTService.AuthenticatedWithToken authenticated = service.authenticatedWithToken();
		if (authenticated != null) frame.addSlot("authenticationValidator", new Frame("authenticationValidator").addSlot("type", "Basic"));
	}

	private void addCommonSlots(String name, Frame frame) {
		frame.addSlot("package", packageName);
		frame.addSlot("name", name);
		frame.addSlot("box", boxName);
	}

	private boolean hasResponse(Operation operation) {
		return (operation.response() != null && operation.response().isType()) || operation.response().i$(Redirect.class);
	}

	private Frame frameOf(Response response) {
		Frame frame = new Frame(response.getClass().getSimpleName()).addSlot("value", Commons.returnType(response, packageName));
		if (response.isText() && response.dataFormat() != Response.DataFormat.html)
			frame.addSlot("format", MimeTypes.get(response.dataFormat().toString()));
		return frame;
	}

	private Frame notificationResponse() {
		return new Frame("Response").addSlot("value", String.class.getSimpleName()).addSlot("format", MimeTypes.get("text"));
	}

	private String[] throwCodes(Operation resource) {
		final RESTService.AuthenticatedWithToken authenticated = resource.core$().ownerAs(RESTService.class).authenticatedWithToken();
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
		final Frame parameterFrame = new Frame().addTypes("parameter", parameter.in().toString(), parameter.asType().getClass().getSimpleName(), (parameter.isRequired() ? "required" : "optional"));
		if (parameter.isList()) parameterFrame.addTypes("List");
		return parameterFrame
				.addSlot("name", parameter.name$())
				.addSlot("parameterType", parameterType(parameter))
				.addSlot("in", parameter.in().name());
	}

	private Frame parameter(Notification.Parameter parameter) {
		final Frame parameterFrame = new Frame().addTypes("parameter", parameter.in().toString(), parameter.asType().getClass().getSimpleName(), (parameter.isRequired() ? "required" : "optional"));
		if (parameter.isList()) parameterFrame.addTypes("List");
		return parameterFrame
				.addSlot("name", parameter.name$())
				.addSlot("parameterType", parameterType(parameter))
				.addSlot("in", parameter.in().name());
	}

	private Frame parameterType(io.intino.konos.model.graph.Parameter parameter) {
		String innerPackage = parameter.isObject() && parameter.asObject().isComponent() ? String.join(".", packageName, "schemas.") : "";
		final Frame frame = new Frame().addSlot("value", innerPackage + parameter.asType().type());
		if (parameter.i$(ListData.class)) frame.addTypes("list");
		return frame;
	}

	private Template template() {
		return Formatters.customize(RestResourceTemplate.create());
	}
}
