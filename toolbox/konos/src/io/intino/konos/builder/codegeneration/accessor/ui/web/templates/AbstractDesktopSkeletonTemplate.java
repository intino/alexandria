package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AbstractDesktopSkeletonTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("desktop")).output(literal("import React from \"react\";\nimport { Header as UiHeader, Tabs as UiTabs } from \"alexandria-ui-elements/gen/Displays\";\n")).output(expression().output(placeholder("alexandriaComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(placeholder("projectComponentImport").multiple("\n"))).output(literal("\nimport AlexandriaDesktop from \"alexandria-ui-elements/src/displays/Desktop\";")).output(expression().output(literal("\n")).output(literal("import ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier from \"../notifiers/")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier\";"))).output(expression().output(literal("\n")).output(literal("import ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester from \"../requesters/")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester\";"))).output(literal("\n\nexport default class ")).output(expression().output(placeholder("abstract"))).output(placeholder("name", "firstUpperCase")).output(literal(" extends AlexandriaDesktop {\n\n\tconstructor(props) {\n\t\tsuper(props);")).output(expression().output(literal("\n")).output(literal("this.notifier = new ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier(this);"))).output(expression().output(literal("\n")).output(literal("this.requester = new ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester(this);"))).output(literal("\n\t};\n\n\trender() {\n\t\treturn (\n\t\t\t<React.Fragment>\n\t\t\t\t<UiHeader id=\"")).output(placeholder("headerId")).output(literal("\">\n\t\t\t\t\t")).output(placeholder("component").multiple("\n")).output(literal("\n\t\t\t\t</UiHeader>\n\t\t\t\t<UiTabs id=\"")).output(placeholder("tabBarId")).output(literal("\">\n\t\t\t\t\t")).output(placeholder("tabs").multiple("\n")).output(literal("\n\t\t\t\t</UiTabs>\n\t\t\t</React.Fragment>\n\t\t);\n\t}\n}")));
		rules.add(rule().condition(allTypes("alexandriaComponentImport")).output(literal("import { ")).output(placeholder("type", "firstUpperCase")).output(literal(" as Ui")).output(placeholder("type", "firstUpperCase")).output(literal(" } from \"alexandria-ui-elements/gen/Displays\";")));
		rules.add(rule().condition(allTypes("projectComponentImport")).output(literal("import { ")).output(placeholder("type", "firstUpperCase")).output(literal(" as Displays")).output(placeholder("type", "firstUpperCase")).output(literal(" } from \"../")).output(expression().output(placeholder("packageTypeRelativeDirectory"))).output(literal("Displays\";")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}