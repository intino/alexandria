package io.intino.pandora.plugin.codegeneration.accessor.rest;

import com.google.gson.Gson;
import io.intino.pandora.plugin.Format;
import io.intino.pandora.plugin.Resource;
import io.intino.pandora.plugin.codegeneration.accessor.rest.swagger.SwaggerSpec;
import io.intino.pandora.plugin.rest.RESTService;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OpenApiDescriptor {

	private final RESTService restService;
	private final List<Format> formats;

	public OpenApiDescriptor(RESTService restService) {
		this.restService = restService;
		this.formats = restService.graph().find(Format.class);
	}

	public String createJSONDescriptor() {
		Gson gson = new Gson();
		return gson.toJson(fill(new SwaggerSpec()));
	}

	private SwaggerSpec fill(SwaggerSpec spec) {
		spec.basePath = restService.path();
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
		SwaggerSpec.Path.Operation swaggerResource = new SwaggerSpec.Path.Operation();
		swaggerResource.description = resource.description();
		swaggerResource.operationId = resource.id();
		return path;
	}

	private Map<String, SwaggerSpec.Definition> createDefinitions() {
		for (Format format : formats) {

		}
		return null;
	}

}
