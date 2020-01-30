package io.intino.konos.builder.codegeneration.schema;

import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.intino.konos.builder.helpers.Commons.javaFile;
import static java.util.stream.Collectors.toList;

public class SchemaListRenderer extends Renderer {
	private final List<Service> services;
	private final List<Schema> schemas;
	private final File destination;
	private final String packageName;

	public SchemaListRenderer(CompilationContext compilationContext, KonosGraph graph) {
		this(compilationContext, graph, null);
	}

	public SchemaListRenderer(CompilationContext compilationContext, KonosGraph graph, File destination) {
		this(compilationContext, graph, destination, null);
	}

	public SchemaListRenderer(CompilationContext compilationContext, KonosGraph graph, File destination, String packageName) {
		super(compilationContext, Target.Owner);
		this.schemas = graph.core$().find(Schema.class).stream().filter(s -> !s.core$().owner().is(Schema.class)).collect(toList());
		this.services = graph.serviceList();
		this.destination = destination != null ? destination : gen();
		this.packageName = packageName != null ? packageName : compilationContext.packageName();
	}

	public void clean() {
		List<File> serviceFolders = services.stream().map(this::serviceFolder).filter(File::exists).collect(toList());
		if (serviceFolder(null).exists()) serviceFolders.add(serviceFolder(null));
		if (serviceFolders.size() <= 0) return;
		List<String> filenames = schemas.stream().map(s -> javaFile(schemaFolder(s), s.name$()).getAbsolutePath()).collect(toList());
		List<File> filesToDelete = serviceFolders.stream().map(f -> Arrays.asList(Objects.requireNonNull(f.listFiles()))).flatMap(List::stream).filter(f -> !filenames.contains(f.getAbsolutePath())).collect(toList());
		filesToDelete.forEach(File::delete);
	}

	public void render() {
		schemas.forEach(s -> new SchemaRenderer(compilationContext, s, destination, packageName).execute());
	}

	private String subPackage(Schema schema) {
		return subPackage(schema.core$().ownerAs(Service.class));
	}

	private String subPackage(Service service) {
		return "schemas" + (service != null ? File.separator + service.name$().toLowerCase() : "");
	}

	private File schemaFolder(Schema schema) {
		return new File(destination, subPackage(schema));
	}

	private File serviceFolder(Service service) {
		return new File(destination, subPackage(service));
	}

}
