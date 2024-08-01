package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class ThemeTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("theme")).output(literal("import { createTheme } from '@material-ui/core/styles';\n\nconst Theme = (function () {\n\tvar theme = null;\n\tvar provider = {\n        defaultMode: () => {\n            ")).output(placeholder("palette", "defaultMode")).output(literal("\n        },\n        isAutoMode: () => {\n            ")).output(placeholder("palette", "autoMode")).output(literal("\n        },\n\t\tcreate: (mode) => {\n            ")).output(placeholder("palette", "vars")).output(literal("\n\t\t\ttheme = createTheme({\n\t\t\t\tpalette : {\n\t\t\t\t\t")).output(placeholder("palette")).output(literal("\n\t\t\t\t},\n\t\t\t\ttypography : {\n\t\t\t\t\t")).output(placeholder("typography")).output(literal("\n\t\t\t\t},\n\t\t\t\tdarkFormats: {\n\t\t\t\t\t")).output(placeholder("format", "dark").multiple(",\n")).output(literal("\n\t\t\t\t},\n\t\t\t\tformats: {\n\t\t\t\t\t")).output(placeholder("format").multiple(",\n")).output(literal("\n\t\t\t\t}\n\t\t\t});\n            theme.isLight = () => {\n                return theme.palette.type == \"light\";\n            };\n            theme.isDark = () => {\n                return theme.palette.type == \"dark\";\n            };\n            theme.onChangeMode = (listener) => {\n                theme.changeModeListener = listener;\n            }\n            theme.setMode = (mode) => {\n                theme.changeModeListener(mode);\n            }\n\t\t\treturn theme;\n\t\t},\n\t\tget: () => {\n\t\t\treturn theme;\n\t\t},\n\t};\n\treturn provider;\n})();\n\nfunction property(name) {\n    var theme = Theme.get();\n    if (theme == null) return;\n    var parts = name.split(\".\");\n    var property = theme;\n    for (var i=0; i<parts.length; i++) property = property[parts[i]];\n    return property;\n}\n\nexport default Theme;")));
		rules.add(rule().condition(all(allTypes("palette"), trigger("defaultmode"))).output(literal("return \"")).output(placeholder("type", "lowerCase")).output(literal("\";")));
		rules.add(rule().condition(all(allTypes("palette"), trigger("automode"))).output(literal("return \"")).output(placeholder("type", "lowerCase")).output(literal("\" == \"auto\";")));
		rules.add(rule().condition(all(allTypes("palette"), trigger("darkmode"))).output(literal("return \"")).output(placeholder("type", "lowerCase")).output(literal("\" == \"dark\";")));
		rules.add(rule().condition(all(allTypes("palette"), trigger("vars"))).output(literal("const primary = mode == \"dark\" ? \"")).output(placeholder("darkPrimary")).output(literal("\" : \"")).output(placeholder("primary")).output(literal("\";\nconst secondary = mode == \"dark\" ? \"")).output(placeholder("darkSecondary")).output(literal("\" : \"")).output(placeholder("secondary")).output(literal("\";\nconst error = mode == \"dark\" ? \"")).output(placeholder("darkError")).output(literal("\" : \"")).output(placeholder("error")).output(literal("\";")));
		rules.add(rule().condition(allTypes("palette")).output(literal("type : mode,\n")).output(expression().output(literal("primary : { main: primary },")).output(literal("\n"))).output(expression().output(literal("secondary : { main: secondary },")).output(literal("\n"))).output(expression().output(literal("error : { main: error },")).output(literal("\n"))).output(literal("contrastThreshold : \"")).output(placeholder("contrastThreshold")).output(literal("\",\ntonalOffset : \"")).output(placeholder("tonalOffset")).output(literal("\",\ngrey : {\n\tprimary: \"#888\",\n\tsecondary: '#ddd'\n}")));
		rules.add(rule().condition(allTypes("typography")).output(literal("fontFamily : '")).output(placeholder("fontFamily")).output(literal("',\nfontSize : ")).output(placeholder("fontSize")).output(literal(",\nuseNextVariants: true")));
		rules.add(rule().condition(all(allTypes("format", "emptyContent", "dark"), trigger("dark"))).output(placeholder("name")).output(literal(": { ")).output(placeholder("darkContent")).output(literal(" }")));
		rules.add(rule().condition(all(allTypes("format", "dark"), trigger("dark"))).output(placeholder("name")).output(literal(": { ")).output(placeholder("content")).output(literal(", ")).output(placeholder("darkContent")).output(literal(" }")));
		rules.add(rule().condition(all(allTypes("format"), trigger("dark"))));
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