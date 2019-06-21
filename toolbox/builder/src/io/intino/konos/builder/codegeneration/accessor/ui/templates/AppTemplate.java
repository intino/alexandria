package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AppTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("app"))).output(literal("import React from \"react\";\nimport ReactDOM from \"react-dom\";\n")).output(mark("page", "import").multiple("\n")).output(literal("\nimport PushService from \"alexandria-ui-elements/src/services/PushService\";\nimport FileService from \"alexandria-ui-elements/src/services/FileService\";\nimport TranslatorService from \"alexandria-ui-elements/src/services/TranslatorService\";\n\nvar launchApplication = function () {\n\tvar configuration = loadConfiguration();\n\n\twindow.Application = (function(configuration) {\n\t\tvar self = {};\n\n\t\tself.configuration = configuration;\n\t\tself.services = {\n\t\t\tpushService: PushService,\n\t\t\tfileService: FileService.create(configuration),\n\t\t\ttranslatorService: TranslatorService.create(configuration)\n\t\t};\n\n\t\treturn self;\n\t})(configuration);\n\n\trenderApplication();\n    for (var i=0; i<configuration.pushServices.length; i++)\n        PushService.openConnection(configuration.pushServices[i]);\n\n\tfunction loadConfiguration() {\n\t\treturn document.configuration;\n\t}\n\n\tfunction renderApplication() {\n\t\t")).output(mark("page", "render").multiple("\n\n")).output(literal("\n\t}\n};\n\nlaunchApplication();")),
			rule().condition((type("page")), (trigger("import"))).output(literal("import ")).output(mark("value", "firstUpperCase")).output(literal(" from \"../pages/")).output(mark("value", "firstUpperCase")).output(literal("\";")),
			rule().condition((type("page")), (trigger("render"))).output(literal("const ")).output(mark("value", "firstLowerCase")).output(literal(" = document.getElementById(\"")).output(mark("value", "firstUpperCase")).output(literal("\");\nif (")).output(mark("value", "firstLowerCase")).output(literal(") ReactDOM.render(<")).output(mark("value", "firstUpperCase")).output(literal(" />, ")).output(mark("value", "firstLowerCase")).output(literal(");"))
		);
	}
}