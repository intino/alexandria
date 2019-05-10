package io.intino.konos.builder.codegeneration.datalake.mounter;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MounterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("mounter"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".datalake.mounters;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("Mounter {\n\tpublic ")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box;\n\tpublic ")).output(mark("type", "typeClass")).output(literal(" ")).output(mark("type", "typeName")).output(literal(";\n\n\tpublic void execute() {\n\n\t}\n}")),
				rule().condition((type("schemaimport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;")),
				rule().condition((attribute("message")), (trigger("typeclass"))).output(literal("io.intino.alexandria.inl.Message")),
				rule().condition((type("schema")), (trigger("typeclass"))).output(mark("package")).output(literal(".schemas.")).output(mark("name", "FirstUpperCase")),
				rule().condition((type("schema")), (trigger("typename"))).output(mark("name", "firstLowerCase")),
				rule().condition((trigger("typename"))).output(literal("message"))
		);
	}
}