package io.intino.konos.builder.codegeneration.schema;

import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Schema;

import java.io.File;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SchemaListRenderer extends Renderer {
	private final List<Schema> schemas;
	private final File destination;
	private final String packageName;
	private final boolean serializationAnnotations;

	public SchemaListRenderer(CompilationContext context, KonosGraph graph) {
		this(context, graph, null);
	}

	public SchemaListRenderer(CompilationContext context, KonosGraph graph, File destination) {
		this(context, graph, destination, null);
	}

	public SchemaListRenderer(CompilationContext context, KonosGraph graph, File destination, String packageName) {
		super(context, Target.Owner);
		this.schemas = graph.core$().find(Schema.class).stream().filter(s -> !s.core$().owner().is(Schema.class)).collect(toList());
		this.destination = destination != null ? destination : gen();
		this.packageName = packageName != null ? packageName : context.packageName();
		this.serializationAnnotations = false;
	}

	public SchemaListRenderer(CompilationContext context, KonosGraph graph, File destination, boolean serializationAnnotations) {
		super(context, Target.Owner);
		this.schemas = graph.core$().find(Schema.class).stream().filter(s -> !s.core$().owner().is(Schema.class)).collect(toList());
		this.destination = destination != null ? destination : gen();
		this.packageName = context.packageName();
		this.serializationAnnotations = serializationAnnotations;
	}

	public void render() throws KonosException {
		for (Schema schema : schemas) new SchemaRenderer(context, schema, destination, packageName, serializationAnnotations).execute();
	}
}
