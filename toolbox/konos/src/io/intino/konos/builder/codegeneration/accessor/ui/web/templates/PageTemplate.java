package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class PageTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("resource", "js")).output(literal("import React from \"react\";\nimport { MuiThemeProvider, withStyles } from '@material-ui/core/styles';\nimport { IconButton } from \"@material-ui/core\";\nimport { Close } from '@material-ui/icons';\nimport { SnackbarProvider, useSnackbar } from \"notistack\";\nimport CssBaseline from '@material-ui/core/CssBaseline';\nimport Theme from '../../gen/Theme';\nimport Page from \"alexandria-ui-elements/src/displays/Page\";\nimport ConnectionChecker from \"alexandria-ui-elements/src/displays/ConnectionChecker\";\nimport ")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal(" from \"")).output(placeholder("pageDisplayOrigin", "origin")).output(literal("/displays/")).output(placeholder("pageDisplayType", "firstLowerCase")).output(literal("s/")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal("\";\n")).output(placeholder("exposedImport").multiple("\n")).output(literal("\n\nlet theme = Theme.create();\nconst styles = theme => ({});\n\nconst ")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal("PageDismissAction = ({ id }) => {\nconst { closeSnackbar } = useSnackbar();\nreturn (<IconButton color=\"inherit\" onClick={() => closeSnackbar(id)}><Close fontSize=\"small\" /></IconButton>);\n}\n\nexport default class ")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal("Page extends Page {\nrender() {\n\tconst { classes } = this.props;\n    theme = Theme.create(this.state.appMode);\n    theme.onChangeMode(mode => this.setState({appMode:mode}));\n\treturn (\n\t\t<MuiThemeProvider theme={theme}>\n\t\t\t<SnackbarProvider maxSnack={3} action={key => <")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal("PageDismissAction id={key}/>}>\n\t\t\t\t<CssBaseline />\n\t\t\t\t<")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal(" id=\"")).output(placeholder("pageDisplayId")).output(literal("\"></")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal(">\n\t\t\t\t<ConnectionChecker></ConnectionChecker>\n\t\t\t</SnackbarProvider>\n\t\t</MuiThemeProvider>\n\t);\n}\n}")));
		rules.add(rule().condition(allTypes("resource", "html")).output(literal("<!DOCTYPE html>\n<html>\n\t<head>\n\t\t<title>$title</title>\n\n\t\t<meta charset=\"utf-8\"/>\n\t\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"/>\n\t\t<meta name=\"viewport\" content=\"width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes\"/>\n\n\t\t<link rel=\"icon\" href=\"$favicon\">\n\n\t\t<script src=\"$url/res/js/jquery-3.6.1.min.js\"></script>\n\t\t<script src=\"$url/res/js/materialize-1.0.0.min.js\"></script>\n\n\t\t<style nonce=\"main_style\">\n\t\t\thtml, body {\n\t\t\t\tmin-height: 100vh;\n\t\t\t\theight: 100%;\n\t\t\t}\n\t\t\t.page {\n\t\t\t\tdisplay: table;\n\t\t\t\theight: 100%;\n\t\t\t\twidth: 100%;\n\t\t\t}\n\t\t</style>\n\t</head>\n\n\t<body>\n\t\t<div class=\"page\" id=\"")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal("\"></div>\n\t\t<script nonce=\"main_script\">\n\t\t\tdocument.configuration = {};\n\t\t\tdocument.configuration.googleApiKey = \"$googleApiKey\";\n\t\t\tdocument.configuration.pushConnections = \"$pushConnections\".split(\",\");\n\t\t\tdocument.configuration.clientId = \"$client\";\n\t\t\tdocument.configuration.language = \"$language\";\n\t\t\tdocument.configuration.url = \"$url\";\n\t\t\tdocument.configuration.baseUrl = \"$baseUrl\";\n\t\t\tdocument.configuration.baseUrls = \"$baseUrls\".split(\",\");\n\t\t\tdocument.configuration.basePath = \"$basePath\";\n\t\t</script>\n\t</body>\n</html>")));
		rules.add(rule().condition(all(attribute("hasabstract"), trigger("origin"))).output(literal("../../src")));
		rules.add(rule().condition(all(attribute("decorated"), trigger("origin"))).output(literal("../../gen")));
		rules.add(rule().condition(trigger("origin")).output(literal("..")));
		rules.add(rule().condition(allTypes("exposedImport")).output(literal("import ")).output(placeholder("service", "firstUpperCase")).output(literal("ExposedDisplays from '")).output(placeholder("elements")).output(literal("/gen/ExposedDisplays';")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}