package io.intino.konos.builder.codegeneration;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class BoxConfigurationTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("boxConfiguration")).output(literal("package ")).output(placeholder("package")).output(literal(";\n\nimport java.util.Map;\nimport java.util.HashMap;\nimport java.io.File;\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Configuration extends ")).output(expression().output(placeholder("parent")).output(literal("Configuration")).next(expression().output(literal("io.intino.alexandria.core.BoxConfiguration")))).output(literal(" {\n\n\tpublic ")).output(placeholder("name", "PascalCase")).output(literal("Configuration(String[] args) {\n\t\tsuper(args);\n\t}\n\n\t")).output(placeholder("parameter").multiple("\n\n")).output(literal("\n\n\tpublic java.io.File home() {\n\t\treturn new java.io.File(args.getOrDefault(\"home\", System.getProperty(\"user.home\")));\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("file"), trigger("parameter"))).output(literal("public File ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn get(\"")).output(placeholder("value")).output(literal("\") == null ? null : new File(get(\"")).output(placeholder("value")).output(literal("\"));\n}")));
		rules.add(rule().condition(trigger("parameter")).output(literal("public String ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn get(\"")).output(placeholder("value")).output(literal("\");\n}")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("replace"))).output(literal(".replace(\"{")).output(placeholder("name")).output(literal("}\", ")).output(placeholder("name", "validname", "firstLowerCase")).output(literal(")")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("signature"))).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "validname", "firstLowerCase")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("name"))).output(placeholder("name", "validname", "firstLowerCase")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("field"))).output(literal("public ")).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "validname", "firstLowerCase")).output(literal(" = \"\";")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("assign"))).output(literal("this.")).output(placeholder("conf", "validname", "firstLowerCase")).output(literal("Configuration.")).output(placeholder("name", "validname", "firstLowerCase")).output(literal(" = ")).output(placeholder("name", "validname", "firstLowerCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("parameter"))).output(literal("args.get(\"")).output(placeholder("conf", "firstLowerCase")).output(literal("_")).output(placeholder("name", "validname", "firstLowerCase")).output(literal("\")")));
		rules.add(rule().condition(trigger("empty")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}