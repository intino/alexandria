package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class I18nTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("i18n"))).output(literal("const I18n = (function () {\n\n\tvar translators = {\n\t\t")).output(mark("translator").multiple(",\n")).output(literal("\n\t};\n\n\tvar loader = {\n\t\tload: (lang) => {\n\t\t\treturn translators[lang];\n\t\t}\n\t};\n\n\treturn loader;\n})();\n\nexport default I18n;")),
			rule().condition((type("translator"))).output(literal("\"")).output(mark("language")).output(literal("\" : {\n\t")).output(mark("translation").multiple(",\n")).output(literal("\n}")),
			rule().condition((type("translation"))).output(literal("\"")).output(mark("text")).output(literal("\" : \"")).output(mark("value")).output(literal("\""))
		);
	}
}