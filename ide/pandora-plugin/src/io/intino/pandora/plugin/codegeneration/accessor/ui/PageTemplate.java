package io.intino.pandora.plugin.codegeneration.accessor.ui;

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
			rule().add((condition("type", "page"))).add(literal("<!DOCTYPE html>\n<html>\n    <head>\n        <title>$title</title>\n\n        <meta charset=\"utf-8\"/>\n        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"/>\n        <meta name=\"viewport\" content=\"width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes\"/>\n\n        <link rel=\"icon\" type=\"image/png\" href=\"images/favicon.ico\">\n\n        <script src=\"lib/jquery/dist/jquery.min.js\"></script>\n        <script src=\"lib/cotton-push/PushService.js\"></script>\n        <script src=\"lib/webcomponentsjs/webcomponents-lite.min.js\"></script>\n        <script src=\"lib/moment/min/moment-with-locales.min.js\"></script>\n\n        <script src=\"main.js\"></script>\n\n        <link rel=\"import\" href=\"components.html\">\n    </head>\n\n    <body unresolved class=\"fullbleed\">\n        <template is=\"dom-bind\" id=\"app\">\n            <page-body base-url=\"$baseUrl\" url=\"$url\" client=\"$client\" language=\"$language\" title=\"$title\"></page-body>\n        </template>\n\n        <dom-module id=\"page-body\">\n\n            <template>\n                <cotton-zombie display=\"")).add(mark("uses")).add(literal("Display\" widget=\"")).add(mark("uses")).add(literal("Widget\" title=\"$title\"></cotton-zombie>\n            </template>\n\n            <style>\n                :host {\n                    height: 100%;\n                }\n            </style>\n\n            <script>\n\t\t\t\tHTMLImports.whenReady(function() {\n\t\t\t\t\tPolymer({\n\t\t\t\t\t\tis: 'page-body'\n\t\t\t\t\t});\n\t\t\t\t});\n            </script>\n        </dom-module>\n\n        <script>\n            var configuration = {};\n            configuration.pushServiceUrl = \"$pushUrl\";\n\n            launchApplication(configuration);\n        </script>\n    </body>\n</html>\n\n"))
		);
		return this;
	}
}