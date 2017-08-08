package io.intino.konos.builder.codegeneration.swagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.rest.RESTService.Resource;
import io.intino.tara.magritte.Layer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenApiDescriptor {

	private final RESTService restService;
	private final List<Schema> schemas;

	public OpenApiDescriptor(RESTService restService) {
		this.restService = restService;
		this.schemas = restService.graph().schemaList();
	}

	public String createJSONDescriptor() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(create());
	}

	private SwaggerSpec create() {
		SwaggerSpec spec = new SwaggerSpec();
		spec.basePath = restService.basePath();
		spec.definitions = createDefinitions();
		spec.host = restService.host();
		spec.schemes = restService.protocols().stream().map(Enum::name).collect(Collectors.toList());
		spec.paths = new LinkedHashMap<>();
		spec.info = createInfo(restService.info());
		for (Resource resource : restService.resourceList())
			spec.paths.put(resource.path(), createPath(resource));
		return spec;
	}

	private SwaggerSpec.Info createInfo(RESTService.Info info) {
		return info == null ? null : new SwaggerSpec.Info(info.version(), info.title(), info.description(), info.termsOfService(), info.contact() == null ? null :
				new SwaggerSpec.Info.Contact(info.contact().name$(), info.contact().email(), info.contact().url()), info.license() == null ? null :
				new SwaggerSpec.Info.License(info.license().name$(), info.license().url()));
	}

	private SwaggerSpec.Path createPath(Resource resource) {
		SwaggerSpec.Path path = new SwaggerSpec.Path();
		for (Resource.Operation op : resource.operationList()) {
			SwaggerSpec.Path.Operation operation = new SwaggerSpec.Path.Operation();
			operation.description = op.description().isEmpty() ? null : op.description();
			operation.summary = op.summary().isEmpty() ? null : op.summary();
			operation.operationId = op.name$();
			operation.tags = op.tags().isEmpty() ? null : op.tags();
			operation.parameters = createParameters(op.parameterList());
			addResponse(operation.responses, op.response());
			addResponse(operation.responses, op.exceptionList());
			addOperationToPath(path, operation, op.getClass().getSimpleName());
		}
		return path;
	}

	private void addResponse(Map<String, SwaggerSpec.Path.Operation.Response> responses, Response response) {
		SwaggerSpec.Path.Operation.Response swaggerResponse = new SwaggerSpec.Path.Operation.Response();
		swaggerResponse.description = response.description();
		if (response.isObject())
			swaggerResponse.schema = new SwaggerSpec.Path.Operation.Response.Schema(null, "#/definitions/" + response.asObject().schema().name$());
		responses.put(response.code(), swaggerResponse);
	}

	private void addResponse(Map<String, SwaggerSpec.Path.Operation.Response> responses, List<Exception> exceptions) {
		for (Exception exception : exceptions) {
			SwaggerSpec.Path.Operation.Response swaggerResponse = new SwaggerSpec.Path.Operation.Response();
			swaggerResponse.description = exception.description();
			if (exception.isObject()) {
				swaggerResponse.schema = new SwaggerSpec.Path.Operation.Response.Schema(null, "#/definitions/" + exception.asObject().schema().name$());
			}
			responses.put(exception.code().value(), swaggerResponse);
		}
	}

	private void addOperationToPath(SwaggerSpec.Path path, SwaggerSpec.Path.Operation operation, String name) {
		switch (name) {
			case "Get":
				path.get = operation;
				break;
			case "Post":
				path.post = operation;
				break;
			case "patch":
				path.patch = operation;
				break;
			case "Put":
				path.put = operation;
				break;
			case "Delete":
				path.delete = operation;
				break;
			default:
				path.head = operation;
				break;
		}
	}

	private List<SwaggerSpec.Path.Operation.Parameter> createParameters(List<Resource.Parameter> parameters) {
		List<SwaggerSpec.Path.Operation.Parameter> list = new ArrayList<>();
		for (Resource.Parameter parameter : parameters) {
			SwaggerSpec.Path.Operation.Parameter swaggerParameter = new SwaggerSpec.Path.Operation.Parameter();
			swaggerParameter.description = parameter.description();
			swaggerParameter.in = parameter.in().name();
			swaggerParameter.name = parameter.name$();
			swaggerParameter.type = parameter.asType().type().toLowerCase();
			swaggerParameter.required = parameter.required();
			list.add(swaggerParameter);
		}
		return list.isEmpty() ? null : list;
	}

	private Map<String, SwaggerSpec.Definition> createDefinitions() {
		Map<String, SwaggerSpec.Definition> map = new LinkedHashMap<>();
		for (Schema schema : schemas) {
			SwaggerSpec.Definition definition = new SwaggerSpec.Definition();
			definition.required = schema.attributeList().stream().filter(Schema.Attribute::required).map(Layer::name$).collect(Collectors.toList());
			definition.properties = toMap(schema.attributeList());
			map.put(schema.name$(), definition);
		}
		return map;
	}

	private Map<String, SwaggerSpec.Definition.Property> toMap(List<Schema.Attribute> attributes) {
		Map<String, SwaggerSpec.Definition.Property> map = new LinkedHashMap<>();
		for (Schema.Attribute attribute : attributes) map.put(attribute.name$(), propertyFrom(attribute));
		return map;
	}

	@NotNull
	private SwaggerSpec.Definition.Property propertyFrom(Schema.Attribute attribute) {
		final SwaggerSpec.Definition.Property property = new SwaggerSpec.Definition.Property();
		property.type = attribute.asType().type().toLowerCase();
		return property;
	}

}
