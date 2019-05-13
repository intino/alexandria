package io.intino.konos.builder.codegeneration.accessor.ui.templates;

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
			rule().add((condition("type", "i18n"))).add(literal("const I18n = (function () {\n\n\tvar translators = {\n\t\t")).add(mark("translator").multiple(",\n")).add(literal("\n\t};\n\n\tvar loader = {\n\t\tload: (lang) => {\n\t\t\treturn translators[lang];\n\t\t}\n\t};\n\n\treturn loader;\n})();\n\nexport default I18n;")),
			rule().add((condition("type", "translator"))).add(literal("\"")).add(mark("language")).add(literal("\" : {\n\t")).add(mark("translation").multiple(",\n")).add(literal("\n}")),
			rule().add((condition("type", "translation"))).add(literal("\"")).add(mark("text")).add(literal("\" : \"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}