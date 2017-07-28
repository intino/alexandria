package io.intino.konos.builder.codegeneration.schema;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Schema;
import io.intino.konos.model.Service;
import io.intino.konos.model.bool.BoolData;
import io.intino.konos.model.date.DateData;
import io.intino.konos.model.datetime.DateTimeData;
import io.intino.konos.model.integer.IntegerData;
import io.intino.konos.model.longinteger.LongIntegerData;
import io.intino.konos.model.real.RealData;
import io.intino.konos.model.text.TextData;
import io.intino.konos.model.type.TypeData;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class SchemaRenderer {
	private final List<Schema> schemas;
	private File destination;
	private String rootPackage;

	public SchemaRenderer(Graph graph, File destination, String rootPackage) {
		schemas = graph.find(Schema.class);
		this.destination = destination;
		this.rootPackage = rootPackage;
	}

	public void execute() {
		schemas.forEach(this::processSchemas);
	}

	private void processSchemas(Schema format) {
		format.node().findNode(Schema.class).forEach(this::processSchema);
	}

	private void processSchema(Schema schema) {
		final Service service = schema.ownerAs(Service.class);
		String subPackage = "schemas" + (service != null ? File.separator + service.name().toLowerCase() : "");
		final File packageFolder = new File(destination, subPackage);
		Commons.writeFrame(packageFolder, schema.name(), template().format(createSchemaFrame(schema, subPackage.isEmpty() ? rootPackage : rootPackage + "." + subPackage.replace(File.separator, "."), rootPackage)));
	}

	public static Frame createSchemaFrame(Schema schema, String packageName, String rootPackage) {
		Frame frame = new Frame().addTypes("schema").addSlot("name", schema.name()).addSlot("package", packageName).addSlot("root", rootPackage);
		frame.addSlot("attribute", (AbstractFrame[]) processAttributes(schema.attributeList()));
		frame.addSlot("attribute", (AbstractFrame[]) processSchemasAsAttribute(schema.schemaList(), rootPackage));
		frame.addSlot("attribute", (AbstractFrame[]) processHasAsAttribute(schema.hasList(), rootPackage));
		if (schema.attributeMap() != null) frame.addSlot("attribute", render(schema.attributeMap()));
		addReturningValueToAttributes(schema.name(), frame.frames("attribute"));
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
		else if (attribute.isLongInteger()) return processAttribute(attribute.asLongInteger());
		return null;
	}

	private static Frame processAttribute(RealData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", "double")
				.addSlot("name", attribute.as(Schema.Attribute.class).name())
				.addSlot("type", "double")
				.addSlot("defaultValue", attribute.defaultValue());
	}

	private static Frame processAttribute(IntegerData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", attribute.defaultValue());
	}

	private static Frame processAttribute(LongIntegerData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", attribute.defaultValue() + "L");
	}

	private static Frame processAttribute(BoolData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", attribute.defaultValue());
	}

	private static Frame processAttribute(TextData attribute) {
		return new Frame().addTypes(multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", "\"" + attribute.defaultValue() + "\"");
	}

	private static Frame processAttribute(DateTimeData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name())
				.addSlot("type", attribute.type());
	}

	private static Frame processAttribute(DateData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name())
				.addSlot("type", attribute.type());
	}

	private static Frame processSchemaAsAttribute(Schema schema, String rootPackage) {
		return new Frame().addTypes(schema.multiple() ? "multiple" : "single", "member", schema.name())
				.addSlot("name", schema.name())
				.addSlot("type", schema.name())
				.addSlot("package", packageOf(schema, rootPackage));
	}

	private static Frame processHasAsAttribute(Schema.Has has, String rootPackage) {
		return new Frame().addTypes(has.multiple() ? "multiple" : "single", "member", has.reference().name())
				.addSlot("name", has.reference().name())
				.addSlot("type", has.reference().name())
				.addSlot("package", packageOf(has.reference(), rootPackage));
	}

	private static String packageOf(Schema schema, String rootPackage) {
		final Service service = schema.ownerAs(Service.class);
		String subPackage = "schemas" + (service != null ? File.separator + service.name().toLowerCase() : "");
		return subPackage.isEmpty() ? rootPackage : rootPackage + "." + subPackage.replace(File.separator, ".");
	}

	private static Frame render(Schema.AttributeMap map) {
		return new Frame().addTypes("attributeMap").addSlot("name", map.name());
	}

	private static void addReturningValueToAttributes(String elementName, Iterator<AbstractFrame> attributes) {
		while (attributes.hasNext())
			((Frame) attributes.next()).addSlot("element", elementName);
	}

	private static boolean multiple(TypeData attribute) {
		return attribute.as(Schema.Attribute.class).multiple();
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
