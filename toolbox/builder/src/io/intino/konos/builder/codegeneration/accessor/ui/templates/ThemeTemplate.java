package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ThemeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("theme"))).output(literal("import { createMuiTheme } from '@material-ui/core/styles';\n\nconst Theme = (function () {\n\tvar theme = null;\n\tvar provider = {\n\t\tcreate: () => {\n\t\t\ttheme = createMuiTheme({\n\t\t\t\tpalette : {\n\t\t\t\t    ")).output(mark("palette")).output(literal("\n\t\t\t\t},\n\t\t\t\ttypography : {\n\t\t\t\t    ")).output(mark("typography")).output(literal("\n\t\t\t\t},\n\t\t\t\tformats: {\n\t\t\t\t    ")).output(mark("format").multiple(",\n")).output(literal("\n\t\t\t\t}\n\t\t\t});\n\t\t\treturn theme;\n\t\t},\n\t\tget: () => {\n\t\t\treturn theme;\n\t\t},\n\t};\n\treturn provider;\n})();\n\nexport default Theme;")),
			rule().condition((type("palette"))).output(expression().output(literal("type : \"")).output(mark("type", "lowerCase")).output(literal("\",")).output(literal("\n"))).output(expression().output(literal("primary : { main: \"")).output(mark("primary")).output(literal("\" },")).output(literal("\n"))).output(expression().output(literal("secondary : { main: \"")).output(mark("secondary")).output(literal("\" },")).output(literal("\n"))).output(expression().output(literal("error : { main: \"")).output(mark("error")).output(literal("\" },")).output(literal("\n"))).output(literal("contrastThreshold : \"")).output(mark("contrastThreshold")).output(literal("\",\ntonalOffset : \"")).output(mark("tonalOffset")).output(literal("\",\ngrey : {\n\tprimary: \"#888\",\n\tsecondary: '#ddd'\n}")),
			rule().condition((type("typography"))).output(literal("fontFamily : '")).output(mark("fontFamily")).output(literal("',\nfontSize : ")).output(mark("fontSize")).output(literal(",\nuseNextVariants: true")),
			rule().condition((type("format"))).output(mark("name")).output(literal(": { ")).output(mark("content")).output(literal(" }"))
		);
	}
}