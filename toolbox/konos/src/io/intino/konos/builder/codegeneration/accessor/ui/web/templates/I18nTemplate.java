package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class I18nTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("i18n")).output(placeholder("use", "import").multiple("\n")).output(literal("\n\nconst I18n = (function () {\n\tvar cache = {};\n\n\tvar translators = {\n\t\t")).output(placeholder("translator").multiple(",\n")).output(literal("\n\t};\n\n\tfunction merge(dictionary, lang) {\n\t\tif (dictionary == null) dictionary = {};\n\t\t")).output(placeholder("use", "addWords").multiple("\n")).output(literal("\n\t\treturn dictionary;\n\t};\n\n\tfunction addWords(dictionary, words) {\n\t\tif (words == null) return;\n\t\tfor (var index in words) dictionary[index] = words[index];\n\t\treturn dictionary;\n\t};\n\n\tvar loader = {\n\t\tload: (lang) => {\n\t\t\tif (cache[lang] == null) cache[lang] = merge(translators[lang], lang);\n\t\t\treturn cache[lang];\n\t\t}\n\t};\n\n\treturn loader;\n})();\n\nexport default I18n;")));
		rules.add(rule().condition(allTypes("translator")).output(literal("\"")).output(placeholder("language")).output(literal("\" : {\n\t")).output(placeholder("translation").multiple(",\n")).output(literal("\n}")));
		rules.add(rule().condition(allTypes("translation")).output(literal("\"")).output(placeholder("text")).output(literal("\" : \"")).output(placeholder("value")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("use"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUpperCase")).output(literal("I18n from \"")).output(placeholder("service", "camelCaseToKebabCase")).output(literal("/gen/I18n\";")));
		rules.add(rule().condition(all(allTypes("use"), trigger("addwords"))).output(literal("addWords(dictionary, ")).output(placeholder("name", "firstUpperCase")).output(literal("I18n.load(lang));")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}