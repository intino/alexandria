package io.intino.konos.builder.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AppTemplate extends Template {

	protected AppTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AppTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "app"))).add(literal("import React from \"react\";\nimport ReactDOM from \"react-dom\";\n")).add(mark("use", "import").multiple("\n")).add(literal("\n\n")).add(mark("use", "render").multiple("\n\n")),
			rule().add((condition("type", "use")), (condition("trigger", "import"))).add(literal("import ")).add(mark("value")).add(literal(" from \"../src/pages/")).add(mark("value")).add(literal("\";")),
			rule().add((condition("type", "use")), (condition("trigger", "render"))).add(literal("const ")).add(mark("use", "firstLowerCase")).add(literal(" = document.getElementById(\"")).add(mark("use")).add(literal("\");\nif (")).add(mark("use", "firstLowerCase")).add(literal(") ReactDOM.render(<")).add(mark("use")).add(literal(" />, homePage);"))
		);
		return this;
	}
}