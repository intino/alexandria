package io.intino.konos.builder.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DialogWidgetTemplate extends Template {

	protected DialogWidgetTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DialogWidgetTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "dialog"))).add(literal("<link rel=\"import\" href=\"../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../lib/iron-flex-layout/iron-flex-layout.html\">\n<link rel=\"import\" href=\"../../lib/cotton-push/cotton-push-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-carrier/cotton-carrier-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-zombie/cotton-zombie.html\">\n<link rel=\"import\" href=\"../../lib/cotton-translator/cotton-translator-behavior.html\">\n\n<script src=\"../../lib/konos-server-web/dialogs/requester.js\"></script>\n<script src=\"../../lib/konos-server-web/dialogs/notifier-listener.js\"></script>\n\n<dom-module id=\"")).add(mark("name", "lowercase")).add(literal("-widget\">\n\n\t<template>\n\t\t<iframe id=\"frame\" class=\"dialog\" src=\"[[delegateUrl]]\"></iframe>\n\t</template>\n\n\t<style>\n\t\t:host iframe {\n\t\t\tborder: 0;\n\t\t\toverflow: auto;\n\t\t\twidth: 100%;\n\t\t\theight: 100%;\n\t\t\tborder-radius: 2px;\n\t\t}\n\t</style>\n\n\t<script>\n\t\tconst DialogDelegate = \"lib/konos-server-web/dialogs/alpaca.html\";\n\n\t\tconst ")).add(mark("name", "firstUpperCase")).add(literal("WidgetDictionary = {\n\t\t\tes: {\n\t\t\t},\n\t\t\ten: {\n\t\t\t}\n\t\t};\n\n\t\tPolymer({\n\t\t\tis: '")).add(mark("name", "lowercase")).add(literal("-widget',\n\n\t\t\tbehaviors: [ CottonBehaviors.CarrierBehavior,\n\t\t\t\tCottonBehaviors.PushBehavior,\n\t\t\t\tCottonBehaviors.TranslatorBehavior,\n\t\t\t\tKonosDialogWidgetBehaviors.Requester,\n\t\t\t\tKonosDialogWidgetBehaviors.NotifierListener ],\n\n\t\t\tproperties : {\n\t\t\t\t_dialog : Object,\n\t\t\t\tdelegateUrl : String\n\t\t\t},\n\n\t\t\tattached : function() {\n\t\t\t\tthis.translate(")).add(mark("name", "firstUpperCase")).add(literal("WidgetDictionary);\n\t\t\t\tthis.listenToDisplay();\n\t\t\t\tthis._initDelegate();\n\t\t\t},\n\n\t\t\t_initDelegate : function() {\n\t\t\t\tthis.delegateUrl = this._getUrl() + \"/\" + DialogDelegate + \"?random=\" + Math.random();\n\t\t\t\tthis.$.frame.onload = this._loadDelegate.bind(this);\n\t\t\t},\n\n\t\t\t_render : function(dialog) {\n\t\t\t\tthis._dialog = dialog;\n\t\t\t\tthis._loadDelegate();\n\t\t\t},\n\n\t\t\t_refresh: function(validation) {\n\t\t\t\tif (this.delegate == null) return;\n\t\t\t\tvar modifiedInputs = JSON.parse(validation.modifiedInputs);\n\t\t\t\tthis.delegate.sendMessage(validation.input, validation.status, validation.message);\n\t\t\t\tthis.delegate.refresh(modifiedInputs);\n\t\t\t\tif (modifiedInputs.length > 0)\n\t\t\t\t\tthis.delegate.focus(validation.input);\n\t\t\t},\n\n\t\t\t_configuration : function() {\n\t\t\t\treturn {\n\t\t\t\t\tlanguage: this.getLanguage(),\n\t\t\t\t\tresourceManager: this\n\t\t\t\t}\n\t\t\t},\n\n\t\t\t_inputChanged : function(input, value) {\n\t\t\t\tthis.update({ name: input.label, value: value });\n\t\t\t},\n\n\t\t\t_inputResourceRemoved : function(input, position) {\n\t\t\t\tthis.removeResource({ input: input.label, position: position });\n\t\t\t},\n\n\t\t\t_operationExecuted : function(operation, parameters) {\n\t\t\t\tthis.execute();\n\t\t\t},\n\n\t\t\t_getUrl: function() {\n\t\t\t\tvar url = this.getProperty(\"url\");\n\t\t\t\tif (url.charAt(url.length-1) == \"/\")\n\t\t\t\t\turl = url.substring(0, url.length-1);\n\t\t\t\treturn url;\n\t\t\t},\n\n\t\t\tgetProperty: function(name) {\n\t\t\t\tvar widget = this;\n\n\t\t\t\twhile (widget != null && widget.getAttribute(name) == null)\n\t\t\t\t\twidget = widget.parentElement;\n\n\t\t\t\tif (widget == null)\n\t\t\t\t\treturn \"\";\n\n\t\t\t\treturn widget.getAttribute(name);\n\t\t\t},\n\n\t\t\t_loadDelegate : function() {\n\t\t\t\tif (this._dialog == null) return;\n\n\t\t\t\tvar frame = this.$.frame;\n\t\t\t\tvar delegate = (frame.contentWindow || frame.contentDocument).document;\n\t\t\t\tif (!delegate.render) return;\n\n\t\t\t\tthis.delegate = delegate;\n\t\t\t\tthis.delegate.render(JSON.parse(this._dialog.definition), this._configuration());\n\t\t\t\tthis.delegate.onInputChange = this._inputChanged.bind(this);\n\t\t\t\tthis.delegate.onRemoveResource = this._inputResourceRemoved.bind(this);\n\t\t\t\tthis.delegate.onOperation = this._operationExecuted.bind(this);\n\t\t\t}\n\n\n\t\t});\n\t</script>\n</dom-module>"))
		);
		return this;
	}
}