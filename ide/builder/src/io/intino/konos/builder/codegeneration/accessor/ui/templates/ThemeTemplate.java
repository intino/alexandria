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
			rule().add((condition("type", "theme"))).add(literal("import { createMuiTheme } from '@material-ui/core/styles';\n\nconst Theme = (function () {\n\tvar theme = null;\n\tvar provider = {\n\t\tcreate: () => {\n\t\t\ttheme = createMuiTheme({\n\t\t\t\tpalette : { ")).add(mark("palette")).add(literal(" },\n\t\t\t\ttypography : { ")).add(mark("typography")).add(literal(" },\n\t\t\t\tcomponentStyles: { ")).add(mark("style").multiple(",\n")).add(literal(" }\n\t\t\t});\n\t\t\treturn theme;\n\t\t},\n\t\tget: () => {\n\t\t\treturn theme;\n\t\t},\n\t};\n\treturn provider;\n})();\n\nexport default Theme;")),
			rule().add((condition("type", "palette"))).add(expression().add(literal("type : \"")).add(mark("type", "lowerCase")).add(literal("\",")).add(literal("\n"))).add(expression().add(literal("primary : { main: \"")).add(mark("primary")).add(literal("\" },")).add(literal("\n"))).add(expression().add(literal("secondary : { main: \"")).add(mark("secondary")).add(literal("\" },")).add(literal("\n"))).add(expression().add(literal("error : { main: \"")).add(mark("error")).add(literal("\" },")).add(literal("\n"))).add(literal("contrastThreshold : \"")).add(mark("contrastThreshold")).add(literal("\",\ntonalOffset : \"")).add(mark("tonalOffset")).add(literal("\",\ngrey : {\n\tprimary: \"#888\",\n\tsecondary: '#ddd'\n}")),
			rule().add((condition("type", "typography"))).add(literal("fontFamily : '")).add(mark("fontFamily")).add(literal("',\nhtmlFontSize : ")).add(mark("fontSize")).add(literal(",\nuseNextVariants: true")),
			rule().add((condition("type", "style"))).add(mark("name")).add(literal(": {\n\t")).add(mark("property").multiple(",\n")).add(literal("\n}")),
			rule().add((condition("type", "property"))).add(mark("name")).add(literal(": \"")).add(mark("content")).add(literal("\""))
		);
		return this;
	}
}