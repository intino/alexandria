package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class PanelSkeletonTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("panel"))).output(literal("package ")).output(mark("package")).output(literal(".displays;\n\nimport io.intino.alexandria.ui.displays.AlexandriaDisplay;\nimport io.intino.alexandria.ui.displays.CatalogInstantBlock;\nimport io.intino.alexandria.ui.services.push.UISession;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\nimport java.util.function.Consumer;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "firstUpperCase")).output(literal(" {\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Toolbar {\n\t\t")).output(mark("operation").multiple("\n")).output(literal("\n\t}\n\n\tpublic static class Views {\n\t\t")).output(mark("view").multiple("\n")).output(literal("\n\t}\n}"))
		);
	}
}