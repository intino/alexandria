package io.intino.konos.builder.codegeneration.server.activity;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.schema.SchemaRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Activity;
import io.intino.konos.model.Schema;
import io.intino.konos.model.Service;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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

	private Frame processSchema(Schema schema) {
		final Service service = schema.ownerAs(Service.class);
		String subPackage = "schemas" + (service != null ? File.separator + service.name().toLowerCase() : "");
		return SchemaRenderer.createSchemaFrame(schema, subPackage.isEmpty() ? packageName : packageName + "." + subPackage.replace(File.separator, "."), packageName);
	}

	private Collection<Schema> findActivitySchemas(Graph graph) {
		List<Schema> schemas = new ArrayList<>();
		for (Schema schema : graph.find(Schema.class)) if (!isAlreadyAdded(schema, schemas)) schemas.add(schema);
		return schemas;
	}

	private boolean isAlreadyAdded(Schema schema, List<Schema> schemas) {
		for (Schema anSchema : schemas) if (schema.name().equals(anSchema.name())) return true;
		return false;
	}

}
