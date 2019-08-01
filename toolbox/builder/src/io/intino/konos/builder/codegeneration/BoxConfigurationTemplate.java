package io.intino.konos.builder.codegeneration;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class BoxConfigurationTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("boxConfiguration"))).output(literal("package ")).output(mark("package")).output(literal(";\n\nimport java.util.Map;\nimport java.util.HashMap;\n\npublic class ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Configuration extends ")).output(expression().output(mark("parent")).output(literal("Configuration")).next(expression().output(literal("io.intino.alexandria.core.BoxConfiguration")))).output(literal(" {\n\n\tpublic ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Configuration(String[] args) {\n\t\tsuper(args);\n\t}\n\n\t")).output(mark("parameter").multiple("\n\n")).output(literal("\n}")),
				rule().condition((trigger("parameter"))).output(literal("public String ")).output(mark("name", "camelCase", "firstLowerCase")).output(literal("() {\n\treturn get(\"")).output(mark("value")).output(literal("\");\n}")),
			rule().condition((type("custom")), (trigger("replace"))).output(literal(".replace(\"{")).output(mark("name")).output(literal("}\", ")).output(mark("name", "validname", "firstLowerCase")).output(literal(")")),
			rule().condition((type("custom")), (trigger("signature"))).output(mark("type")).output(literal(" ")).output(mark("name", "validname", "firstLowerCase")),
			rule().condition((type("custom")), (trigger("name"))).output(mark("name", "validname", "firstLowerCase")),
			rule().condition((type("custom")), (trigger("field"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "validname", "firstLowerCase")).output(literal(" = \"\";")),
			rule().condition((type("custom")), (trigger("assign"))).output(literal("this.")).output(mark("conf", "validname", "firstLowerCase")).output(literal("Configuration.")).output(mark("name", "validname", "firstLowerCase")).output(literal(" = ")).output(mark("name", "validname", "firstLowerCase")).output(literal(";")),
			rule().condition((type("custom")), (trigger("parameter"))).output(literal("args.get(\"")).output(mark("conf", "firstLowerCase")).output(literal("_")).output(mark("name", "validname", "firstLowerCase")).output(literal("\")")),
			rule().condition((trigger("empty")))
		);
	}
}