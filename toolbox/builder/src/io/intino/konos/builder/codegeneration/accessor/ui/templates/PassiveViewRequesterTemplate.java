package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class PassiveViewRequesterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("display"))).output(mark("import")).output(literal("\n\nexport default class ")).output(mark("name", "firstUpperCase")).output(mark("proxy")).output(literal("Requester extends ")).output(mark("parentType")).output(literal(" {\n\tconstructor(element) {\n\t\tsuper(element);\n\t};\n\t")).output(expression().output(mark("request").multiple("\n"))).output(literal("\n}")),
			rule().condition((attribute("extensionof")), (trigger("import"))).output(literal("import ")).output(mark("parent", "firstUpperCase")).output(literal("Requester from \"./")).output(mark("parent", "firstUpperCase")).output(literal("Requester\"")),
			rule().condition((attribute("accessible")), (trigger("import"))).output(literal("import Requester from \"alexandria-ui-elements/gen/displays/requesters/ProxyDisplayRequester\";")),
			rule().condition((trigger("import"))).output(literal("import Requester from \"./Requester\";")),
			rule().condition((attribute("accessible")), (trigger("proxy"))).output(literal("Proxy")),
			rule().condition((trigger("proxy"))),
			rule().condition((attribute("extensionof")), (trigger("parenttype"))).output(mark("parent", "firstUpperCase")).output(literal("Requester")),
			rule().condition((trigger("parenttype"))).output(literal("Requester")),
			rule().condition((type("parameter")), (trigger("request"))).output(mark("name")).output(literal(" = function(value) {\n    if (this.addToHistory(value)) return;\n\t")).output(mark("method")).output(literal("({ op: \"")).output(mark("name")).output(literal("\", s: \"")).output(mark("display", "lowercase")).output(literal("\", d: this.element.props.id, o: this.element.props.owner(), c: this.element.props.context(), v: ")).output(mark("parameter")).output(literal("}, this.element.ownerUnit());\n}")),
			rule().condition((trigger("request"))).output(mark("name")).output(literal(" = (")).output(expression().output(mark("parameterSignature"))).output(literal(") => {\n    if (this.addToHistory(")).output(expression().output(mark("parameterSignature"))).output(literal(")) return;\n    ")).output(mark("method")).output(literal("({ op: \"")).output(mark("name")).output(literal("\", s: \"")).output(mark("display", "lowercase")).output(literal("\", d: this.element.props.id, o: this.element.props.owner(), c: this.element.props.context()")).output(expression().output(literal(", v: ")).output(mark("parameter"))).output(literal("}, this.element.ownerUnit());\n};")),
			rule().condition((type("object")), (trigger("parameter"))).output(literal("JSON.stringify(value)")),
			rule().condition((type("list")), (trigger("parameter"))).output(literal("JSON.stringify(value)")),
			rule().condition((trigger("parameter"))).output(literal("value")),
			rule().condition((attribute("upload")), (trigger("method"))).output(literal("this.fileService.upload")),
			rule().condition((attribute("download")), (trigger("method"))).output(literal("this.fileService.download")),
			rule().condition((trigger("method"))).output(literal("this.pushService.send"))
		);
	}
}