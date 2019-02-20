package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplayTemplate extends Template {

	protected DisplayTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplayTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display"))).add(literal("import React from \"react\";\nimport { withStyles } from '@material-ui/core/styles';\nimport Abstract")).add(mark("name", "firstUpperCase")).add(literal(" from \"../../")).add(mark("packageTypeRelativeDirectory")).add(literal("gen/displays")).add(expression().add(literal("/")).add(mark("packageType")).add(literal("s"))).add(literal("/Abstract")).add(mark("name", "firstUpperCase")).add(literal("\";\nimport ")).add(mark("name", "firstUpperCase")).add(literal("Notifier from \"../../")).add(mark("packageTypeRelativeDirectory")).add(literal("gen/displays/notifiers/")).add(mark("name", "firstUpperCase")).add(literal("Notifier\";\nimport ")).add(mark("name", "firstUpperCase")).add(literal("Requester from \"../../")).add(mark("packageTypeRelativeDirectory")).add(literal("gen/displays/requesters/")).add(mark("name", "firstUpperCase")).add(literal("Requester\";\n\nconst styles = {};\n\nclass ")).add(mark("name", "firstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "firstUpperCase")).add(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\tthis.notifier = new ")).add(mark("name", "firstUpperCase")).add(literal("Notifier(this);\n\t\tthis.requester = new ")).add(mark("name", "firstUpperCase")).add(literal("Requester(this);\n\t};\n\n\trender() {\n        const { classes } = this.props;\n\t\treturn (\n\t\t\t<React.Fragment></React.Fragment>\n\t\t);\n\t};\n\n\t")).add(mark("notification").multiple("\n\n")).add(literal("\n}\n\nexport default withStyle(styles)(")).add(mark("name", "firstUpperCase")).add(literal(");")),
			rule().add((condition("type", "notification"))).add(mark("name")).add(literal(" = (")).add(expression().add(mark("parameter")).add(literal("value"))).add(literal(") => {\n};")),
			rule().add((condition("trigger", "parameterValue"))).add(literal("value")),
			rule().add((condition("trigger", "parameter")))
		);
		return this;
	}
}