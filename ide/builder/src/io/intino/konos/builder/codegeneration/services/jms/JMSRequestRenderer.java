package io.intino.konos.builder.codegeneration.services.jms;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.action.JMSRequestActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.jms.JMSService;
import io.intino.konos.model.graph.jms.JMSService.Request;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMSRequestRenderer {
	private static final String REQUESTS = "requests";
	private final Project project;
	private final List<JMSService> services;
	private File gen;
	private File src;
	private String packageName;
	private final String boxName;
	private final Map<String, String> classes;

	public JMSRequestRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName, Map<String, String> classes) {
		this.project = project;
		this.services = graph.jMSServiceList();
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(JMSService service) {
		service.core$().findNode(Request.class).forEach(this::processRequest);
	}

	private void processRequest(Request resource) {
		Frame frame = fillRequestFrame(resource);
		Commons.writeFrame(new File(gen, REQUESTS), snakeCaseToCamelCase(resource.name$()) + "Request", template().format(frame));
		createCorrespondingAction(resource);
	}

	private void createCorrespondingAction(Request request) {
		new JMSRequestActionRenderer(project, request, src, packageName, boxName, classes).execute();
	}

	private Frame fillRequestFrame(Request request) {
		final String returnType = Commons.returnType(request.response());
		Frame frame = new Frame().addTypes("request").
				addSlot("name", request.name$()).
				addSlot("box", boxName).
				addSlot("package", packageName).
				addSlot("call", new Frame().addTypes(returnType)).
				addSlot("parameter", (AbstractFrame[]) parameters(request.parameterList()));
		if (!returnType.equals("void"))
			frame.addSlot("returnType", returnType).addSlot("returnMessageType", messageType(request.response()));
		if (!request.exceptionList().isEmpty() || !request.exceptionRefs().isEmpty())
			frame.addSlot("exception", "");
		if (!request.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		return frame;
	}

	private String messageType(Response response) {
		return response.isFile() ? "Bytes" : "Text";
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		final Frame frame = new Frame();
		if (parameter.isList()) frame.addTypes("List");
		return frame.addTypes("parameter", parameter.asType().getClass().getSimpleName())
				.addSlot("name", parameter.name$())
				.addSlot("type", parameter.asType().type());
	}

	private Template template() {
		return Formatters.customize(JMSRequestTemplate.create());
	}
}
