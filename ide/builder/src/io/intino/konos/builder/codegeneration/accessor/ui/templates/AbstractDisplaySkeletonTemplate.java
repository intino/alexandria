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
			rule().add((condition("type", "display"))).add(literal("import React from \"react\";\nimport * as Alexandria from \"../../../lib/alexandria-ui-elements/Alexandria\";\n")).add(mark("accessorType", "import")).add(literal(";")).add(expression().add(literal("\n")).add(literal("import ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier from \"../notifiers/")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier\";"))).add(expression().add(literal("\n")).add(literal("import ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester from \"../requesters/")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester\";"))).add(literal("\n\nexport default class ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal(" extends Alexandria")).add(mark("type", "firstUpperCase")).add(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);")).add(expression().add(literal("\n")).add(literal("\t\tthis.notifier = new ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Notifier(this);"))).add(expression().add(literal("\n")).add(literal("\t\tthis.requester = new ")).add(mark("notDecorated", "firstUpperCase")).add(literal("Requester(this);"))).add(literal("\n\t};\n\n\trender() {\n\t\treturn (\n\t\t\t<React.Fragment>\n\t\t\t\t")).add(mark("component").multiple("\n")).add(literal("\n\t\t\t</React.Fragment>\n\t\t);\n\t}\n}")),
			rule().add((condition("attribute", "component")), (condition("trigger", "import"))).add(literal("import Alexandria")).add(mark("value", "firstUpperCase")).add(literal(" from \"../../../lib/alexandria-ui-elements/src/displays/components/")).add(mark("value", "firstUpperCase")).add(literal("\"")),
			rule().add((condition("trigger", "import"))).add(literal("import Alexandria")).add(mark("value", "firstUpperCase")).add(literal(" from \"../../../lib/alexandria-ui-elements/src/displays/")).add(mark("value", "firstUpperCase")).add(literal("\""))
		);
		return this;
	}
}