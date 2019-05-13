package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractDisplaySkeletonTemplate extends Template {

	protected AbstractDisplaySkeletonTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractDisplaySkeletonTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display"))).add(literal("import React from \"react\";\nimport * as Ui from \"alexandria-ui-elements/gen/Displays\";\nimport * as Displays from \"../")).add(expression().add(mark("packageTypeRelativeDirectory"))).add(literal("Displays\";\n")).add(mark("accessorType", "import")).add(literal(";")).add(expression().add(literal("\n")).add(literal("import ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier from \"../notifiers/")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier\";"))).add(expression().add(literal("\n")).add(literal("import ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester from \"../requesters/")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester\";"))).add(literal("\n\nexport default class ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal(" extends ")).add(mark("displayExtends")).add(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);")).add(expression().add(literal("\n")).add(literal("\t\tthis.notifier = new ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier(this);"))).add(expression().add(literal("\n")).add(literal("\t\tthis.requester = new ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester(this);"))).add(literal("\n\t\t")).add(mark("properties", "initialization")).add(literal("\n\t};\n\t\n\trender() {\n\t\treturn ")).add(expression().add(literal("this.")).add(mark("baseMethod"))).add(literal("(\n\t\t\t")).add(mark("renderTag")).add(expression().add(literal("\n")).add(literal("\t\t\t\t")).add(mark("reference").multiple("\n")).add(literal("\n")).add(literal("\t\t\t\t")).add(mark("component").multiple("\n")).add(literal("\n")).add(literal("\t\t\t"))).add(mark("renderTag", "end")).add(literal("\n\t\t);\n\t}\n}")),
			rule().add((condition("type", "displayExtends & extensionOf"))).add(literal("Displays.")).add(mark("parent", "firstUpperCase")),
			rule().add((condition("type", "displayExtends"))).add(literal("Ui")).add(mark("type", "firstUpperCase")),
			rule().add((condition("type", "renderTag & template")), (condition("trigger", "end"))).add(literal("</Ui.Block>")),
			rule().add((condition("type", "renderTag & block")), (condition("trigger", "end"))).add(literal("</Ui.Block>")),
			rule().add((condition("type", "renderTag & item")), (condition("trigger", "end"))).add(literal("</div>")),
			rule().add((condition("type", "renderTag")), (condition("trigger", "end"))).add(literal("</React.Fragment>")),
			rule().add((condition("type", "renderTag & template"))).add(literal("<Ui.Block")).add(expression().add(mark("properties", "common")).add(mark("properties", "specific"))).add(literal(">")),
			rule().add((condition("type", "renderTag & block"))).add(literal("<Ui.Block")).add(expression().add(mark("properties", "common")).add(mark("properties", "specific"))).add(literal(">")),
			rule().add((condition("type", "renderTag & item"))).add(literal("<div style={this.props.style}>")),
			rule().add((condition("type", "renderTag"))).add(literal("<React.Fragment>")),
			rule().add((condition("type", "renderTagAttributes & block"))).add(mark("properties", "common")).add(mark("properties", "specific")),
			rule().add((condition("type", "renderTagAttributes"))),
			rule().add((condition("attribute", "baseDisplay")), (condition("trigger", "import"))).add(literal("import Ui")).add(mark("value", "firstUpperCase")).add(literal(" from \"alexandria-ui-elements/src/displays/Display\"")),
			rule().add((condition("attribute", "baseComponent")), (condition("trigger", "import"))).add(literal("import Ui")).add(mark("value", "firstUpperCase")).add(literal(" from \"alexandria-ui-elements/src/displays/components/Component\"")),
			rule().add((condition("attribute", "component")), (condition("trigger", "import"))).add(literal("import Ui")).add(mark("value", "firstUpperCase")).add(literal(" from \"alexandria-ui-elements/src/displays/components/")).add(mark("value", "firstUpperCase")).add(literal("\"")),
			rule().add((condition("trigger", "import"))).add(literal("import Ui")).add(mark("value", "firstUpperCase")).add(literal(" from \"alexandria-ui-elements/src/displays/")).add(mark("value", "firstUpperCase")).add(literal("\""))
		);
		return this;
	}
}