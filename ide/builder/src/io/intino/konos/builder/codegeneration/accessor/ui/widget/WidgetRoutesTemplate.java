package io.intino.konos.builder.codegeneration.accessor.ui.widget;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class WidgetRoutesTemplate extends Template {

	protected WidgetRoutesTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetRoutesTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "routes"))).add(literal("<link rel=\"import\" href=\"../../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../../lib/cotton-carrier/cotton-carrier-behavior.html\">\n<link rel=\"import\" href=\"../../../lib/alexandria-ui-elements/_common/alexandria-configuration-behavior.html\">\n<link rel=\"import\" href=\"../../../lib/app-route/app-route.html\">\n<link rel=\"import\" href=\"../../../lib/app-route/app-location.html\">\n\n<script src=\"requester.js\"></script>\n\n<dom-module id=\"")).add(mark("name", "camelCaseToSnakeCase")).add(literal("-routes\">\n\n    <template>\n        <app-location route=\"{{route}}\"></app-location>\n        <app-route route=\"{{route}}\" pattern=\"{_path}/\" data=\"{{showHomeRouteData}}\"></app-route>\n        ")).add(mark("route", "define").multiple("\n")).add(literal("\n    </template>\n\n    <style></style>\n\n    <script>\n\n        Polymer({\n            is: '")).add(mark("name", "camelCaseToSnakeCase")).add(literal("-routes',\n\n            behaviors: [ CottonBehaviors.CarrierBehavior, ")).add(mark("name", "firstuppercase")).add(literal("Behaviors.Requester, AlexandriaBehaviors.ConfigurationBehavior ],\n\n            observers: [\n                '_pathChanged(route.path)'\n            ],\n\n\t\t\tproperties : {\n\t\t\t\t_path : String\n\t\t\t},\n\n            attached : function() {\n                var parent = $(this).parent().get(0);\n                this.display = parent.display;\n                this._path = this.getBasePath();\n            },\n\n            setPath : function(path) {\n                this.set(\"route.path\", path);\n            },\n\n            _pathChanged : function(path) {\n                if (this.timeout != null)\n                    window.clearTimeout(this.timeout);\n\n                this.timeout = window.setTimeout(() => {\n                    if (path == \"/\") this.routeShowHome(this.display);\n                    ")).add(mark("route", "onChanged").multiple("\n")).add(literal("\n                }, 50);\n            },\n\n\t\t\t_isRoute : function(path, match) {\n\t\t\t\treturn path.replace(match, \"\").indexOf(\"/\") == -1;\n\t\t\t},\n\n\t\t\t_samePath : function(path) {\n\t\t\t\treturn path == this.route.path;\n\t\t\t},\n\n\t\t\t")).add(mark("route", "function").multiple(",\n")).add(literal("\n        });\n    </script>\n\n</dom-module>")),
			rule().add((condition("trigger", "onChanged"))).add(literal("else if (!this._samePath(path) && this._isRoute(path, this._path + \"")).add(mark("value", "subpath")).add(literal("\")) this.route")).add(mark("request", "FirstUpperCase")).add(literal("(this.display, ")).add(mark("routePath", "call")).add(literal(");")),
			rule().add((condition("type", "parameter")), (condition("trigger", "function"))).add(literal(" ")).add(mark("request")).add(literal(" : function(value) {\n\tvar path = ")).add(mark("routePath", "register")).add(literal(";\n\tif (this._samePath(path)) this.route")).add(mark("request", "FirstUpperCase")).add(literal("(this.display, path);\n\tthis.set(\"route.path\", path);\n}")),
			rule().add((condition("trigger", "function"))).add(literal(" ")).add(mark("request")).add(literal(" : function() {\n\tvar path = this._path + \"")).add(mark("value", "subpath")).add(literal("\";\n\tif (this._samePath(path)) this.route")).add(mark("request", "FirstUpperCase")).add(literal("(this.display);\n\tthis.set(\"route.path\", path);\n}")),
			rule().add((condition("type", "route")), (condition("trigger", "define"))).add(literal("<app-route route=\"{{route}}\" pattern=\"{_path}")).add(mark("value")).add(literal("\" data=\"{{")).add(mark("request")).add(literal("RouteData}}\"></app-route>")),
			rule().add((condition("type", "routePath & encoded")), (condition("trigger", "call"))).add(literal("window.atob(path.replace(this._path + \"")).add(mark("value", "subpath")).add(literal("enc:\",\"\"))")),
			rule().add((condition("type", "routePath & encoded")), (condition("trigger", "register"))).add(literal("this.path + \"")).add(mark("value", "subpath")).add(literal("\" + \"enc:\" + window.btoa(value)")),
			rule().add((condition("type", "routePath")), (condition("trigger", "call"))).add(literal("path.replace(this._path + \"")).add(mark("value", "subpath")).add(literal("\",\"\")")),
			rule().add((condition("type", "routePath")), (condition("trigger", "register"))).add(literal("this._path + \"")).add(mark("value", "subpath")).add(literal("\" + value"))
		);
		return this;
	}
}