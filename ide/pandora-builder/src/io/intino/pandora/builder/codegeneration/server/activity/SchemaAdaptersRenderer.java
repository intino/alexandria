package io.intino.pandora.builder.codegeneration.server.activity;

import io.intino.pandora.builder.codegeneration.Formatters;
import io.intino.pandora.model.Activity;
import io.intino.pandora.model.Schema;
import io.intino.pandora.builder.codegeneration.schema.SchemaRenderer;
import io.intino.pandora.builder.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.Collection;


public class SchemaAdaptersRenderer {


	private final Collection<Schema> schemas;
	private final File destination;
	private final String packageName;
	private final Graph graph;

	public SchemaAdaptersRenderer(Graph graph, File destination, String packageName) {
		this.graph = graph;
		this.schemas = findActivitySchemas(this.graph);
		this.destination = destination;
		this.packageName = packageName;
	}

	public void execute() {
		if (graph.find(Activity.class).isEmpty()) return;
		final Frame[] schemaFrames = schemas.stream().map(this::processSchema).toArray(Frame[]::new);
		Commons.writeFrame(new File(destination, "schemas"), "ActivitySchemaAdapters", template().format(new Frame().addTypes("adapters").addSlot("package", packageName).addSlot("schema", schemaFrames)));
	}

	private Template template() {
		Template template = Formatters.customize(SchemaAdapterTemplate.create());
		template.add("typeFormat", (value) -> value.toString().contains(".") ? Formatters.firstLowerCase(value.toString()) : value);
		return template;
	}


	private Frame processSchema(Schema element) {
		return SchemaRenderer.createSchemaFrame(element, packageName);
	}

	private Collection<Schema> findActivitySchemas(Graph graph) {
		return graph.find(Schema.class);
	}

}
