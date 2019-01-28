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
			rule().add((condition("type", "app"))).add(literal("import React from \"react\";\nimport ReactDOM from \"react-dom\";\n")).add(mark("page", "import").multiple("\n")).add(literal("\n\nvar launchApplication = function (configuration) {\n\n\twindow.Application = (function (configuration) {\n\t\tvar self = {};\n\n\t\tself.configuration = configuration;\n\t\tself.services = { pushService: PushService, uploadService: UploadService };\n\n\t\treturn self;\n\t})(configuration);\n\n\twindow.addEventListener('WebComponentsReady', function () {\n\t\trenderApp();\n\t\tPushService.openConnection(configuration.pushServiceUrl);\n\t});\n\n\tfunction renderApp() {\n\t\t")).add(mark("page", "render").multiple("\n\n")).add(literal("\n\t}\n};")),
			rule().add((condition("type", "page")), (condition("trigger", "import"))).add(literal("import ")).add(mark("value", "firstUpperCase")).add(literal(" from \"../src/pages/")).add(mark("value", "firstUpperCase")).add(literal("\";")),
			rule().add((condition("type", "page")), (condition("trigger", "render"))).add(literal("const ")).add(mark("value", "firstLowerCase")).add(literal(" = document.getElementById(\"")).add(mark("value", "firstUpperCase")).add(literal("\");\nif (")).add(mark("value", "firstLowerCase")).add(literal(") ReactDOM.render(<")).add(mark("value", "firstUpperCase")).add(literal(" />, homePage);"))
		);
		return this;
	}
}