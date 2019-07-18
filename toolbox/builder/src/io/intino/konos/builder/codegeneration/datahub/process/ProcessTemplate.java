package io.intino.konos.builder.codegeneration.datahub.process;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ProcessTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("process"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".procedures.")).output(mark("procedure", "lowercase")).output(literal(";\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.message.Message;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal("Process {\n\n\tpublic java.util.List<String> outputs;\n\tpublic ")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box;\n\tpublic ")).output(mark("type", "typeClass")).output(literal(" ")).output(mark("type", "typeName")).output(literal(";\n\n\tpublic void execute() {\n\t}\n}")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;")),
			rule().condition((attribute("message")), (trigger("typeclass"))).output(literal("io.intino.alexandria.message.Message")),
			rule().condition((type("schema")), (trigger("typeclass"))).output(mark("package")).output(literal(".schemas.")).output(mark("name", "FirstUpperCase")),
			rule().condition((type("schema")), (trigger("typename"))).output(mark("name", "firstLowerCase")),
			rule().condition((trigger("typename"))).output(literal("message"))
		);
	}
}