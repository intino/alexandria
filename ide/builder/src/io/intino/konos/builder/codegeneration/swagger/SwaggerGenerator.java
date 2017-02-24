package io.intino.konos.builder.codegeneration.swagger;

import io.intino.konos.model.rest.RESTService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class SwaggerGenerator {
	private final List<RESTService> services;
	private final File outDirectory;

	public SwaggerGenerator(List<RESTService> services, File outDirectory) {
		this.services = services;
		this.outDirectory = outDirectory;
		this.outDirectory.mkdirs();
	}

	public void execute(List<String> targetLanguages) {
		for (RESTService restService : services) {
			final String jsonFilePath = writeFile(new OpenApiDescriptor(restService).createJSONDescriptor());
			io.swagger.codegen.Codegen.main(new String[]{"generate", "-i", jsonFilePath, "-o", outDirectory.getPath(), "-l", "io.swagger.codegen.languages.StaticDocCodegen"});
			new File(outDirectory, "main.js").delete();
			new File(outDirectory, "package.json").delete();
			new File(jsonFilePath).delete();
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
