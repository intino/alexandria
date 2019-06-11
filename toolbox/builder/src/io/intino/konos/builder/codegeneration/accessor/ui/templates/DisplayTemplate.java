package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DisplayTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("display"))).output(literal("import React from \"react\";\nimport { withStyles } from '@material-ui/core/styles';\nimport Abstract")).output(mark("name", "firstUpperCase")).output(literal(" from \"../../")).output(mark("packageTypeRelativeDirectory")).output(literal("gen/displays")).output(expression().output(literal("/")).output(mark("packageType")).output(literal("s"))).output(literal("/Abstract")).output(mark("name", "firstUpperCase")).output(literal("\";\nimport ")).output(mark("name", "firstUpperCase")).output(literal("Notifier from \"../../")).output(mark("packageTypeRelativeDirectory")).output(literal("gen/displays/notifiers/")).output(mark("name", "firstUpperCase")).output(literal("Notifier\";\nimport ")).output(mark("name", "firstUpperCase")).output(literal("Requester from \"../../")).output(mark("packageTypeRelativeDirectory")).output(literal("gen/displays/requesters/")).output(mark("name", "firstUpperCase")).output(literal("Requester\";\nimport DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';\nimport { withSnackbar } from 'notistack';\n\nconst styles = theme => ({});\n\nclass ")).output(mark("name", "firstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "firstUpperCase")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\tthis.notifier = new ")).output(mark("name", "firstUpperCase")).output(literal("Notifier(this);\n\t\tthis.requester = new ")).output(mark("name", "firstUpperCase")).output(literal("Requester(this);\n\t};\n\n\t")).output(mark("notification").multiple("\n\n")).output(literal("\n}\n\nexport default withStyles(styles, { withTheme: true })(withSnackbar(")).output(mark("name", "firstUpperCase")).output(literal("));\nDisplayFactory.register(\"")).output(mark("name", "firstUpperCase")).output(literal("\", withStyles(styles, { withTheme: true })(withSnackbar(")).output(mark("name", "firstUpperCase")).output(literal(")));")),
			rule().condition((type("notification"))).output(mark("name")).output(literal(" = (")).output(expression().output(mark("parameter")).output(literal("value"))).output(literal(") => {\n};")),
			rule().condition((trigger("parametervalue"))).output(literal("value")),
			rule().condition((trigger("parameter")))
		);
	}
}