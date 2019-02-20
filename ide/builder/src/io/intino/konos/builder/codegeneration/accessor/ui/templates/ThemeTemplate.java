package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ThemeTemplate extends Template {

	protected ThemeTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ThemeTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "theme"))).add(literal("import { createMuiTheme } from '@material-ui/core/styles';\n\nconst Theme = (function () {\n\tvar theme = null;\n\tvar provider = {\n\t\tcreate: () => {\n\t\t\ttheme = createMuiTheme({\n\t\t\t\tpalette : {\n\t\t\t\t\t")).add(mark("paletteRule", "rule").multiple(",\n")).add(literal("\n\t\t\t\t}")).add(expression().add(literal(",")).add(literal("\n")).add(literal("\t\t\t\t")).add(mark("customRule", "rule").multiple(",\n")).add(literal("\n")).add(literal("\t\t\t\t"))).add(literal("\n\t\t\t});\n\t\t\treturn theme;\n\t\t},\n\t\tget: () => {\n\t\t\treturn theme;\n\t\t},\n\t};\n\treturn provider;\n})();\n\nexport default Theme;")),
			rule().add((condition("trigger", "rule"))).add(mark("type")).add(literal(": { ")).add(mark("content")).add(literal(" }"))
		);
		return this;
	}
}