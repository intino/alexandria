package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.*;

public class ThemeTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("theme")).output(literal("import { createTheme } from '@material-ui/core/styles';\n\nconst Theme = (function () {\n\tvar theme = null;\n\tvar provider = {\n\t\tcreate: () => {\n\t\t\ttheme = createTheme({\n\t\t\t\tpalette : {\n\t\t\t\t\t")).output(placeholder("palette")).output(literal("\n\t\t\t\t},\n\t\t\t\ttypography : {\n\t\t\t\t\t")).output(placeholder("typography")).output(literal("\n\t\t\t\t},\n\t\t\t\tformats: {\n\t\t\t\t\t")).output(placeholder("format").multiple(",\n")).output(literal("\n\t\t\t\t}\n\t\t\t});\n\t\t\treturn theme;\n\t\t},\n\t\tget: () => {\n\t\t\treturn theme;\n\t\t},\n\t};\n\treturn provider;\n})();\n\nexport default Theme;")));
		rules.add(rule().condition(allTypes("palette")).output(expression().output(literal("type : \"")).output(placeholder("type", "lowerCase")).output(literal("\",")).output(literal("\n"))).output(expression().output(literal("primary : { main: \"")).output(placeholder("primary")).output(literal("\" },")).output(literal("\n"))).output(expression().output(literal("secondary : { main: \"")).output(placeholder("secondary")).output(literal("\" },")).output(literal("\n"))).output(expression().output(literal("error : { main: \"")).output(placeholder("error")).output(literal("\" },")).output(literal("\n"))).output(literal("contrastThreshold : \"")).output(placeholder("contrastThreshold")).output(literal("\",\ntonalOffset : \"")).output(placeholder("tonalOffset")).output(literal("\",\ngrey : {\n\tprimary: \"#888\",\n\tsecondary: '#ddd'\n}")));
		rules.add(rule().condition(allTypes("typography")).output(literal("fontFamily : '")).output(placeholder("fontFamily")).output(literal("',\nfontSize : ")).output(placeholder("fontSize")).output(literal(",\nuseNextVariants: true")));
		rules.add(rule().condition(allTypes("format")).output(placeholder("name")).output(literal(": { ")).output(placeholder("content")).output(literal(" }")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}