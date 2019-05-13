package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class I18nTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("i18n"))).output(literal("package ")).output(mark("package")).output(literal(".ui;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class I18n {\n\tprivate static Map<String, Map<String, String>> dictionaries = new HashMap<>();\n\n\tpublic static String translate(String word, String language) {\n\t\tlanguage = dictionaries.containsKey(language) ? language : \"en\";\n\t\tMap<String, String> languageDictionary = dictionaries.get(language);\n\t\treturn languageDictionary.containsKey(word) ? languageDictionary.get(word) : word;\n\t}\n\n\tstatic {\n\t\t")).output(mark("translator").multiple(",\n")).output(literal("\n\t}\n}")),
			rule().condition((type("translator"))).output(literal("dictionaries.put(\"")).output(mark("language")).output(literal("\", new HashMap<String, String>() {{\n\t")).output(mark("translation").multiple("\n")).output(literal("\n}});")),
			rule().condition((type("translation"))).output(literal("put(\"")).output(mark("text")).output(literal("\", \"")).output(mark("value")).output(literal("\");"))
		);
	}
}