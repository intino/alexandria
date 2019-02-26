package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DictionaryTemplate extends Template {

	protected DictionaryTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DictionaryTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "dictionaries"))).add(literal("const Dictionary = (function () {\n\n\tvar words = {\n\t\t")).add(mark("dictionary").multiple(",\n")).add(literal("\n\t};\n\n\tvar loader = {\n\t\tload: (lang) => {\n\t\t\treturn words[lang];\n\t\t}\n\t};\n\n\treturn loader;\n})();\n\nexport default Dictionary;")),
			rule().add((condition("type", "dictionary"))).add(literal("\"")).add(mark("language")).add(literal("\" : {\n\t")).add(mark("word").multiple(",\n")).add(literal("\n}")),
			rule().add((condition("type", "word"))).add(literal("\"")).add(mark("text")).add(literal("\" : \"")).add(mark("translation")).add(literal("\""))
		);
		return this;
	}
}