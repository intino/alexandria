package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class I18nTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("i18n"))).output(mark("use", "import").multiple("\n")).output(literal("\n\nconst I18n = (function () {\n    var cache = {};\n\n\tvar translators = {\n\t\t")).output(mark("translator").multiple(",\n")).output(literal("\n\t};\n\n    function merge(dictionary, lang) {\n        if (dictionary == null) dictionary = {};\n        ")).output(mark("use", "addWords").multiple("\n")).output(literal("\n        return dictionary;\n    };\n\n    function addWords(dictionary, words) {\n        if (words == null) return;\n        for (var index in words) dictionary[index] = words[index];\n        return dictionary;\n    };\n\n\tvar loader = {\n\t\tload: (lang) => {\n            if (cache[lang] == null) cache[lang] = merge(translators[lang], lang);\n            return cache[lang];\n\t\t}\n\t};\n\n\treturn loader;\n})();\n\nexport default I18n;")),
			rule().condition((type("translator"))).output(literal("\"")).output(mark("language")).output(literal("\" : {\n\t")).output(mark("translation").multiple(",\n")).output(literal("\n}")),
			rule().condition((type("translation"))).output(literal("\"")).output(mark("text")).output(literal("\" : \"")).output(mark("value")).output(literal("\"")),
			rule().condition((type("use")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUpperCase")).output(literal("I18n from \"")).output(mark("service", "camelCaseToSnakeCase")).output(literal("/gen/I18n\";")),
			rule().condition((type("use")), (trigger("addwords"))).output(literal("addWords(dictionary, ")).output(mark("name", "firstUpperCase")).output(literal("I18n.load(lang));"))
		);
	}
}