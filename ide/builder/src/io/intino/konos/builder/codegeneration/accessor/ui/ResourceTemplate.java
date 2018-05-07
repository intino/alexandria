package io.intino.konos.builder.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ResourceTemplate extends Template {

	protected ResourceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ResourceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "resource"))).add(literal("<!DOCTYPE html>\n<html>\n    <head>\n        <title>$title</title>\n\n        <meta charset=\"utf-8\"/>\n        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"/>\n        <meta name=\"viewport\" content=\"width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes\"/>\n\n        <link rel=\"icon\" href-absolute=\"$favicon\">\n\n        <script src=\"lib/jquery/dist/jquery.min.js\"></script>\n        <script src=\"lib/cotton-push/PushService.js\"></script>\n        <script src=\"lib/moment/min/moment-with-locales.min.js\"></script>\n        <script src=\"lib/webcomponentsjs/webcomponents-lite.min.js\"></script> <!-- fix paper-calendar.html import bug -->\n        <script src=\"lib/numeral/min/numeral.min.js\"></script>\n        <script src=\"lib/numeral/min/languages.min.js\"></script>\n\n        <script src=\"main.js\"></script>\n\n        <link rel=\"import\" href=\"components.html\">\n        <link rel=\"import\" href=\"styles/app-theme.html\">\n        <script src=\"lib/moment/min/moment-with-locales.min.js\"></script> <!-- fix paper-calendar.html import bug -->\n\n\t\t<style>\n\t\t\thtml {\n\t\t\t\theight: 100%;\n\t\t\t}\n\t\t\tbody {\n\t\t\t\theight: calc(100% - 16px);\n\t\t\t}\n\t\t</style>\n    </head>\n\n    <body unresolved class=\"fullbleed\">\n        <template is=\"dom-bind\" id=\"app\">\n            <page-body base-url=\"$baseUrl\" url=\"$url\" client=\"$client\" language=\"$language\"></page-body>\n        </template>\n\n        <dom-module id=\"page-body\">\n\n            <template>\n                <cotton-zombie display=\"")).add(mark("uses")).add(literal("\" widget=\"")).add(mark("uses")).add(literal("\" language=\"$language\"></cotton-zombie>\n            </template>\n\n            <style>\n                :host {\n                    height: 100%;\n                }\n            </style>\n\n            <script>\n\t\t\t\tHTMLImports.whenReady(function() {\n\t\t\t\t\tPolymer({\n\t\t\t\t\t\tis: 'page-body'\n\t\t\t\t\t});\n\t\t\t\t});\n            </script>\n        </dom-module>\n\n        <script>\n            var configuration = {};\n            configuration.googleApiKey = \"$googleApiKey\";\n            configuration.pushServiceUrl = \"$pushUrl\";\n\n            launchApplication(configuration);\n        </script>\n    </body>\n</html>\n\n"))
		);
		return this;
	}
}