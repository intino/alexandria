package io.intino.pandora.builder.codegeneration.schema;

import io.intino.pandora.builder.codegeneration.Formatters;
import io.intino.pandora.builder.helpers.Commons;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.bool.BoolData;
import io.intino.pandora.model.date.DateData;
import io.intino.pandora.model.datetime.DateTimeData;
import io.intino.pandora.model.integer.IntegerData;
import io.intino.pandora.model.longinteger.LongIntegerData;
import io.intino.pandora.model.real.RealData;
import io.intino.pandora.model.text.TextData;
import io.intino.pandora.model.type.TypeData;
import io.intino.tara.magritte.Graph;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class SchemaRenderer {
	private final List<Schema> schemas;
	private File destination;
	private String packageName;

	public SchemaRenderer(Graph graph, File destination, String packageName) {
		schemas = graph.find(Schema.class);
		this.destination = destination;
		this.packageName = packageName;
	}

	public void execute() {
		schemas.forEach(this::processSchemas);
	}

	private void processSchemas(Schema format) {
		format.node().findNode(Schema.class).forEach(this::processSchema);
	}

	private void processSchema(Schema element) {
		Commons.writeFrame(new File(destination, "schemas"), element.name(), template().format(createSchemaFrame(element, packageName)));
	}

	@NotNull
	public static Frame createSchemaFrame(Schema schema, String packageName) {
		Frame frame = new Frame().addTypes("schema").addSlot("name", schema.name()).addSlot("package", packageName);
		frame.addSlot("attribute", (AbstractFrame[]) processAttributes(schema.attributeList()));
		frame.addSlot("attribute", (AbstractFrame[]) processSchemasAsAttribute(schema.schemaList()));
		frame.addSlot("attribute", (AbstractFrame[]) processHasAsAttribute(schema.hasList()));
		if (schema.attributeMap() != null) frame.addSlot("attribute", render(schema.attributeMap()));
		addReturningValueToAttributes(schema.name(), frame.frames("attribute"));
		return frame;
	}

	private static Frame[] processAttributes(List<Schema.Attribute> attributes) {
		return attributes.stream().map(SchemaRenderer::processAttribute).toArray(value -> new Frame[attributes.size()]);
	}

	private static Frame[] processSchemasAsAttribute(List<Schema> members) {
		return members.stream().map(SchemaRenderer::processSchemaAsAttribute).toArray(value -> new Frame[members.size()]);
	}

	private static Frame[] processHasAsAttribute(List<Schema.Has> members) {
		return members.stream().map(SchemaRenderer::processHasAsAttribute).toArray(value -> new Frame[members.size()]);
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

	private static Frame processSchemaAsAttribute(Schema format) {
		return new Frame().addTypes(format.multiple() ? "multiple" : "single", "member", format.name())
				.addSlot("name", format.name())
				.addSlot("type", format.name());
	}

	private static Frame processHasAsAttribute(Schema.Has has) {
		return new Frame().addTypes(has.multiple() ? "multiple" : "single", "member", has.reference().name())
				.addSlot("name", has.reference().name())
				.addSlot("type", has.reference().name());
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
