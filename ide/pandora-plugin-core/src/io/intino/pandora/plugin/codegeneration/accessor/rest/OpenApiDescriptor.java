package io.intino.pandora.plugin.codegeneration.accessor.rest;

import com.google.gson.Gson;
import io.intino.pandora.plugin.Exception;
import io.intino.pandora.plugin.Response;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.codegeneration.accessor.rest.swagger.SwaggerSpec;
import io.intino.pandora.plugin.rest.RESTService;
import io.intino.pandora.plugin.rest.RESTService.Resource;

import java.util.*;

public class OpenApiDescriptor {

	private final RESTService restService;
	private final List<Schema> schemas;

	public OpenApiDescriptor(RESTService restService) {
		this.restService = restService;
		this.schemas = restService.graph().find(Schema.class);
	}

	public String createJSONDescriptor() {
		Gson gson = new Gson();
		return gson.toJson(fill(new SwaggerSpec()));
	}

	private SwaggerSpec fill(SwaggerSpec spec) {
		spec.basePath = restService.basePath();
		spec.definitions = createDefinitions();
		spec.host = "petstore.swagger.io";//restService.host();
		spec.schemas = Collections.singletonList("http");
		spec.paths = new LinkedHashMap<>();
		for (Resource resource : restService.resourceList()) {
			spec.paths.put(resource.path(), createPath(resource));
		}
		return spec;
	}

	private SwaggerSpec.Path createPath(Resource resource) {
		SwaggerSpec.Path path = new SwaggerSpec.Path();
		for (Resource.Operation op : resource.operationList()) {
			SwaggerSpec.Path.Operation operation = new SwaggerSpec.Path.Operation();
			operation.description = op.description();
			operation.operationId = op.id();
			addParameters(operation, op.parameterList());
			addResponse(operation.responses, op.response());
			addResponse(operation.responses, op.exceptionList());
		}
		return path;
	}

	private void addResponse(Map<String, SwaggerSpec.Path.Operation.Response> responses, Response response) {
		SwaggerSpec.Path.Operation.Response swaggerResponse = new SwaggerSpec.Path.Operation.Response();
		swaggerResponse.description = response.description();
		responses.put("200", swaggerResponse);
	}

	private void addResponse(Map<String, SwaggerSpec.Path.Operation.Response> responses, List<Exception> exceptions) {
		for (Exception exception : exceptions) {
			SwaggerSpec.Path.Operation.Response swaggerResponse = new SwaggerSpec.Path.Operation.Response();
			swaggerResponse.description = exception.description();
			responses.put(exception.code().value(), swaggerResponse);
		}
	}

	private void addParameters(SwaggerSpec.Path.Operation operation, List<Resource.Parameter> parameters) {
		List<SwaggerSpec.Path.Operation.Parameter> list = new ArrayList<>();
		for (Resource.Parameter parameter : parameters) {
			SwaggerSpec.Path.Operation.Parameter swaggerParameter = new SwaggerSpec.Path.Operation.Parameter();
			swaggerParameter.description = parameter.description();
			swaggerParameter.in = parameter.in().name();
			swaggerParameter.name = parameter.name();
			swaggerParameter.type = parameter.asType().type();
		}
		operation.parameters = list;
	}

	private Map<String, SwaggerSpec.Definition> createDefinitions() {
		Map<String, SwaggerSpec.Definition> map = new LinkedHashMap<>();
		for (Schema schema : schemas) {
			SwaggerSpec.Definition definition = new SwaggerSpec.Definition();
			map.put(schema.name(), definition);
		}
		return map;
	}

}
