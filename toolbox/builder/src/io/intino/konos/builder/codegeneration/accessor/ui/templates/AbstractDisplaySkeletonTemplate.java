package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractDisplaySkeletonTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("display"))).output(literal("import React from \"react\";\nimport * as Ui from \"alexandria-ui-elements/gen/Displays\";\nimport * as Displays from \"../")).output(expression().output(mark("packageTypeRelativeDirectory"))).output(literal("Displays\";\n")).output(mark("accessorType", "import")).output(literal(";\n")).output(expression().output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier from \"../notifiers/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier\";"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester from \"../requesters/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester\";"))).output(literal("\n\nexport default class ")).output(mark("abstract")).output(mark("name", "firstUpperCase")).output(literal(" extends ")).output(mark("displayExtends")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\t")).output(expression().output(literal("this.notifier = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier(this);"))).output(literal("\n\t\t")).output(expression().output(literal("this.requester = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester(this);"))).output(literal("\n\t\t")).output(expression().output(mark("properties", "initialization"))).output(literal("\n\t};\n\t\n\trender() {\n\t\treturn ")).output(expression().output(literal("this.")).output(mark("baseMethod"))).output(literal("(\n\t\t\t")).output(expression().output(mark("renderTag"))).output(literal("\n\t\t\t\t")).output(expression().output(mark("reference").multiple("\n"))).output(literal("\n\t\t\t\t")).output(expression().output(mark("component").multiple("\n"))).output(literal("\n\t\t\t")).output(expression().output(mark("renderTag", "end"))).output(literal("\n\t\t);\n\t}\n}")),
			rule().condition((allTypes("extensionof","displayextends"))).output(literal("Displays.")).output(mark("parent", "firstUpperCase")),
			rule().condition((type("displayextends"))).output(literal("Ui")).output(mark("type", "firstUpperCase")),
			rule().condition((allTypes("template","rendertag")), (trigger("end"))).output(literal("</Ui.Block>")),
			rule().condition((allTypes("rendertag","block")), (trigger("end"))).output(literal("</Ui.Block>")),
			rule().condition((allTypes("item","rendertag")), (trigger("end"))).output(literal("</div>")),
			rule().condition((type("rendertag")), (trigger("end"))).output(literal("</React.Fragment>")),
			rule().condition((allTypes("template","rendertag"))).output(literal("<Ui.Block")).output(expression().output(mark("properties", "common")).output(mark("properties", "specific"))).output(literal(">")),
			rule().condition((allTypes("rendertag","block"))).output(literal("<Ui.Block")).output(expression().output(mark("properties", "common")).output(mark("properties", "specific"))).output(literal(">")),
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