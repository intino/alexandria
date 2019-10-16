package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractDisplaySkeletonTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("display","accessible"))).output(literal("import React from \"react\";\n")).output(expression().output(mark("alexandriaBlockImport"))).output(literal("\n")).output(expression().output(mark("alexandriaStampImport"))).output(literal("\n")).output(expression().output(mark("alexandriaFrameImport"))).output(literal("\n")).output(expression().output(mark("alexandriaComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(mark("projectComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(mark("parent", "import"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier from \"")).output(mark("notifierDirectory")).output(literal("/notifiers/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier\";"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester from \"")).output(mark("requesterDirectory")).output(literal("/requesters/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester\";"))).output(literal("\n")).output(expression().output(mark("displayRegistration", "import"))).output(literal("\nimport ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal(" from '../../../src/displays/")).output(expression().output(mark("componentDirectory")).output(literal("/"))).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("';\n\nexport default class ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Proxy extends ")).output(mark("displayExtends")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\t")).output(expression().output(literal("this.notifier = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier(this);"))).output(literal("\n\t\t")).output(expression().output(literal("this.requester = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester(this);"))).output(literal("\n\t\t")).output(expression().output(mark("properties", "initialization"))).output(literal("\n\t};\n\n}\n\n")).output(expression().output(mark("displayRegistration", "declaration"))),
			rule().condition((type("display"))).output(literal("import React from \"react\";\n")).output(expression().output(mark("alexandriaBlockImport"))).output(literal("\n")).output(expression().output(mark("alexandriaStampImport"))).output(literal("\n")).output(expression().output(mark("alexandriaFrameImport"))).output(literal("\n")).output(expression().output(mark("alexandriaComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(mark("projectComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(mark("parent", "import"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier from \"")).output(mark("notifierDirectory")).output(literal("/notifiers/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier\";"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester from \"")).output(mark("requesterDirectory")).output(literal("/requesters/")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester\";"))).output(literal("\n")).output(expression().output(mark("displayRegistration", "import"))).output(literal("\n\nexport default class ")).output(mark("abstract")).output(mark("name", "firstUpperCase")).output(literal(" extends ")).output(mark("displayExtends")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\t")).output(expression().output(literal("this.notifier = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Notifier(this);"))).output(literal("\n\t\t")).output(expression().output(literal("this.requester = new ")).output(mark("notDecorated", "firstUpperCase")).output(literal("Requester(this);"))).output(literal("\n\t\t")).output(expression().output(mark("properties", "initialization"))).output(literal("\n\t};\n\n\trender() {\n\t    const display = !this.state.visible ? {display:'none'} : undefined;\n\t\treturn ")).output(expression().output(literal("this.")).output(mark("baseMethod"))).output(literal("(\n\t\t\t")).output(expression().output(mark("renderTag"))).output(literal("\n\t\t\t\t")).output(expression().output(mark("reference").multiple("\n"))).output(literal("\n\t\t\t\t")).output(expression().output(mark("component").multiple("\n"))).output(literal("\n\t\t\t")).output(expression().output(mark("renderTag", "end"))).output(literal("\n\t\t);\n\t}\n}\n\n")).output(expression().output(mark("displayRegistration", "declaration"))),
			rule().condition((type("displayRegistration")), (trigger("import"))).output(literal("import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';")),
			rule().condition((allTypes("displayRegistration","accessible")), (trigger("declaration"))).output(literal("DisplayFactory.register(\"")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Proxy\", ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Proxy);")),
			rule().condition((type("displayRegistration")), (trigger("declaration"))).output(literal("DisplayFactory.register(\"")).output(mark("name", "firstUpperCase")).output(literal("\", ")).output(mark("name", "firstUpperCase")).output(literal(");")),
			rule().condition((type("alexandriaImport"))).output(literal("import Ui")).output(mark("name", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/components/")).output(mark("name", "firstUpperCase")).output(literal("\";")),
			rule().condition((type("alexandriaComponentImport"))).output(literal("import Ui")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal(" from \"alexandria-ui-elements/src/displays/")).output(expression().output(mark("componentDirectory")).output(literal("/"))).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("\";")),
			rule().condition((type("projectComponentImport"))).output(literal("import Displays")).output(mark("name", "firstUpperCase")).output(literal(" from \"app-elements/")).output(expression().output(mark("directory"))).output(literal("/displays/")).output(expression().output(mark("componentDirectory")).output(literal("/"))).output(mark("name", "firstUpperCase")).output(literal("\";")),
			rule().condition((allTypes("displayExtends","generic","isExtensionOf"))).output(literal("Displays")).output(mark("parent")),
			rule().condition((allTypes("displayExtends","generic"))).output(literal("Ui")).output(mark("parent")),
			rule().condition((type("displayExtends"))).output(literal("Ui")).output(mark("type", "firstUpperCase")),
			rule().condition((allTypes("renderTag","template")), (trigger("end"))).output(literal("</UiBlock>")),
			rule().condition((allTypes("renderTag","block")), (trigger("end"))).output(literal("</UiBlock>")),
			rule().condition((allTypes("renderTag","item")), (trigger("end"))).output(literal("</div>")),
			rule().condition((type("renderTag")), (trigger("end"))).output(literal("</React.Fragment>")),
			rule().condition((allTypes("renderTag","template"))).output(literal("<UiBlock")).output(expression().output(mark("properties", "common")).output(mark("properties", "specific"))).output(literal(" style={{...this.props.style,...display}}>")),
			rule().condition((allTypes("renderTag","block"))).output(literal("<UiBlock")).output(expression().output(mark("properties", "common")).output(mark("properties", "specific"))).output(literal(" style={{...this.props.style,...display}}>")),
			rule().condition((allTypes("renderTag","item"))).output(literal("<div style={{width:\"100%\",height:\"100%\",...this.props.style,...this.style(),...display}} className=\"layout vertical center-justified\">")),
			rule().condition((type("renderTag"))).output(literal("<React.Fragment>")),
			rule().condition((allTypes("renderTagAttributes","block"))).output(mark("properties", "common")).output(mark("properties", "specific")),
			rule().condition((type("renderTagAttributes"))),
			rule().condition((attribute("parent")), (trigger("import"))).output(literal("import Displays")).output(mark("parent", "firstUpperCase")).output(literal(" from \"../../../src/displays/")).output(mark("parentDirectory")).output(literal("/")).output(mark("parent", "firstUpperCase")).output(literal("\";")),
			rule().condition((attribute("accessible")), (trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/ProxyDisplay\";")),
			rule().condition((attribute("basedisplay")), (trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/Display\";")),
			rule().condition((attribute("basecomponent")), (trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/components/Component\";")),
			rule().condition((attribute("component")), (trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/components/")).output(mark("value", "firstUpperCase")).output(literal("\";")),
			rule().condition((trigger("import"))).output(literal("import Ui")).output(mark("value", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/")).output(mark("value", "firstUpperCase")).output(literal("\";"))
		);
	}
}