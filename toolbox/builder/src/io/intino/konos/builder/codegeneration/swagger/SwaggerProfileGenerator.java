package io.intino.konos.builder.codegeneration.swagger;

import io.intino.konos.model.graph.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class SwaggerProfileGenerator {
	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

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
			logger.error(e.getMessage(), e);
		}
	}
}
