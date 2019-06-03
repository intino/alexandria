package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractDisplaySkeletonTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("display"))).output(literal("import React from \"react\";\n")).output(expression().output(mark("alexandriaBlockImport"))).output(literal("\n")).output(expression().output(mark("alexandriaComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(mark("projectComponentImport").multiple("\n"))).output(literal("\n")).output(mark("accessorType", "import")).output(literal(";\n")).output(expression().output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier from \"../notifiers/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier\";"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester from \"../requesters/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester\";"))).output(literal("\n\nexport default class ")).output(mark("abstract")).output(mark("name", "firstUpperCase")).output(literal(" extends ")).output(mark("displayExtends")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\t")).output(expression().output(literal("this.notifier = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier(this);"))).output(literal("\n\t\t")).output(expression().output(literal("this.requester = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester(this);"))).output(literal("\n\t\t")).output(expression().output(mark("properties", "initialization"))).output(literal("\n\t};\n\t\n\trender() {\n\t\treturn ")).output(expression().output(literal("this.")).output(mark("baseMethod"))).output(literal("(\n\t\t\t")).output(expression().output(mark("renderTag"))).output(literal("\n\t\t\t\t")).output(expression().output(mark("reference").multiple("\n"))).output(literal("\n\t\t\t\t")).output(expression().output(mark("component").multiple("\n"))).output(literal("\n\t\t\t")).output(expression().output(mark("renderTag", "end"))).output(literal("\n\t\t);\n\t}\n}")),
			rule().condition((type("alexandriablockimport"))).output(literal("import { Block as UiBlock } from \"alexandria-ui-elements/src/displays/components/Block\";")),
			rule().condition((type("alexandriacomponentimport"))).output(literal("import { ")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal(" as Ui")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal(" } from \"alexandria-ui-elements/src/displays/")).output(expression().output(mark("componentDirectory")).output(literal("/"))).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("\";")),
			rule().condition((type("projectcomponentimport"))).output(literal("import { ")).output(mark("name", "firstUpperCase")).output(literal(" as Displays")).output(mark("name", "firstUpperCase")).output(literal(" } from \"app-elements/src/displays/")).output(expression().output(mark("componentDirectory")).output(literal("/"))).output(mark("name", "firstUpperCase")).output(literal("\";")),
			rule().condition((allTypes("displayextends","generic","isextensionof"))).output(literal("Displays")).output(mark("parent")),
			rule().condition((allTypes("displayextends","generic"))).output(literal("Ui")).output(mark("parent")),
			rule().condition((type("displayextends"))).output(literal("Ui")).output(mark("type", "firstUpperCase")),
			rule().condition((allTypes("template","rendertag")), (trigger("end"))).output(literal("</UiBlock>")),
			rule().condition((allTypes("rendertag","block")), (trigger("end"))).output(literal("</UiBlock>")),
			rule().condition((allTypes("item","rendertag")), (trigger("end"))).output(literal("</div>")),
			rule().condition((type("rendertag")), (trigger("end"))).output(literal("</React.Fragment>")),
			rule().condition((allTypes("template","rendertag"))).output(literal("<UiBlock")).output(expression().output(mark("properties", "common")).output(mark("properties", "specific"))).output(literal(">")),
			rule().condition((allTypes("rendertag","block"))).output(literal("<UiBlock")).output(expression().output(mark("properties", "common")).output(mark("properties", "specific"))).output(literal(">")),
			rule().condition((allTypes("item","rendertag"))).output(literal("<div style={this.props.style}>")),
			rule().condition((type("rendertag"))).output(literal("<React.Fragment>")),
			rule().condition((allTypes("rendertagattributes","block"))).output(mark("properties", "common")).output(mark("properties", "specific")),
			rule().condition((type("rendertagattributes"))),
			rule().condition((attribute("basedisplay")), (trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/Display\"")),
			rule().condition((attribute("basecomponent")), (trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/components/Component\"")),
			rule().condition((attribute("component")), (trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/components/")).output(mark("value", "firstUpperCase")).output(literal("\"")),
			rule().condition((trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/")).output(mark("value", "firstUpperCase")).output(literal("\""))
		);
	}
}