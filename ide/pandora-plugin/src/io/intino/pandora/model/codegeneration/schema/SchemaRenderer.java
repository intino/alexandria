package io.intino.pandora.model.codegeneration.schema;

import io.intino.pandora.model.Schema;
import io.intino.pandora.model.bool.BoolData;
import io.intino.pandora.model.date.DateData;
import io.intino.pandora.model.datetime.DateTimeData;
import io.intino.pandora.model.helpers.Commons;
import io.intino.pandora.model.integer.IntegerData;
import io.intino.pandora.model.real.RealData;
import io.intino.pandora.model.text.TextData;
import io.intino.pandora.model.type.TypeData;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static io.intino.pandora.model.codegeneration.server.rest.RESTResourceRenderer.firstLowerCase;
import static io.intino.pandora.model.helpers.Commons.writeFrame;

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
		writeFrame(new File(destination, "schemas"), element.name(), template().format(createSchemaFrame(element, packageName)));
	}

	@NotNull
	public static Frame createSchemaFrame(Schema element, String packageName) {
		Frame frame = new Frame().addTypes("schema").addSlot("name", element.name()).addSlot("package", packageName);
		frame.addSlot("attribute", (AbstractFrame[]) processAttributes(element.attributeList()));
		frame.addSlot("attribute", (AbstractFrame[]) processSchemasAsAttribute(element.schemaList()));
		frame.addSlot("attribute", (AbstractFrame[]) processHasAsAttribute(element.hasList()));
		if (element.attributeMap() != null) frame.addSlot("attribute", attributeMap());
		addReturningValueToAttributes(element.name(), frame.frames("attribute"));
		return frame;
	}

	private Template template() {
		final Template template = SchemaTemplate.create();
		template.add("ValidPackage", Commons::validPackage);
		template.add("typeFormat", (value) -> {
			if (value.toString().contains(".")) return firstLowerCase(value.toString());
			else return value;
		});
		return template;
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

	private static Frame processAttribute(BoolData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name()).addSlot("type", attribute.type()).addSlot("defaultValue", attribute.defaultValue());
	}

	private static Frame processAttribute(TextData attribute) {
		return new Frame().addTypes(multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name()).addSlot("type", attribute.type()).addSlot("defaultValue", "\"" + attribute.defaultValue() + "\"");
	}

	private static Frame processAttribute(DateTimeData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name()).addSlot("type", attribute.type());
	}

	private static Frame processAttribute(DateData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single", attribute.type())
				.addSlot("name", attribute.as(Schema.Attribute.class).name()).addSlot("type", attribute.type());
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

	private static Frame attributeMap() {
		return new Frame().addTypes("attributeMap");
	}

	private static void addReturningValueToAttributes(String elementName, Iterator<AbstractFrame> attribute) {
		while (attribute.hasNext())
			((Frame) attribute.next()).addSlot("element", elementName);
	}

	private static boolean multiple(TypeData attribute) {
		return attribute.as(Schema.Attribute.class).multiple();
	}

}
