package io.intino.konos.builder.codegeneration.schema;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.bool.BoolData;
import io.intino.konos.model.graph.date.DateData;
import io.intino.konos.model.graph.datetime.DateTimeData;
import io.intino.konos.model.graph.file.FileData;
import io.intino.konos.model.graph.integer.IntegerData;
import io.intino.konos.model.graph.list.ListData;
import io.intino.konos.model.graph.longinteger.LongIntegerData;
import io.intino.konos.model.graph.real.RealData;
import io.intino.konos.model.graph.text.TextData;
import io.intino.konos.model.graph.type.TypeData;
import io.intino.konos.model.graph.word.WordData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.addAll;

public class SchemaRenderer extends Renderer {
	private final Schema schema;
	private final File destination;
	private final String packageName;

	public SchemaRenderer(Settings settings, Schema schema, File destination, String packageName) {
		super(settings, Target.Service);
		this.schema = schema;
		this.destination = destination != null ? destination : gen();
		this.packageName = packageName != null ? packageName : settings.packageName();
	}

	public void render() {
		String rootPackage = packageName;
		String subPackage = subPackage(schema);
		final File packageFolder = schemaFolder(schema);
		final String packageName = subPackage.isEmpty() ? rootPackage : rootPackage + "." + subPackage.replace(File.separator, ".");
		final Frame frame = createSchemaFrame(schema, packageName);
		classes().put(Schema.class.getSimpleName() + "#" + schema.name$(), subPackage.replace(File.separator, ".") + "." + schema.name$());
		Commons.writeFrame(packageFolder, schema.name$(), template().render(new FrameBuilder("root").add("root", rootPackage).add("package", packageName).add("schema", frame)));
		saveRendered(schema);
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

	@NotNull
	private Frame[] components(Schema schema, String packageName, Set<Schema> processed) {
		return schema.schemaList().stream().filter(processed::add).map(s -> createSchemaFrame(s, packageName, processed)).toArray(Frame[]::new);
	}

	@NotNull
	private FrameBuilder[] collectAttributes(Schema schema) {
		List<FrameBuilder> attributes = new ArrayList<>();
		addAll(attributes, processAttributes(schema.attributeList()));
		addAll(attributes, processSchemasAsAttribute(schema.schemaList()));
		if (schema.attributeMap() != null) attributes.add(processAttributeMap(schema.attributeMap()));
		attributes.forEach(f -> f.add("element", schema.name$()));
		return attributes.toArray(new FrameBuilder[0]);
	}

	private FrameBuilder[] processAttributes(List<Schema.Attribute> attributes) {
		return attributes.stream().map(this::process).toArray(FrameBuilder[]::new);
	}

	private FrameBuilder[] processSchemasAsAttribute(List<Schema> schemas) {
		return schemas.stream().map(schema -> processSchemaAsAttribute(schema, schema.name$() + (schema.multiple() ? "List" : ""), schema.multiple())).toArray(FrameBuilder[]::new);
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
		else if (attribute.isObject())
			return processSchemaAsAttribute(attribute.asObject().schema(), attribute.name$(), attribute.isList());
		return null;
	}

	private FrameBuilder process(RealData attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", "double")
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", !multiple(attribute) ? "double" : attribute.type())
				.add("defaultValue", attribute.defaultValue());
	}

	private FrameBuilder process(IntegerData attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", !multiple(attribute) ? "int" : attribute.type())
				.add("defaultValue", attribute.defaultValue());
	}

	private FrameBuilder process(LongIntegerData attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type())
				.add("defaultValue", attribute.defaultValue() + "L");
	}

	private FrameBuilder process(FileData attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type());
	}

	private FrameBuilder processAttributeMap(Schema.AttributeMap map) {
		return new FrameBuilder("attributeMap").add("name", map.name$());
	}

	private FrameBuilder process(BoolData attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type())
				.add("defaultValue", attribute.defaultValue());
	}

	private FrameBuilder process(TextData attribute) {
		FrameBuilder builder = new FrameBuilder(multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type());
		if (attribute.defaultValue() != null) builder.add("defaultValue", "\"" + attribute.defaultValue() + "\"");
		return builder;

	}

	private FrameBuilder process(DateTimeData attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type());
	}

	private FrameBuilder process(DateData attribute) {
		return new FrameBuilder("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", attribute.a$(Schema.Attribute.class).name$())
				.add("type", attribute.type());
	}

	private FrameBuilder process(WordData attribute) {
		final Schema.Attribute a = attribute.a$(Schema.Attribute.class);
		return new FrameBuilder("word", multiple(attribute) ? "multiple" : "single", attribute.type())
				.add("name", a.name$())
				.add("words", attribute.values().toArray(new String[0]))
				.add("type", a.name$());
	}

	private FrameBuilder processSchemaAsAttribute(Schema schema, String name, boolean multiple) {
		return new FrameBuilder(multiple ? "multiple" : "single", "member", schema.name$())
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

	private boolean multiple(TypeData attribute) {
		return attribute.i$(ListData.class);
	}


	private Template template() {
		return Formatters.customize(new SchemaTemplate()).add("typeFormat", (value) -> {
			if (value.toString().contains(".")) return Formatters.firstLowerCase(value.toString());
			else return value;
		});
	}

}
