package io.intino.pandora.plugin.codegeneration.format;

import io.intino.pandora.plugin.Format;
import io.intino.pandora.plugin.bool.BoolData;
import io.intino.pandora.plugin.date.DateData;
import io.intino.pandora.plugin.datetime.DateTimeData;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.integer.IntegerData;
import io.intino.pandora.plugin.real.RealData;
import io.intino.pandora.plugin.text.TextData;
import io.intino.pandora.plugin.type.TypeData;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class FormatRenderer {
    private final List<Format> formatList;
    private File destination;
    private String packageName;

    public FormatRenderer(Graph graph, File destination, String packageName) {
        formatList = graph.find(Format.class);
        this.destination = destination;
        this.packageName = packageName;
    }

    public void execute() {
        formatList.forEach(this::processFormats);
    }

    private void processFormats(Format format) {
        format.node().findNode(Format.class).forEach(this::processFormat);
    }

    private void processFormat(Format element) {
        Frame frame = new Frame().addTypes("format");
        frame.addSlot("name", element.name());
        frame.addSlot("package", packageName);
        frame.addSlot("attribute", (AbstractFrame[]) processAttributes(element.attributeList()));
        frame.addSlot("attribute", (AbstractFrame[]) processFormatsAsAttribute(element.formatList()));
        frame.addSlot("attribute", (AbstractFrame[]) processHasAsAttribute(element.hasList()));
        if (element.attributeMap() != null) frame.addSlot("attribute", attributeMap());
        addReturningValueToAttributes(element.name(), frame.frames("attribute"));
        writeFrame(new File(destination, "formats"), element.name(), template().format(frame));
    }

    private Template template() {
        final Template template = FormatTemplate.create();
        template.add("ValidPackage", Commons::validPackage);
        return template;
    }

    private Frame[] processAttributes(List<Format.Attribute> attributes) {
        return attributes.stream().map(this::processAttribute).toArray(value -> new Frame[attributes.size()]);
    }

    private Frame[] processFormatsAsAttribute(List<Format> members) {
        return members.stream().map(this::processFormatAsAttribute).toArray(value -> new Frame[members.size()]);
    }

    private Frame[] processHasAsAttribute(List<Format.Has> members) {
        return members.stream().map(this::processHasAsAttribute).toArray(value -> new Frame[members.size()]);
    }

    private Frame processAttribute(Format.Attribute attribute) {
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
                .addSlot("name", attribute.as(Format.Attribute.class).name())
                .addSlot("type", "double")
                .addSlot("defaultValue", attribute.defaultValue());
    }

    private Frame processAttribute(IntegerData attribute) {
        return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
                .addSlot("name", attribute.as(Format.Attribute.class).name())
                .addSlot("type", attribute.type())
                .addSlot("defaultValue", attribute.defaultValue());
    }

    private Frame processAttribute(BoolData attribute) {
        return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
                .addSlot("name", attribute.as(Format.Attribute.class).name()).addSlot("type", attribute.type()).addSlot("defaultValue", attribute.defaultValue());
    }

    private Frame processAttribute(TextData attribute) {
        return new Frame().addTypes(multiple(attribute) ? "multiple" : "single")
                .addSlot("name", attribute.as(Format.Attribute.class).name()).addSlot("type", attribute.type()).addSlot("defaultValue", "\"" + attribute.defaultValue() + "\"");
    }

    private Frame processAttribute(DateTimeData attribute) {
        return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
                .addSlot("name", attribute.as(Format.Attribute.class).name()).addSlot("type", attribute.type());
    }

    private Frame processAttribute(DateData attribute) {
        return new Frame().addTypes("primitive", multiple(attribute) ? "multiple" : "single")
                .addSlot("name", attribute.as(Format.Attribute.class).name()).addSlot("type", attribute.type());
    }

    private Frame processFormatAsAttribute(Format format) {
        return new Frame().addTypes(format.multiple() ? "multiple" : "single", "member")
                .addSlot("name", format.name())
                .addSlot("type", format.name());
    }

    private Frame processHasAsAttribute(Format.Has has) {
        return new Frame().addTypes(has.multiple() ? "multiple" : "single", "member")
                .addSlot("name", has.reference().name())
                .addSlot("type", has.reference().name());
    }

    private Frame attributeMap() {
        return new Frame().addTypes("attributeMap");
    }

    private void addReturningValueToAttributes(String elementName, Iterator<AbstractFrame> attribute) {
        while (attribute.hasNext())
            ((Frame) attribute.next()).addSlot("element", elementName);
    }

    private boolean multiple(TypeData attribute) {
        return attribute.as(Format.Attribute.class).multiple();
    }

}
