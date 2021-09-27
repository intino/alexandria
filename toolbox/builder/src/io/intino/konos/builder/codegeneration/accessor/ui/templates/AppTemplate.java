package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AppTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("app"))).output(literal("__webpack_public_path__ = loadAppUrl();\n\nimport React from \"react\";\nimport ReactDOM from \"react-dom\";\n")).output(mark("page", "import").multiple("\n")).output(literal("\nimport PushService from \"alexandria-ui-elements/src/services/PushService\";\nimport FileService from \"alexandria-ui-elements/src/services/FileService\";\nimport TranslatorService from \"alexandria-ui-elements/src/services/TranslatorService\";\nimport GoogleApiLoader from \"alexandria-ui-elements/src/displays/components/geo/GoogleApiLoader\";\nimport DisplayRouter from \"alexandria-ui-elements/src/displays/DisplayRouter\";\n\nvar launchApplication = function () {\n\tvar configuration = loadConfiguration();\n\n\twindow.Application = (function(configuration) {\n\t\tvar self = {};\n\n\t\tself.configuration = configuration;\n\t\tself.services = {\n\t\t\tpushService: PushService,\n\t\t\tfileService: FileService.create(configuration),\n\t\t\ttranslatorService: TranslatorService.create(configuration)\n\t\t};\n\n\t\treturn self;\n\t})(configuration);\n\n\trenderApplication();\n\n\tfunction loadConfiguration() {\n\t\treturn document.configuration;\n\t}\n\n\tfunction renderApplication() {\n\t\t")).output(mark("page", "render").multiple("\n\n")).output(literal("\n\t}\n\n    function openPushServices() {\n        window.setTimeout(() => {\n            const configuration = Application.configuration;\n            const pushConnections = configuration.pushConnections;\n            for (let i=0; i<pushConnections.length; i++) {\n                const connection = pushConnections[i].split(\"_##_\");\n                PushService.openConnection(connection[0], connection[1]);\n            }\n        }, 100);\n    }\n\n};\n\nfunction loadAppUrl() {\n    let url = window.location.pathname !== \"/\" ? window.location.pathname : \"\";\n    ")).output(expression().output(mark("pattern").multiple("\n"))).output(literal("\n    if (url.lastIndexOf(\"/\") > 0) url = url.substr(0, window.location.pathname.lastIndexOf('/'));\n    if (url === \"/\") url = \"\";\n    return url + \"/")).output(mark("webModuleName")).output(literal("/\";\n}\n\nlaunchApplication();")),
			rule().condition((type("pattern"))).output(literal("url = url.replace(new RegExp(\"")).output(mark("value")).output(literal("\", 'g'), \"\");")),
			rule().condition((type("page")), (trigger("import"))).output(literal("import ")).output(mark("templateName", "firstUpperCase")).output(literal("Page from \"../pages/")).output(mark("templateName", "firstUpperCase")).output(literal("Page\";")),
			rule().condition((type("page")), (trigger("render"))).output(literal("const requireGoogleApi = Application.configuration.googleApiKey != null && Application.configuration.googleApiKey !== \"\";\nconst content = requireGoogleApi ? <GoogleApiLoader onLoad={openPushServices()}><")).output(mark("templateName", "firstUpperCase")).output(literal("Page/></GoogleApiLoader> : <")).output(mark("templateName", "firstUpperCase")).output(literal("Page/>;\nconst ")).output(mark("templateName", "firstLowerCase")).output(literal(" = document.getElementById(\"")).output(mark("templateName", "firstUpperCase")).output(literal("\");\nif (")).output(mark("templateName", "firstLowerCase")).output(literal(") ReactDOM.render(<DisplayRouter id=\"__router__\" owner={()=>\"\"} context={()=>\"\"}>{content}</DisplayRouter>, ")).output(mark("templateName", "firstLowerCase")).output(literal(");\nif (!requireGoogleApi) openPushServices();"))
		);
	}
}