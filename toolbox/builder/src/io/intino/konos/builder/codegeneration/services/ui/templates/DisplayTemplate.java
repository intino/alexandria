package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DisplayTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("display"))).output(literal("package ")).output(mark("package")).output(literal(".ui.displays")).output(expression().output(literal(".")).output(mark("packageType")).output(literal("s"))).output(literal(";\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package")).output(literal(".*;\n")).output(mark("schemaImport")).output(literal("\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".ui.displays")).output(expression().output(literal(".")).output(mark("packageType")).output(literal("s"))).output(literal(".Abstract")).output(mark("name", "firstUpperCase")).output(literal(";\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "firstUpperCase")).output(literal("<")).output(mark("box", "firstUpperCase")).output(literal("Box> {\n\n    public ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n        super(box);\n    }\n\n\t")).output(expression().output(mark("request").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(mark("parameter", "setter").multiple("\n\n"))).output(literal("\n}")),
			rule().condition((type("templatesImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.templates.*;")),
			rule().condition((type("blocksImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.blocks.*;")),
			rule().condition((type("itemsImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.items.*;")),
			rule().condition((allTypes("request","asset"))).output(literal("public io.intino.alexandria.ui.spark.UIFile ")).output(mark("name")).output(literal("(")).output(expression().output(mark("parameter")).output(literal(" value"))).output(literal(") {\n    return null;\n}")),
			rule().condition((type("request"))).output(literal("public void ")).output(mark("name")).output(literal("(")).output(expression().output(mark("parameter")).output(literal(" value"))).output(literal(") {\n\n}")),
			rule().condition((trigger("setter"))).output(literal("public void ")).output(mark("value", "firstLowerCase")).output(literal("(String value) {\n\n}")),
			rule().condition((anyTypes("dateTime","date")), (type("list")), (trigger("parameter"))).output(mark("value")),
			rule().condition((anyTypes("dateTime","date")), (trigger("parameter"))).output(mark("value")),
			rule().condition((type("list")), (trigger("parameter"))).output(mark("value")).output(literal("[]")),
			rule().condition((trigger("parameter"))).output(mark("value")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;")),
			rule().condition((trigger("import"))).output(literal("import ")).output(mark("package")).output(literal(".ui.displays.*;"))
		);
	}
}