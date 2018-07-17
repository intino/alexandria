package io.intino.konos.builder.codegeneration.schema;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.bool.BoolData;
import io.intino.konos.model.graph.date.DateData;
import io.intino.konos.model.graph.datetime.DateTimeData;
import io.intino.konos.model.graph.file.FileData;
import io.intino.konos.model.graph.integer.IntegerData;
import io.intino.konos.model.graph.longinteger.LongIntegerData;
import io.intino.konos.model.graph.real.RealData;
import io.intino.konos.model.graph.text.TextData;
import io.intino.konos.model.graph.type.TypeData;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class SchemaRenderer {
	private final List<Schema> schemas;
	private File gen;
	private String rootPackage;
	private final Map<String, String> classes;

	public SchemaRenderer(KonosGraph graph, File gen, String rootPackage, Map<String, String> classes) {
		schemas = graph.core$().find(Schema.class).stream().filter(s -> !s.core$().owner().is(Schema.class)).collect(Collectors.toList());
		this.gen = gen;
		this.rootPackage = rootPackage;
		this.classes = classes;
	}

	public void execute() {
		schemas.forEach(this::processSchema);
	}

	private void processSchema(Schema schema) {
		final Service service = schema.core$().ownerAs(Service.class);
		String subPackage = "schemas" + (service != null ? File.separator + service.name$().toLowerCase() : "");
		final File packageFolder = new File(gen, subPackage);
		final String packageName = subPackage.isEmpty() ? rootPackage : rootPackage + "." + subPackage.replace(File.separator, ".");
		final Frame frame = createSchemaFrame(schema, packageName);
		classes.put(Schema.class.getSimpleName() + "#" + schema.name$(), subPackage.replace(File.separator, ".") + "." + schema.name$());
		Commons.writeFrame(packageFolder, schema.name$(), template().format(new Frame("root").addSlot("root", rootPackage).addSlot("package", packageName).addSlot("schema", frame)));
	}

	public Frame createSchemaFrame(Schema schema, String packageName) {
		return createSchemaFrame(schema, packageName, new HashSet<>());
	}

	private Frame createSchemaFrame(Schema schema, String packageName, Set<Schema> processed) {
		Frame frame = new Frame("schema").addSlot("name", schema.name$()).addSlot("package", packageName);
		if (schema.core$().owner().is(Schema.class)) frame.addSlot("inner", "static");
		frame.addSlot("attribute", (AbstractFrame[]) processAttributes(schema.attributeList()));
		frame.addSlot("attribute", (AbstractFrame[]) processSchemasAsAttribute(schema.schemaList(), rootPackage));
		frame.addSlot("attribute", (AbstractFrame[]) processHasAsAttribute(schema.hasList(), rootPackage));
		if (schema.isExtensionOf()) {
			frame.addSlot("parent", schema.asExtensionOf().parent().name$());
		}
		if (schema.attributeMap() != null) frame.addSlot("attribute", render(schema.attributeMap()));
		addReturningValueToAttributes(schema.name$(), frame.frames("attribute"));
		final Frame[] innerSchemas = schema.schemaList().stream().filter(processed::add).map(s -> createSchemaFrame(s, packageName, processed)).toArray(Frame[]::new);
		if (innerSchemas.length > 0) frame.addSlot("schema", innerSchemas);
		return frame;
	}

	private static Frame[] processAttributes(List<Schema.Attribute> attributes) {
		return attributes.stream().map(SchemaRenderer::processAttribute).toArray(value -> new Frame[attributes.size()]);
	}

	private static Frame[] processSchemasAsAttribute(List<Schema> members, String rootPackage) {
		return members.stream().map(m -> processSchemaAsAttribute(m, rootPackage)).toArray(value -> new Frame[members.size()]);
	}

	private static Frame[] processHasAsAttribute(List<Schema.Has> members, String rootPackage) {
		return members.stream().map(m -> processHasAsAttribute(m, rootPackage)).toArray(value -> new Frame[members.size()]);
	}

	private static Frame processAttribute(Schema.Attribute attribute) {
		if (attribute.isReal()) return processAttribute(attribute.asReal());
		else if (attribute.isInteger()) return processAttribute(attribute.asInteger());
		else if (attribute.isBool()) return processAttribute(attribute.asBool());
		else if (attribute.isText()) return processAttribute(attribute.asText());
		else if (attribute.isDateTime()) return processAttribute(attribute.asDateTime());
		else if (attribute.isDate()) return processAttribute(attribute.asDate());
		else if (attribute.isFile()) return processAttribute(attribute.asFile());
		else if (attribute.isLongInteger()) return processAttribute(attribute.asLongInteger());
		return null;
	}

	private static Frame processAttribute(RealData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", "double")
				.addSlot("name", attribute.a$(Schema.Attribute.class).name$())
				.addSlot("type", "double")
				.addSlot("defaultValue", attribute.defaultValue());
	}

	private static Frame processAttribute(IntegerData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.a$(Schema.Attribute.class).name$())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", attribute.defaultValue());
	}

	private static Frame processAttribute(LongIntegerData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.a$(Schema.Attribute.class).name$())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", attribute.defaultValue() + "L");
	}


	private static Frame processAttribute(FileData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.a$(Schema.Attribute.class).name$())
				.addSlot("type", attribute.type());
	}

	private static Frame processAttribute(BoolData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.a$(Schema.Attribute.class).name$())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", attribute.defaultValue());
	}

	private static Frame processAttribute(TextData attribute) {
		return new Frame().addTypes(multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.a$(Schema.Attribute.class).name$())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", "\"" + attribute.defaultValue() + "\"");
	}

	private static Frame processAttribute(DateTimeData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.a$(Schema.Attribute.class).name$())
				.addSlot("type", attribute.type());
	}

	private static Frame processAttribute(DateData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.a$(Schema.Attribute.class).name$())
				.addSlot("type", attribute.type());
	}

	private static Frame processSchemaAsAttribute(Schema schema, String rootPackage) {
		return new Frame().addTypes(schema.multiple() ? "multiple" : "single", "member", schema.name$())
				.addSlot("name", schema.name$())
				.addSlot("type", schema.name$())
				.addSlot("list", "List")
				.addSlot("package", packageOf(schema, rootPackage));
	}

	private static Frame processHasAsAttribute(Schema.Has has, String rootPackage) {
		final boolean anonymous = has.name$().contains("-");
		return new Frame().addTypes(has.multiple() ? "multiple" : "single", "member", has.reference().name$())
				.addSlot("name", anonymous ? has.reference().name$() : has.name$())
				.addSlot("type", has.reference().name$())
				.addSlot("list", anonymous ? "List" : "")
				.addSlot("package", packageOf(has.reference(), rootPackage));
	}

	private static String packageOf(Schema schema, String rootPackage) {
		final Service service = schema.core$().ownerAs(Service.class);
		String subPackage = "schemas" + (service != null ? File.separator + service.name$().toLowerCase() : "");
		return subPackage.isEmpty() ? rootPackage : rootPackage + "." + subPackage.replace(File.separator, ".");
	}

	private static Frame render(Schema.AttributeMap map) {
		return new Frame().addTypes("attributeMap").addSlot("name", map.name$());
	}

	private static void addReturningValueToAttributes(String elementName, Iterator<AbstractFrame> attributes) {
		while (attributes.hasNext())
			((Frame) attributes.next()).addSlot("element", elementName);
	}

	private static boolean multiple(TypeData attribute) {
		return attribute.a$(Schema.Attribute.class).multiple();
	}

	private Template template() {
		Template template = Formatters.customize(SchemaTemplate.create());
		template.add("typeFormat", (value) -> {
			if (value.toString().contains(".")) return Formatters.firstLowerCase(value.toString());
			else return value;
		});
		return template;
	}

}
