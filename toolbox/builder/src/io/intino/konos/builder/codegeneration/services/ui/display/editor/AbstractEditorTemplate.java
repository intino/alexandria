package io.intino.konos.builder.codegeneration.services.ui.display.editor;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractEditorTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("editor", "gen"))).output(literal("package ")).output(mark("package")).output(literal(".displays;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".displays.*;\nimport ")).output(mark("package", "validPackage")).output(literal(".dialogs.*;\nimport io.intino.alexandria.ui.displays.AlexandriaEditor;\n\npublic abstract class Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" extends io.intino.alexandria.ui.displays.AlexandriaEditor<")).output(mark("display", "FirstUpperCase")).output(literal("> {\n\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n}"))
		);
	}
}