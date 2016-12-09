package io.intino.pandora.plugin.codegeneration.server.activity;

import io.intino.pandora.plugin.Activity;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.codegeneration.schema.SchemaRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.Collection;

import static io.intino.pandora.plugin.codegeneration.server.rest.RESTResourceRenderer.firstLowerCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

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
		writeFrame(new File(destination, "schemas"), "ActivitySchemaAdapters", template().format(new Frame().addTypes("adapters").addSlot("package", packageName).addSlot("schema", schemaFrames)));
	}

	private Template template() {
		final Template template = SchemaAdapterTemplate.create();
		template.add("ValidPackage", Commons::validPackage);
		template.add("typeFormat", (value) -> value.toString().contains(".") ? firstLowerCase(value.toString()) : value);
		return template;
	}

	private Frame processSchema(Schema element) {
		return SchemaRenderer.createSchemaFrame(element, packageName);
	}

	private Collection<Schema> findActivitySchemas(Graph graph) {
		return graph.find(Schema.class);
	}

}
