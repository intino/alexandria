package io.intino.konos.builder.codegeneration.schema;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Data;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.intino.konos.builder.helpers.Commons.javaFile;
import static java.util.Collections.addAll;

public class SchemaRenderer extends Renderer {
	private final Schema schema;
	private final File destination;
	private final String packageName;

	public SchemaRenderer(CompilationContext compilationContext, Schema schema, File destination, String packageName) {
		super(compilationContext, Target.Owner);
		this.schema = schema;
		this.destination = destination != null ? destination : gen();
		this.packageName = packageName != null ? packageName : compilationContext.packageName();
	}

	public void render() {
		String rootPackage = packageName;
		String subPackage = subPackage(schema);
		final File packageFolder = schemaFolder(schema);
		final String packageName = subPackage.isEmpty() ? rootPackage : rootPackage + "." + subPackage.replace(File.separator, ".");
		final Frame frame = createSchemaFrame(schema, packageName);
		classes().put(Schema.class.getSimpleName() + "#" + schema.name$(), subPackage.replace(File.separator, ".") + "." + schema.name$());
		Commons.writeFrame(packageFolder, schema.name$(), template().render(new FrameBuilder("root").add("root", rootPackage).add("package", packageName).add("schema", frame)));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(schema), javaFile(packageFolder, schema.name$()).getAbsolutePath()));
	}

	public Frame createSchemaFrame(Schema schema, String packageName) {
		return createSchemaFrame(schema, packageName, new HashSet<>());
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

	private Frame createSchemaFrame(Schema schema, String packageName, Set<Schema> processed) {
		FrameBuilder builder = new FrameBuilder("schema").add("name", schema.name$()).add("package", packageName);
		if (schema.core$().owner().is(Schema.class)) builder.add("inner", "static");
		builder.add("attribute", collectAttributes(schema));
		if (schema.isExtensionOf()) builder.add("parent", schema.asExtensionOf().parent().name$());
		final Frame[] components = components(schema, packageName, processed);
		if (components.length > 0) builder.add("schema", components);
		return builder.toFrame();
	}


	private Frame[] components(Schema schema, String packageName, Set<Schema> processed) {
		return schema.schemaList().stream().filter(processed::add).map(s -> createSchemaFrame(s, packageName, processed)).toArray(Frame[]::new);
	}


	private FrameBuilder[] collectAttributes(Schema schema) {
		List<FrameBuilder> attributes = new ArrayList<>();
		addAll(attributes, processAttributes(schema.attributeList()));
		addAll(attributes, processSchemasAsAttribute(schema.schemaList()));
		attributes.forEach(f -> f.add("element", schema.name$()));
		return attributes.toArray(new FrameBuilder[0]);
	}

	private FrameBuilder[] processAttributes(List<Schema.Attribute> attributes) {
		return attributes.stream().map(this::process).toArray(FrameBuilder[]::new);
	}

	private FrameBuilder[] processSchemasAsAttribute(List<Schema> schemas) {
		return schemas.stream().map(schema -> processSchema(schema, schema.name$(), schema.multiple())).toArray(FrameBuilder[]::new);
	}

	private FrameBuilder process(Schema.Attribute attribute) {
		if (attribute.isReal()) return process(attribute.asReal());
		else if (attribute.isInteger()) return process(attribute.asInteger());
		else if (attribute.isBool()) return process(attribute.asBool());
		else if (attribute.isText()) return process(attribute.asText());
		else if (attribute.isDateTime()) return process(attribute.asDateTime());
		else if (attribute.isDate()) return process(attribute.asDate());
		else if (attribute.isFile()) return process(attribute.asFile());
		else if (attribute.isLongInteger()) return process(attribute.asLongInteger());
		else if (attribute.isWord()) return process(attribute.asWord());
		else if (attribute.isMap()) return process(attribute.asMap());
		else if (attribute.isObject())
			return processObjectAttribute(attribute.asObject().schema(), attribute.name$(), attribute.isList());
		return null;
	}

	private FrameBuilder process(Data.Real attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", "double")
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", !multiple(attribute) ? "double" : attribute.type())
				.add("defaultValue", attribute.defaultValue());
	}

	private FrameBuilder process(Data.Integer attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", !multiple(attribute) ? "int" : attribute.type())
				.add("defaultValue", attribute.defaultValue());
	}

	private FrameBuilder process(Data.LongInteger attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type())
				.add("defaultValue", attribute.defaultValue() + "L");
	}

	private FrameBuilder process(Data.File attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type());
	}

	private FrameBuilder process(Data.Bool attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type())
				.add("defaultValue", attribute.defaultValue());
	}

	private FrameBuilder process(Data.Text attribute) {
		FrameBuilder builder = new FrameBuilder(multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type());
		if (attribute.defaultValue() != null) builder.add("defaultValue", "\"" + attribute.defaultValue() + "\"");
		return builder;
	}

	private FrameBuilder process(Data.DateTime attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type());
	}

	private FrameBuilder process(Data.Date attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type());
	}

	private FrameBuilder process(Data.Word attribute) {
		final Schema.Attribute a = attribute.a$(Schema.Attribute.class);
		return new FrameBuilder("word", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", a.name$())
				.add("words", attribute.values().toArray(new String[0]))
				.add("type", a.name$());
	}

	private FrameBuilder process(Data.Map attribute) {
		return new FrameBuilder("map", attribute.value().isList() ? "valueList" : "valueSingle", attribute.key().isList() ? "keyList" : "keySingle")
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("key", new FrameBuilder(attribute.key().isList() ? "list" : "single").add("type", attribute.key().asType().type()))
				.add("value", new FrameBuilder(attribute.value().isList() ? "list" : "single").add("type", attribute.value().asType().type()));
	}

	private FrameBuilder processObjectAttribute(Schema schema, String name, boolean multiple) {
		return new FrameBuilder(multiple ? "multiple" : "single", "object", schema.name$())
				.add("name", name)
				.add("type", schema.name$())
				.add("package", packageOf(schema));
	}

	private FrameBuilder processSchema(Schema schema, String name, boolean multiple) {
		return new FrameBuilder(multiple ? "multiple" : "single", "schema", schema.name$())
				.add("name", name)
				.add("type", schema.name$())
				.add("package", packageOf(schema));
	}

	private String packageOf(Schema schema) {
		final Service service = schema.core$().ownerAs(Service.class);
		String rootPackage = packageName;
		String subPackage = "schemas" + (service != null ? File.separator + service.name$().toLowerCase() : "");
		return subPackage.isEmpty() ? rootPackage : rootPackage + "." + subPackage.replace(File.separator, ".");
	}

	private boolean multiple(Data.Type attribute) {
		return attribute.asData().isList();
	}


	private Template template() {
		return Formatters.customize(new SchemaTemplate()).add("typeFormat", (value) -> {
			if (value.toString().contains(".")) return Formatters.firstLowerCase(value.toString());
			else return value;
		});
	}

}
