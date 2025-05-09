package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.condition.predicates.Predicates.trigger;
import static io.intino.itrules.template.outputs.Outputs.*;

public class DisplayTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display", "exposed")));
		rules.add(rule().condition(allTypes("display", "template")));
		rules.add(rule().condition(allTypes("display", "collection")).output(literal("import React from \"react\";\nimport { withStyles } from '@material-ui/core/styles';\nimport Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(" from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays")).output(expression().output(literal("/")).output(placeholder("packageType")).output(literal("s"))).output(literal("/Abstract")).output(placeholder("name", "firstUpperCase")).output(literal("\";\nimport ")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays/notifiers/")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier\";\nimport ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays/requesters/")).output(placeholder("name", "firstUpperCase")).output(literal("Requester\";\nimport DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';\nimport { withSnackbar } from 'notistack';\nimport { ")).output(placeholder("type", "firstUpperCase")).output(literal("Styles } from \"alexandria-ui-elements/src/displays/components/")).output(placeholder("type", "firstUpperCase")).output(literal("\";\n\nconst styles = theme => ({\n\t...")).output(placeholder("type", "firstUpperCase")).output(literal("Styles(theme),\n});\n\nclass ")).output(placeholder("name", "firstUpperCase")).output(literal(" extends Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\tthis.notifier = new ")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier(this);\n\t\tthis.requester = new ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester(this);\n\t};\n\n\t")).output(placeholder("notification").multiple("\n\n")).output(literal("\n}\n\nexport default withStyles(styles, { withTheme: true })(withSnackbar(")).output(placeholder("name", "firstUpperCase")).output(literal("));\nDisplayFactory.register(\"")).output(placeholder("name", "firstUpperCase")).output(literal("\", withStyles(styles, { withTheme: true })(withSnackbar(")).output(placeholder("name", "firstUpperCase")).output(literal(")));")));
		rules.add(rule().condition(allTypes("display")).output(literal("import React from \"react\";\nimport { withStyles } from '@material-ui/core/styles';\nimport Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(" from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays")).output(expression().output(literal("/")).output(placeholder("packageType")).output(literal("s"))).output(literal("/Abstract")).output(placeholder("name", "firstUpperCase")).output(literal("\";\nimport ")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays/notifiers/")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier\";\nimport ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays/requesters/")).output(placeholder("name", "firstUpperCase")).output(literal("Requester\";\nimport DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';\nimport { withSnackbar } from 'notistack';\n\nconst styles = theme => ({});\n\nclass ")).output(placeholder("name", "firstUpperCase")).output(literal(" extends Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\tthis.notifier = new ")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier(this);\n\t\tthis.requester = new ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester(this);\n\t};\n\n\t")).output(placeholder("notification").multiple("\n\n")).output(literal("\n}\n\nexport default withStyles(styles, { withTheme: true })(withSnackbar(")).output(placeholder("name", "firstUpperCase")).output(literal("));\nDisplayFactory.register(\"")).output(placeholder("name", "firstUpperCase")).output(literal("\", withStyles(styles, { withTheme: true })(withSnackbar(")).output(placeholder("name", "firstUpperCase")).output(literal(")));")));
		rules.add(rule().condition(allTypes("notification")).output(placeholder("name")).output(literal(" = (")).output(expression().output(placeholder("parameter")).output(literal("value"))).output(literal(") => {\n};")));
		rules.add(rule().condition(trigger("parametervalue")).output(literal("value")));
		rules.add(rule().condition(trigger("parameter")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}