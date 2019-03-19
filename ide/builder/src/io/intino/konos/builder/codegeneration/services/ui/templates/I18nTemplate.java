package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class I18nTemplate extends Template {

	protected I18nTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new I18nTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "i18n"))).add(literal("package ")).add(mark("package")).add(literal(".ui;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class I18n {\n\tprivate static Map<String, Map<String, String>> dictionaries = new HashMap<>();\n\n\tpublic static String translate(String word, String language) {\n\t\tlanguage = dictionaries.containsKey(language) ? language : \"en\";\n\t\tMap<String, String> languageDictionary = dictionaries.get(language);\n\t\treturn languageDictionary.containsKey(word) ? languageDictionary.get(word) : word;\n\t}\n\n\tstatic {\n\t\t")).add(mark("translator").multiple(",\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "translator"))).add(literal("dictionaries.put(\"")).add(mark("language")).add(literal("\", new HashMap<String, String>() {{\n\t")).add(mark("translation").multiple(",\n")).add(literal("\n}});")),
			rule().add((condition("type", "translation"))).add(literal("put(\"")).add(mark("text")).add(literal("\", \"")).add(mark("value")).add(literal("\");"))
		);
		return this;
	}
}