package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DialogTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("dialog"))).output(literal("package ")).output(mark("package")).output(literal(".dialogs;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.ui.model.Dialog;\nimport io.intino.alexandria.ui.displays.DialogValidator;\n\nimport static io.intino.alexandria.ui.model.dialog.DialogResult;\n\npublic class ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal(" {\n\n\tpublic ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("(")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\t@Override\n\tpublic void prepare() {\n\t}\n\n\tpublic static class Toolbar {\n\t\t")).output(mark("execution").multiple("\n")).output(literal("\n\t}\n\n\tpublic static class Validators {\n\t\t")).output(mark("validator").multiple("\n")).output(literal("\n\t}\n\n\tpublic static class Sources {\n\t\t")).output(mark("source").multiple("\n")).output(literal("\n\t}\n}")),
				rule().condition((type("execution"))).output(literal("public static DialogResult ")).output(mark("name")).output(literal("(")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box, Dialog dialog, Dialog.Toolbar.Operation self, io.intino.alexandria.ui.services.push.UISession session) {\n\treturn DialogResult.none();\n}\n")),
				rule().condition((type("validator"))).output(literal("public static DialogValidator.Result ")).output(mark("name")).output(literal("(")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box, Dialog.Tab.")).output(mark("field")).output(literal(" value) {\n\treturn null;\n}\n")),
				rule().condition((type("source"))).output(literal("public static java.util.List<String> ")).output(mark("name")).output(literal("(")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box, Dialog.Tab.")).output(mark("field")).output(literal(" self) {\n\treturn java.util.Collections.emptyList();\n}\n"))
		);
	}
}