package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.*;

public class I18nTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("i18n")).output(literal("package ")).output(placeholder("package")).output(literal(";\n\nimport io.intino.alexandria.ui.services.translator.Dictionary;\n\nimport java.util.HashMap;\nimport java.util.Map;\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic class I18n {\n\tprivate static Map<String, Dictionary> dictionaries = new HashMap<>();\n\n\tpublic static String translate(String word, String language) {\n\t\tlanguage = dictionaries.containsKey(language) ? language : \"en\";\n\t\tDictionary dictionary = dictionaries.get(language);\n\t\treturn dictionary != null && dictionary.containsKey(word) ? dictionary.get(word) : word;\n\t}\n\n\tpublic static List<Dictionary> dictionaries() {\n\t\treturn new ArrayList<>(dictionaries.values());\n\t}\n\n\t")).output(expression().output(literal("static {")).output(literal("\n")).output(literal("\t")).output(placeholder("translator").multiple("\n")).output(literal("\n")).output(literal("}"))).output(literal("\n}")));
		rules.add(rule().condition(allTypes("translator")).output(literal("dictionaries.put(\"")).output(placeholder("language")).output(literal("\", new Dictionary() {{\n\t")).output(placeholder("translation").multiple("\n")).output(literal("\n}}.language(\"")).output(placeholder("language")).output(literal("\"));")));
		rules.add(rule().condition(allTypes("translation")).output(literal("put(\"")).output(placeholder("text")).output(literal("\", \"")).output(placeholder("value")).output(literal("\");")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}