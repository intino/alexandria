package io.intino.konos.builder.codegeneration.services.ui.display.editor;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class EditorTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("editor"))).output(literal("package ")).output(mark("package")).output(literal(".displays;\n\nimport cottons.utils.StreamHelper;\nimport io.intino.alexandria.ui.Resource;\nimport io.intino.alexandria.ui.displays.AlexandriaEditor;\nimport io.intino.alexandria.ui.model.Element;\nimport io.intino.alexandria.ui.services.push.UISession;\nimport io.intino.alexandria.ui.services.EditorService;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".dialogs.*;\nimport ")).output(mark("package", "validPackage")).output(literal(".displays.*;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\t@Override\n\tpublic void prepare(")).output(mark("display", "firstUpperCase")).output(literal(" dialog, io.intino.alexandria.Resource document, EditorService.Permission permission) {\n\t}\n\n\t@Override\n\tpublic void save() {\n\t}\n}"))
		);
	}
}