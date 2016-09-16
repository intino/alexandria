package teseo.codegeneration.server.rest;

import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Resource;
import teseo.Response;
import teseo.Schema;
import teseo.codegeneration.action.MethodActionRenderer;
import teseo.helpers.Commons;
import teseo.rest.RESTService;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.writeFrame;

public class RESTResourceRenderer {
	private final List<RESTService> services;
	private File gen;
	private File src;
	private String packageName;
	private static final String RESOURCES = "resources";

	public RESTResourceRenderer(Graph graph, File gen, File src, String packageName) {
		services = graph.find(RESTService.class);
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(RESTService service) {
		service.node().findNode(Resource.class).forEach(this::processResource);
	}

	private void processResource(Resource resource) {
		Frame frame = fillResourceFrame(resource);
		writeFrame(new File(gen, RESOURCES), resource.name() + "Resource", template().format(frame));
		createCorrespondingAction(resource);
	}

	private void createCorrespondingAction(Resource resource) {
		new MethodActionRenderer(resource, src, packageName).execute();
	}

	private Frame fillResourceFrame(Resource resource) {
		Frame frame = new Frame().addTypes("resource");
		frame.addSlot("name", resource.name());
		frame.addSlot("package", packageName);
		frame.addSlot("doTask", doTask(resource));
		frame.addSlot("throws", throwCodes(resource));
		frame.addSlot("returnType", Commons.returnType(resource.response()));
		frame.addSlot("parameter", (AbstractFrame[]) parameters(resource.resourceParameterList()));
		if (!resource.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		return frame;
	}

	private AbstractFrame doTask(Resource resource) {
		Response response = resource.response();
		return new Frame().addTypes(response.asType() == null ? "void" : response.isObject() ? "object" : "other")
				.addSlot("returnType", Commons.returnType(resource.response()));
	}

	private String[] throwCodes(Resource resource) {
		String[] throwCodes = resource.exceptionList().stream().map(r -> r.code().toString()).toArray(String[]::new);
		return throwCodes.length == 0 ? new String[]{"ErrorUnknown"} : throwCodes;
	}

	private Frame[] parameters(List<Resource.Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Resource.Parameter parameter) {
		return new Frame().addTypes("parameter", parameter.in().toString(), parameter.asType().getClass().getSimpleName(), (parameter.required() ? "required" : "optional"))
				.addSlot("name", parameter.name())
				.addSlot("parameterType", parameter.asType().type())
				.addSlot("in", parameter.in().name());
	}

	private Template template() {
		Template template = RestResourceTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

}
