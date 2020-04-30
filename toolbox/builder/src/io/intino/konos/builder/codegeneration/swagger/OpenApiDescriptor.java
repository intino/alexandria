package io.intino.konos.builder.codegeneration.swagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.intino.konos.builder.codegeneration.swagger.SwaggerSpec.Path.Operation;
import io.intino.konos.builder.codegeneration.swagger.SwaggerSpec.SecurityDefinition;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Service.REST.Resource;
import io.intino.konos.model.graph.Service.REST.Resource.Parameter.In;
import io.intino.magritte.framework.Layer;

import java.util.*;
import java.util.stream.Collectors;

public class OpenApiDescriptor {

	private final Service.REST service;
	private final List<Schema> schemas;

	public OpenApiDescriptor(Service.REST service) {
		this.service = service;
		this.schemas = new ArrayList<>();
	}

	public String createJSONDescriptor() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(create());
	}

	private SwaggerSpec create() {
		SwaggerSpec spec = new SwaggerSpec();
		spec.basePath = service.basePath().isEmpty() ? "/" : service.basePath() + version();
		spec.host = service.host().contains("{") ? "www.example.org" : service.host();
		spec.schemes = service.protocols().stream().map(Enum::name).collect(Collectors.toList());
		spec.paths = new LinkedHashMap<>();
		spec.info = createInfo(service.info());
		for (Resource resource : service.resourceList())
			spec.paths.put(resource.path(), createPath(resource));
		spec.definitions = createDefinitions();
		if (service.authentication() != null)
			if (service.authentication().isBasic()) {
				spec.securityDefinitions = new HashMap<>();
				spec.securityDefinitions.put("basic", new SecurityDefinition().type("basic"));
				spec.security = new ArrayList<>();
				spec.security.add(new SwaggerSpec.SecuritySchema().basic());
			}else if (service.authentication().isBearer()){
				spec.securityDefinitions = new HashMap<>();
				spec.securityDefinitions.put("bearer", new SecurityDefinition().type("bearer"));
				spec.security = new ArrayList<>();
				spec.security.add(new SwaggerSpec.SecuritySchema().bearer());
			}
		return spec;
	}


	private String version() {
		if (service.info() == null || service.info().version() == null) return "";
		return "/" + service.info().version();
	}

	private SwaggerSpec.Info createInfo(Service.REST.Info info) {
		return info == null ? null : new SwaggerSpec.Info(info.version(), info.title(), info.description(), info.termsOfService(), info.contact() == null ? null :
				new SwaggerSpec.Info.Contact(info.contact().name$(), info.contact().email(), info.contact().url()), info.license() == null ? null :
				new SwaggerSpec.Info.License(info.license().name$(), info.license().url()));
	}

	private SwaggerSpec.Path createPath(Resource resource) {
		SwaggerSpec.Path path = new SwaggerSpec.Path();
		for (Resource.Operation op : resource.operationList()) {
			Operation operation = new Operation();
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

	private void addResponse(Map<String, Operation.Response> responses, Response response) {
		Operation.Response swaggerResponse = new Operation.Response();
		if (response != null) {
			swaggerResponse.description = response.description();
			if (response.isObject()) {
				swaggerResponse.schema = new SwaggerSpec.Schema(null, "#/definitions/" + response.asObject().schema().name$());
				this.schemas.add(response.asObject().schema());
			}
		}
		responses.put(response == null ? "200" : response.code(), swaggerResponse);
	}

	private void addResponse(Map<String, Operation.Response> responses, List<Exception> exceptions) {
		for (Exception exception : exceptions) {
			Operation.Response swaggerResponse = new Operation.Response();
			swaggerResponse.description = exception.description();
			if (exception.isObject()) {
				swaggerResponse.schema = new SwaggerSpec.Schema(null, "#/definitions/" + exception.asObject().schema().name$());
				this.schemas.add(exception.asObject().schema());
			}
			responses.put(exception.code().value(), swaggerResponse);
		}
	}

	private void addOperationToPath(SwaggerSpec.Path path, Operation operation, String name) {
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

	private List<Operation.Parameter> createParameters(List<Resource.Parameter> parameters) {
		List<Operation.Parameter> list = new ArrayList<>();
		for (Resource.Parameter parameter : parameters) {
			Operation.Parameter swaggerParameter = new Operation.Parameter();
			swaggerParameter.description = parameter.description();
			swaggerParameter.in = parameter.in().name();
			swaggerParameter.name = parameter.name$();
			swaggerParameter.type = parameterType(parameter.in(), parameter.asType());
			swaggerParameter.required = parameter.in() == In.path || parameter.isRequired();
			if (parameter.isObject()) {
				this.schemas.add(parameter.asObject().schema());
				if (parameter.in() == In.body)
					swaggerParameter.schema = new SwaggerSpec.Schema(null, "#/definitions/" + parameter.asObject().schema().name$());
			}
			list.add(swaggerParameter);
		}
		return list.isEmpty() ? null : list;
	}

	private String parameterType(In in, Data.Type typeData) {
		String type = typeData.type();
		if (typeData.i$(Data.LongInteger.class) || type.equals("java.time.Instant") || type.equalsIgnoreCase("double")) return "number";
		if (typeData.i$(Data.File.class) || type.endsWith("Resource")) return "file";
		if (type.equalsIgnoreCase("java.lang.enum")) return "string";
		if (typeData.i$(Data.Object.class)) {
			if (in == In.body) return null;
			return "string";
		}
		return type.toLowerCase();
	}

	private String transform(Data.Type typeData) {
		String type = typeData.type();
		if (typeData.i$(Data.LongInteger.class) || type.equals("java.time.Instant") || type.equalsIgnoreCase("double")) return "number";
		if (typeData.i$(Data.File.class) || type.endsWith("Resource")) return "file";
		if (type.equalsIgnoreCase("java.lang.enum")) return "string";
		if (typeData.i$(Data.Object.class)) return "object";
		return type.toLowerCase();
	}

	private Map<String, SwaggerSpec.Definition> createDefinitions() {
		Map<String, SwaggerSpec.Definition> map = new LinkedHashMap<>();
		for (Schema schema : schemas) {
			SwaggerSpec.Definition definition = new SwaggerSpec.Definition();
			definition.required = schema.attributeList().stream().filter(Schema.Attribute::isRequired).map(Layer::name$).collect(Collectors.toList());
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


	private SwaggerSpec.Definition.Property propertyFrom(Schema.Attribute attribute) {
		final SwaggerSpec.Definition.Property property = new SwaggerSpec.Definition.Property();
		property.type = transform(attribute.asType());
		return property;
	}

}
