package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AppTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("app")).output(literal("__webpack_public_path__ = loadAppUrl();\n\nimport React from \"react\";\nimport ReactDOM from \"react-dom\";\n")).output(placeholder("page", "import").multiple("\n")).output(literal("\nimport PushService from \"alexandria-ui-elements/src/services/PushService\";\nimport FileService from \"alexandria-ui-elements/src/services/FileService\";\nimport TranslatorService from \"alexandria-ui-elements/src/services/TranslatorService\";\nimport GoogleApiLoader from \"alexandria-ui-elements/src/displays/components/geo/GoogleApiLoader\";\nimport DisplayRouter from \"alexandria-ui-elements/src/displays/DisplayRouter\";\n\nvar launchApplication = function () {\n\tvar configuration = loadConfiguration();\n\n\twindow.Application = (function(configuration) {\n\t\tvar self = {};\n\n\t\tself.configuration = configuration;\n\t\tself.configuration.appUrls = loadUrls(configuration.baseUrls);\n\t\tself.configuration.appUrl = (app) => {\n\t\t\treturn self.configuration.appUrls[app] != null ? self.configuration.appUrls[app] : self.configuration.baseUrl;\n\t\t};\n\t\tself.services = {\n\t\t\tpushService: PushService,\n\t\t\tfileService: FileService.create(configuration),\n\t\t\ttranslatorService: TranslatorService.create(configuration)\n\t\t};\n\n\t\treturn self;\n\t})(configuration);\n\n\trenderApplication();\n\n\tfunction loadConfiguration() {\n\t\treturn document.configuration;\n\t}\n\n\tfunction loadUrls(urlList) {\n\t\tlet result = {};\n\t\tif (urlList == null) return result;\n\t\tfor (let i=0; i<urlList.length; i++) {\n\t\t\tconst urlInfo = urlList[i].split(\"_##_\");\n\t\t\tresult[urlInfo[0]] = urlInfo[1];\n\t\t}\n\t\treturn result;\n\t}\n\n\tfunction renderApplication() {\n\t\t")).output(placeholder("page", "render").multiple("\n\n")).output(literal("\n\t}\n\n\tfunction openPushService() {\n\t\twindow.setTimeout(() => {\n\t\t\tconst pushConnections = Application.configuration.pushConnections;\n\t\t\tfor (let i=0; i<pushConnections.length; i++) {\n\t\t\t\tconst connection = pushConnections[i].split(\"_##_\");\n\t\t\t\tif (connection[0].toLowerCase() !== \"default\") continue;\n\t\t\t\tPushService.openConnection(connection[0], connection[1]);\n\t\t\t}\n\t\t}, 100);\n\t}\n\n};\n\nfunction loadAppUrl() {\n\tlet url = window.location.pathname !== \"/\" ? window.location.pathname : \"\";\n\t")).output(expression().output(placeholder("pattern").multiple("\n"))).output(literal("\n\tif (url.lastIndexOf(\"/\") > 0) url = url.substr(0, window.location.pathname.lastIndexOf('/'));\n\tif (url === \"/\") url = \"\";\n\treturn url + \"/")).output(placeholder("serviceName")).output(literal("/\";\n}\n\nlaunchApplication();")));
		rules.add(rule().condition(allTypes("pattern")).output(literal("url = url.replace(new RegExp(\"")).output(placeholder("value")).output(literal("\", 'g'), \"\");")));
		rules.add(rule().condition(all(allTypes("page"), trigger("import"))).output(literal("import ")).output(placeholder("templateName", "firstUpperCase")).output(literal("Page from \"../pages/")).output(placeholder("templateName", "firstUpperCase")).output(literal("Page\";")));
		rules.add(rule().condition(all(allTypes("page"), trigger("render"))).output(literal("const requireGoogleApi = Application.configuration.googleApiKey != null && Application.configuration.googleApiKey !== \"\";\nconst content = requireGoogleApi ? <GoogleApiLoader onLoad={openPushService()}><")).output(placeholder("templateName", "firstUpperCase")).output(literal("Page/></GoogleApiLoader> : <")).output(placeholder("templateName", "firstUpperCase")).output(literal("Page/>;\nconst ")).output(placeholder("templateName", "firstLowerCase")).output(literal(" = document.getElementById(\"")).output(placeholder("templateName", "firstUpperCase")).output(literal("\");\nif (")).output(placeholder("templateName", "firstLowerCase")).output(literal(") ReactDOM.render(<DisplayRouter id=\"__router__\" owner={()=>\"\"} context={()=>\"\"}>{content}</DisplayRouter>, ")).output(placeholder("templateName", "firstLowerCase")).output(literal(");\nif (!requireGoogleApi) openPushService();")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}