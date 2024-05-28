package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class PassiveViewRequesterTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display")).output(placeholder("import")).output(literal("\n\nexport default class ")).output(placeholder("name", "firstUpperCase")).output(placeholder("proxy")).output(literal("Requester extends ")).output(placeholder("parentType")).output(literal(" {\n\tconstructor(element) {\n\t\tsuper(element);\n\t};\n\t")).output(expression().output(placeholder("request").multiple("\n"))).output(literal("\n\tdidMount = () => {\n\t\tthis.pushService.send({ op: \"didMount\", s: \"")).output(placeholder("name", "lowerCase")).output(literal("\", d: this.element.shortId(), o: this.element.props.owner(), c: this.element.props.context()}, this.element.ownerUnit());\n\t};\n}")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("import"))).output(literal("import ")).output(placeholder("parent", "firstUpperCase")).output(literal("Requester from \"./")).output(placeholder("parent", "firstUpperCase")).output(literal("Requester\"")));
		rules.add(rule().condition(all(attribute("accessible"), trigger("import"))).output(literal("import Requester from \"alexandria-ui-elements/gen/displays/requesters/ProxyDisplayRequester\";")));
		rules.add(rule().condition(trigger("import")).output(literal("import Requester from \"./Requester\";")));
		rules.add(rule().condition(all(attribute("accessible"), trigger("proxy"))).output(literal("Proxy")));
		rules.add(rule().condition(trigger("proxy")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("parenttype"))).output(placeholder("parent", "firstUpperCase")).output(literal("Requester")));
		rules.add(rule().condition(trigger("parenttype")).output(literal("Requester")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("request"))).output(placeholder("name")).output(literal(" = function(value) {\n\tif (this.addToHistory(value)) return;\n\t")).output(placeholder("method")).output(literal("({ app: this.element.context, op: \"")).output(placeholder("name")).output(literal("\", s: \"")).output(placeholder("display", "lowercase")).output(literal("\", d: this.element.shortId(), o: this.element.props.owner(), c: this.element.props.context(), v: ")).output(placeholder("parameter")).output(literal("}, this.element.ownerUnit());\n}")));
		rules.add(rule().condition(trigger("request")).output(placeholder("name")).output(literal(" = (")).output(expression().output(placeholder("parameterSignature"))).output(literal(") => {\n\tif (this.addToHistory(")).output(expression().output(placeholder("parameterSignature"))).output(literal(")) return;\n\t")).output(placeholder("method")).output(literal("({ app: this.element.context, op: \"")).output(placeholder("name")).output(literal("\", s: \"")).output(placeholder("display", "lowercase")).output(literal("\", d: this.element.shortId(), o: this.element.props.owner(), c: this.element.props.context()")).output(expression().output(literal(", v: ")).output(placeholder("parameter"))).output(literal("}, this.element.ownerUnit());\n};")));
		rules.add(rule().condition(all(allTypes("object"), trigger("parameter"))).output(literal("encodeURIComponent(JSON.stringify(value))")));
		rules.add(rule().condition(all(allTypes("list", "file"), trigger("parameter"))).output(literal("value")));
		rules.add(rule().condition(all(allTypes("list"), trigger("parameter"))).output(literal("encodeURIComponent(JSON.stringify(value))")));
		rules.add(rule().condition(all(allTypes("file"), trigger("parameter"))).output(literal("value")));
		rules.add(rule().condition(trigger("parameter")).output(literal("encodeURIComponent(value)")));
		rules.add(rule().condition(all(attribute("upload"), trigger("method"))).output(literal("this.fileService.upload")));
		rules.add(rule().condition(all(attribute("download"), trigger("method"))).output(literal("this.fileService.download")));
		rules.add(rule().condition(trigger("method")).output(literal("this.pushService.send")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}