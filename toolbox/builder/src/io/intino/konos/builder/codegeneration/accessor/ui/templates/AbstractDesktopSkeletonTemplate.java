package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractDesktopSkeletonTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("desktop"))).output(literal("import React from \"react\";\nimport * as Ui from \"alexandria-ui-elements/gen/Displays\";\nimport * as Displays from \"../")).output(expression().output(mark("packageTypeRelativeDirectory"))).output(literal("Displays\";\nimport AlexandriaDesktop from \"alexandria-ui-elements/src/displays/Desktop\";")).output(expression().output(literal("\n")).output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier from \"../notifiers/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier\";"))).output(expression().output(literal("\n")).output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester from \"../requesters/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester\";"))).output(literal("\n\nexport default class ")).output(expression().output(mark("abstract"))).output(mark("name", "firstUpperCase")).output(literal(" extends AlexandriaDesktop {\n\n\tconstructor(props) {\n\t\tsuper(props);")).output(expression().output(literal("\n")).output(literal("this.notifier = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier(this);"))).output(expression().output(literal("\n")).output(literal("this.requester = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester(this);"))).output(literal("\n\t};\n\n\trender() {\n\t\treturn (\n\t\t\t<React.Fragment>\n\t\t\t\t<Ui.Header id=\"")).output(mark("headerId")).output(literal("\">\n\t\t\t\t\t")).output(mark("component").multiple("\n")).output(literal("\n\t\t\t\t</Ui.Header>\n\t\t\t\t<Ui.Tabs id=\"")).output(mark("tabBarId")).output(literal("\">\n\t\t\t\t\t")).output(mark("tabs").multiple("\n")).output(literal("\n\t\t\t\t</Ui.Tabs>\n\t\t\t</React.Fragment>\n\t\t);\n\t}\n}"))
		);
	}
}