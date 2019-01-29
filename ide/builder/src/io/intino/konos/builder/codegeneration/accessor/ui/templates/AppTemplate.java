package io.intino.konos.builder.codegeneration.accessor.ui.templates;

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
			rule().add((condition("type", "app"))).add(literal("import React from \"react\";\nimport ReactDOM from \"react-dom\";\n")).add(mark("page", "import").multiple("\n")).add(literal("\nimport PushService from \"../lib/alexandria-ui-elements/src/services/PushService\";\nimport FileService from \"../lib/alexandria-ui-elements/src/services/FileService\";\n\nvar launchApplication = function () {\n\tvar configuration = loadConfiguration();\n\n\twindow.Application = (function(configuration) {\n\t\tvar self = {};\n\n\t\tself.configuration = configuration;\n\t\tself.services = { pushService: PushService, fileService: FileService };\n\n\t\treturn self;\n\t})(configuration);\n\n\trenderApplication();\n    for (var i=0; i<configuration.pushServices.length; i++)\n        PushService.openConnection(configuration.pushServices")).add(expression().add(literal("i"))).add(literal(");\n\n\tfunction loadConfiguration() {\n\t\tvar configuration = {};\n\t\tconfiguration.googleApiKey = variable(\"GoogleApiKey\");\n\t\tconfiguration.pushServices = variable(\"PushServices\").split(\",\");\n\t\treturn configuration;\n\t}\n\n\tfunction variable(name) {\n\t\tvar result = document.getElementById(name).innerHTML;\n\t\tdocument.body.removeChild(document.getElementById(name));\n\t\treturn result;\n\t}\n\n\tfunction renderApplication() {\n\t\t")).add(mark("page", "render").multiple("\n\n")).add(literal("\n\t}\n};\n\nlaunchApplication();")),
			rule().add((condition("type", "page")), (condition("trigger", "import"))).add(literal("import ")).add(mark("value", "firstUpperCase")).add(literal(" from \"../src/pages/")).add(mark("value", "firstUpperCase")).add(literal("\";")),
			rule().add((condition("type", "page")), (condition("trigger", "render"))).add(literal("const ")).add(mark("value", "firstLowerCase")).add(literal(" = document.getElementById(\"")).add(mark("value", "firstUpperCase")).add(literal("\");\nif (")).add(mark("value", "firstLowerCase")).add(literal(") ReactDOM.render(<")).add(mark("value", "firstUpperCase")).add(literal(" />, ")).add(mark("value", "firstLowerCase")).add(literal(");"))
		);
		return this;
	}
}