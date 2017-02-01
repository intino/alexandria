package io.intino.konos.builder.codegeneration.accessor.rest.swagger;

import io.intino.konos.model.Konos;
import io.intino.konos.model.rest.RESTService;
import io.swagger.codegen.SwaggerCodegen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class SwaggerGenerator {
	private final Konos application;
	private final File outDirectory;

	public SwaggerGenerator(Konos application, File outDirectory) {
		this.application = application;
		this.outDirectory = outDirectory;
	}

	public void execute(List<String> targetLanguages) {
		for (RESTService restService : application.rESTServiceList()) {
			final String jsonFilePath = writeFile(new OpenApiDescriptor(restService).createJSONDescriptor());
			SwaggerCodegen.main(new String[]{"generate", "-i", jsonFilePath, "-o", outDirectory.getPath(), "-l", targetLanguages.get(0)});
		}
	}

	private String writeFile(String json) {
		try {
			return Files.write(Files.createTempFile("__swagger", ".json"), json.getBytes()).toFile().getPath();
		} catch (IOException ignored) {
			return "";
		}
	}
}
