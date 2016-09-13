package teseo.codegeneration.schema;

import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Schema;
import teseo.bool.BoolData;
import teseo.date.DateData;
import teseo.datetime.DateTimeData;
import teseo.integer.IntegerData;
import teseo.real.RealData;
import teseo.text.TextData;
import teseo.type.TypeData;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static teseo.helpers.Commons.writeFrame;

public class SchemaRenderer {
	private final List<Schema> schemaList;
	private File destination;
	private String packageName;

	public SchemaRenderer(Graph graph) {
		schemaList = graph.find(Schema.class);
	}

	public void execute(File destination, String packageName) {
		this.destination = destination;
		this.packageName = packageName;
		schemaList.forEach(this::processScheme);
	}

	private void processScheme(Schema schema) {
		schema.node().findNode(Schema.Element.class).forEach(this::processElement);
	}

	private void processElement(Schema.Element element) {
		Frame frame = new Frame().addTypes("element");
		frame.addSlot("name", element.name());
		frame.addSlot("package", packageName);
		frame.addSlot("attribute", (AbstractFrame[]) processAttributes(element.attributeList()));
		frame.addSlot("attribute", (AbstractFrame[]) processAsAttribute(element.memberList()));
		if (element.attributeMap() != null) frame.addSlot("attribute", attributeMap());
		addReturningElementToAttributes(element.name(), frame.frames("attribute"));
		writeFrame(new File(destination, "schemas"), element.name(), template().format(frame));
	}

	private Template template() {
		final Template template = SchemaTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private Frame[] processAttributes(List<Schema.Element.Attribute> attributes) {
		return attributes.stream().map(this::processAttribute).toArray(value -> new Frame[attributes.size()]);
	}

	private Frame[] processAsAttribute(List<Schema.Element.Member> members) {
		return members.stream().map(this::processAsAttribute).toArray(value -> new Frame[members.size()]);
	}

	private Frame processAttribute(Schema.Element.Attribute attribute) {
		if (attribute.isReal()) return processAttribute(attribute.asReal());
		else if (attribute.isInteger()) return processAttribute(attribute.asInteger());
		else if (attribute.isBool()) return processAttribute(attribute.asBool());
		else if (attribute.isText()) return processAttribute(attribute.asText());
		else if (attribute.isDateTime()) return processAttribute(attribute.asDateTime());
		else if (attribute.isDate()) return processAttribute(attribute.asDate());
		return null;
	}

	private Frame processAttribute(RealData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
				.addSlot("name", attribute.as(Schema.Element.Attribute.class).name())
				.addSlot("type", "double")
				.addSlot("defaultValue", attribute.defaultValue());
	}

	private Frame processAttribute(IntegerData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
				.addSlot("name", attribute.as(Schema.Element.Attribute.class).name())
				.addSlot("type", attribute.type())
				.addSlot("defaultValue", attribute.defaultValue());
	}

	private Frame processAttribute(BoolData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
				.addSlot("name", attribute.as(Schema.Element.Attribute.class).name()).addSlot("type", attribute.type()).addSlot("defaultValue", attribute.defaultValue());
	}

	private Frame processAttribute(TextData attribute) {
		return new Frame().addTypes(multiple(attribute) ? "multiple" : "single")
				.addSlot("name", attribute.as(Schema.Element.Attribute.class).name()).addSlot("type", attribute.type()).addSlot("defaultValue", "\"" + attribute.defaultValue() + "\"");
	}

	private Frame processAttribute(DateTimeData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
				.addSlot("name", attribute.as(Schema.Element.Attribute.class).name()).addSlot("type", attribute.type());
	}

	private Frame processAttribute(DateData attribute) {
		return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
				.addSlot("name", attribute.as(Schema.Element.Attribute.class).name()).addSlot("type", attribute.type());
	}

	private Frame processAsAttribute(Schema.Element.Member member) {
		return new Frame().addTypes(member.multiple() ? "multiple" : "single", "member")
				.addSlot("name", member.name())
				.addSlot("type", member.name());
	}

	private Frame attributeMap() {
		return new Frame().addTypes("attributeMap");
	}

	private void addReturningElementToAttributes(String elementName, Iterator<AbstractFrame> attribute) {
		while (attribute.hasNext())
			((Frame) attribute.next()).addSlot("element", elementName);
	}

	private boolean multiple(TypeData attribute) {
		return attribute.as(Schema.Element.Attribute.class).multiple();
	}

}
