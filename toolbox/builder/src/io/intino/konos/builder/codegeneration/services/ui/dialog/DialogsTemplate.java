package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DialogsTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("dialogs"))).output(literal("package ")).output(mark("package")).output(literal(".dialogs;\n\nimport io.intino.alexandria.ui.displays.AlexandriaDisplay;\nimport io.intino.alexandria.ui.displays.*;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class Dialogs {\n\tprivate static Map<String, DialogBuilder> dialogMap = new HashMap<>();\n\n\tstatic {\n\t\t{\n\t\t\t")).output(mark("dialog").multiple("\n")).output(literal("\n\t\t}\n\t}\n\n\tpublic static AlexandriaDialog dialogFor(")).output(mark("box", "firstUpperCase")).output(literal("Box box, String name) {\n\t\tif (!dialogMap.containsKey(name)) return null;\n\t\treturn dialogMap.get(name).build(box);\n\t}\n\n\tprivate interface DialogBuilder {\n\t\tAlexandriaDialog build(")).output(mark("box", "firstUpperCase")).output(literal("Box box);\n\t}\n}")),
				rule().condition((trigger("dialog"))).output(literal("dialogMap.put(\"")).output(mark("name")).output(literal("\", (box) -> new ")).output(mark("name", "FirstUpperCase")).output(literal("(box));"))
		);
	}
}