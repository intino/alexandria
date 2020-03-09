package io.intino.konos.builder.codegeneration.swagger;

import io.intino.alexandria.logger.Logger;
import io.intino.konos.model.graph.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class SwaggerProfileGenerator {

	private final List<Service.REST> services;
	private final File directory;

	public SwaggerProfileGenerator(List<Service.REST> services, File directory) {
		this.services = services;
		this.directory = directory;
	}

	public void execute() {
		services.stream().filter(Service.REST::generateDocs).forEach(service -> writeFile(service, new OpenApiDescriptor(service).createJSONDescriptor()));
	}

	private void writeFile(Service.REST service, String json) {
		try {
			Files.write(new File(directory, service.name$() + ".json").toPath(), json.getBytes(Charset.forName("UTF-8"))).toFile().getPath();
		} catch (IOException e) {
			Logger.error(e);
		}
	}
}
