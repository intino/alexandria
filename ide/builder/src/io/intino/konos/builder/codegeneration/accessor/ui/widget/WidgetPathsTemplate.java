package io.intino.konos.builder.codegeneration.accessor.ui.widget;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class WidgetPathsTemplate extends Template {

	protected WidgetPathsTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetPathsTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "paths"))).add(literal("<link rel=\"import\" href=\"../../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../../lib/cotton-carrier/cotton-carrier-behavior.html\">\n<link rel=\"import\" href=\"../../../lib/app-route/app-route.html\">\n<link rel=\"import\" href=\"../../../lib/app-route/app-location.html\">\n\n<script src=\"requester-paths.js\"></script>\n\n<dom-module id=\"")).add(mark("name", "camelCaseToSnakeCase")).add(literal("-paths\">\n\n    <template>\n        <app-location route=\"{{route}}\"></app-location>\n        <app-route route=\"{{route}}\" pattern=\"/\" data=\"{{showHomeRouteData}}\"></app-route>\n\n        ")).add(mark("path").multiple("\n")).add(literal("\n    </template>\n\n    <style></style>\n\n    <script>\n\n        Polymer({\n            is: '")).add(mark("name", "camelCaseToSnakeCase")).add(literal("-paths',\n\n            behaviors: [ CottonBehaviors.CarrierBehavior, ")).add(mark("name", "firstuppercase")).add(literal("Behaviors.Requester ],\n\n            observers: [\n                '_pathChanged(route.path)'\n            ],\n\n            attached : function() {\n                var parent = $(this).parent().get(0);\n                this.display = parent.display;\n            },\n\n            setPath : function(path) {\n                this.set(\"route.path\", path);\n            },\n\n            _pathChanged : function(path) {\n                if (this.timeout != null)\n                    window.clearTimeout(this.timeout);\n\n                this.timeout = window.setTimeout(() => {\n                    if (path == \"/\") this.routeShowHome(this.display);\n                    ")).add(mark("path", "onChanged").multiple("\n")).add(literal("\n                }, 50);\n            },\n\t\t\t")).add(mark("path", "function").multiple(",\n")).add(literal("\n        });\n    </script>\n\n</dom-module>")),
			rule().add((condition("trigger", "onChanged"))).add(literal("else if (path.indexOf(\"")).add(mark("path", "subpath")).add(literal("\") != -1) this.route")).add(mark("request", "FirstUpperCase")).add(literal("(this.display, window.atob(path.replace(\"")).add(mark("path", "subpath")).add(literal("\",\"\")));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "function"))).add(literal(" ")).add(mark("request")).add(literal(" : function(value) {\n\tthis.set(\"route.path\", \"")).add(mark("path", "subpath")).add(literal("\" + window.btoa(value));\n}")),
			rule().add((condition("trigger", "function"))).add(literal(" ")).add(mark("request")).add(literal(" : function() {\n\tthis.set(\"route.path\", \"")).add(mark("path", "subpath")).add(literal("\");\n}")),
			rule().add((condition("trigger", "path"))).add(literal("<app-route route=\"{{route}}\" pattern=\"")).add(mark("path")).add(literal("\" data=\"{{")).add(mark("request")).add(literal("RouteData}}\"></app-route>"))
		);
		return this;
	}
}