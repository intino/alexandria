package teseo.codegeneration.accessor;

import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import teseo.Resource;
import teseo.Schema;
import teseo.codegeneration.schema.SchemaRenderer;
import teseo.date.DateData;
import teseo.datetime.DateTimeData;
import teseo.file.FileData;
import teseo.helpers.Commons;
import teseo.html.HtmlData;
import teseo.object.ObjectData;
import teseo.rest.RESTService;
import teseo.type.TypeData;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.writeFrame;

public class JavaAccessorRenderer {
	private final RESTService service;
	private File destination;
	private String packageName;

	public JavaAccessorRenderer(RESTService application) {
		this.service = application;
	}

	public void execute(File destination, String packageName) {
		this.destination = destination;
		this.packageName = packageName;
		new SchemaRenderer(service.graph()).execute(destination, packageName);
		processService(service);
	}

	private void processService(RESTService restService) {
		Frame frame = new Frame().addTypes("accessor");
		frame.addSlot("name", restService.name());
		frame.addSlot("package", packageName);
		if (!restService.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		frame.addSlot("resources", (AbstractFrame[]) processResources(restService.node().findNode(Resource.class)));
		writeFrame(destination, snakeCaseToCamelCase(restService.name()) + "Accessor", getTemplate().format(frame));
	}

	private Frame[] processResources(List<Resource> resources) {
		return resources.stream().map(this::processResource).toArray(Frame[]::new);
	}

	private Frame processResource(Resource resource) {
		return new Frame().addTypes("resource", resource.method().toString())
				.addSlot("returnType", Commons.returnType(resource.responseList()))
				.addSlot("name", resource.name())
				.addSlot("parameters", (AbstractFrame[]) parameters(resource.parameterList()))
				.addSlot("invokeSentence", invokeSentence(resource))
				.addSlot("exceptionResponses", exceptionResponses(resource));
	}

	private Frame[] parameters(List<Resource.Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Resource.Parameter parameter) {
		return new Frame().addTypes("parameter", parameter.in().toString(), (parameter.required() ? "required" : "optional"), parameter.asType().getClass().getSimpleName())
				.addSlot("name", parameter.name())
				.addSlot("parameterType", parameter.asType().type());
	}

	private Frame invokeSentence(Resource resource) {
		Resource.Response response = Commons.successResponse(resource.responseList());
		Frame result;
		if (response.asType() == null) result = voidInvokeSentence();
		else if (response.isObject()) result = objectInvokeSentence(response.asObject());
		else if (response.isFile()) result = fileInvokeSentence(response.asFile());
		else if (response.isHtml()) result = htmlInvokeSentence(response.asHtml());
		else if (response.isDate()) result = dateInvokeSentence(response.asDate());
		else if (response.isDateTime()) result = dateTimeInvokeSentence(response.asDateTime());
		else result = primitiveInvokeSentence(response.asType());
		return result.addSlot("doInvoke", doInvoke(resource));
	}

	private Frame doInvoke(Resource resource) {
		return new Frame().addTypes("doInvoke")
				.addSlot("relativePath", Commons.path(resource))
				.addSlot("method", resource.method().toString())
				.addSlot("parameters", Commons.pathParameters(resource));
	}

	private Frame exceptionResponses(Resource resource) {
		List<Resource.Response> responses = Commons.nonSuccessResponse(resource.responseList());
		if (responses.isEmpty()) return new Frame().addTypes("exceptionResponses", "none");
		return new Frame().addTypes("exceptionResponses")
				.addSlot("exceptionResponse", (AbstractFrame[]) exceptionResponses(responses));
	}

	private Frame[] exceptionResponses(List<Resource.Response> responses) {
		return responses.stream().map(this::exceptionResponse).toArray(Frame[]::new);
	}

	private Frame exceptionResponse(Resource.Response response) {
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

	private Frame htmlInvokeSentence(HtmlData htmlData) {
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
		Template template = JavaAccessorTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

}
