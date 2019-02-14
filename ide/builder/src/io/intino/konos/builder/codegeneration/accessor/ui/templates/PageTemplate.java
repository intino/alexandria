package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PageTemplate extends Template {

	protected PageTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PageTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "resource & js"))).add(literal("import React from \"react\";\nimport Page from \"../../lib/alexandria-ui-elements/src/displays/Page\";\nimport ")).add(mark("pageDisplay", "firstUpperCase")).add(literal(" from \"")).add(mark("pageDisplayOrigin", "origin")).add(literal("/displays/")).add(mark("pageDisplayType", "firstLowerCase")).add(literal("s/")).add(mark("pageDisplay", "firstUpperCase")).add(literal("\";\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal(" extends Page {\nrender() {\n    return <")).add(mark("pageDisplay", "firstUpperCase")).add(literal(" id=\"")).add(mark("id")).add(literal("\"></")).add(mark("pageDisplay", "firstUpperCase")).add(literal(">;\n}\n}")),
			rule().add((condition("type", "resource & html"))).add(literal("<!DOCTYPE html>\n<html>\n\t<head>\n        <title>$title</title>\n\n\t\t<meta charset=\"utf-8\"/>\n        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"/>\n        <meta name=\"viewport\" content=\"width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes\"/>\n\n        <link rel=\"icon\" href-absolute=\"$favicon\">\n\n\t</head>\n\n\t<body>\n\t\t<div id=\"")).add(mark("name", "firstUpperCase")).add(literal("\" base-url=\"$baseUrl\" base-path=\"$basePath\" url=\"$url\" client=\"$client\" language=\"$language\"></div>\n\t\t<script>\n\t\t\tdocument.configuration = {};\n\t\t\tdocument.configuration.googleApiKey = \"$googleApiKey\";\n\t\t\tdocument.configuration.pushServices = \"$pushUrls\".split(\",\");\n\t\t</script>\n\t</body>\n</html>")),
			rule().add((condition("attribute", "src")), (condition("trigger", "origin"))).add(literal("./")),
			rule().add((condition("trigger", "origin"))).add(literal("../../gen")),
			rule().add((condition("type", "polymer & editor"))).add(literal("Polymer({\n\tis: 'page-body',\n\n\tattached : function() {\n\t\tthis._initEditor();\n\t},\n\n\t_initEditor : function() {\n\t\tvar widget = this;\n\t\tthis.$.root.whenPersonified().execute(function() {\n\t\t\tvar editor = widget.querySelector(\"alexandria-editor\");\n\n\t\t\teditor.onSaved = function() {\n\t\t\t\tif (document.onSaved) document.onSaved();\n\t\t\t};\n\n\t\t\tdocument.save = function() {\n\t\t\t\teditor.save();\n\t\t\t}\n\t\t});\n\t}\n});"))
		);
		return this;
	}
}