package io.intino.konos.builder.codegeneration.schema;

import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Schema;

import java.io.File;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SchemaListRenderer extends Renderer {
	private final List<Schema> schemas;
	private final File destination;
	private final String packageName;
	private final boolean serializationAnnotations;
	private final SchemaWriter writer;

	public SchemaListRenderer(CompilationContext context, KonosGraph graph) {
		this(context, graph, null);
	}

	public SchemaListRenderer(CompilationContext context, KonosGraph graph, File destination) {
		this(context, graph, destination, null);
	}

	public SchemaListRenderer(CompilationContext context, KonosGraph graph, File destination, String packageName) {
		super(context);
		this.schemas = graph.core$().find(Schema.class).stream().filter(s -> !s.core$().owner().is(Schema.class)).collect(toList());
		this.destination = destination != null ? destination : gen(Target.Server);
		this.packageName = packageName != null ? packageName : context.packageName();
		this.serializationAnnotations = false;
		this.writer = new DefaultSchemaWriter(context, this.destination, this.packageName, false);
	}

	public SchemaListRenderer(CompilationContext context, KonosGraph graph, File destination, String packageName, boolean serializationAnnotations) {
		super(context);
		this.schemas = graph.core$().find(Schema.class).stream().filter(s -> !s.core$().owner().is(Schema.class)).collect(toList());
		this.destination = destination != null ? destination : gen(Target.Server);
		this.packageName = packageName;
		this.serializationAnnotations = serializationAnnotations;
		this.writer = new DefaultSchemaWriter(context, this.destination, this.packageName, this.serializationAnnotations);
	}

	public SchemaListRenderer(CompilationContext context, KonosGraph graph, File destination, String packageName, SchemaWriter writer) {
		super(context);
		this.schemas = graph.core$().find(Schema.class).stream().filter(s -> !s.core$().owner().is(Schema.class)).collect(toList());
		this.destination = destination != null ? destination : gen(Target.Server);
		this.packageName = packageName;
		this.serializationAnnotations = false;
		this.writer = writer;
	}

	public void render() throws KonosException {
		for (Schema schema : schemas)
			new SchemaRenderer(context, schema, destination, packageName, serializationAnnotations, writer).execute();
	}
}
