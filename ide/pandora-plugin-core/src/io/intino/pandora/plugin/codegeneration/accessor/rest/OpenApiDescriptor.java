package io.intino.pandora.plugin.codegeneration.accessor.rest;

import com.google.gson.Gson;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.codegeneration.accessor.rest.swagger.SwaggerSpec;
import io.intino.pandora.plugin.rest.RESTService;
import io.intino.pandora.plugin.rest.RESTService.Resource;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OpenApiDescriptor {

	private final RESTService restService;
	private final List<Schema> formats;

	public OpenApiDescriptor(RESTService restService) {
		this.restService = restService;
		this.formats = restService.graph().find(Schema.class);
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
		}
		return path;
	}

	private Map<String, SwaggerSpec.Definition> createDefinitions() {
		for (Schema format : formats) {

		}
		return null;
	}

}
