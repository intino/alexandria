package io.intino.konos.builder.codegeneration.accessor.ui.widget;

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
			rule().add((condition("type", "dialog"))).add(literal("<link rel=\"import\" href=\"../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../lib/cotton-push/cotton-push-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-carrier/cotton-carrier-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-zombie/cotton-zombie.html\">\n<link rel=\"import\" href=\"../../lib/cotton-translator/cotton-translator-behavior.html\">\n\n<script src=\"../../lib/alexandria-ui-elements/dialogs/alexandria-dialog-requester.js\"></script>\n<script src=\"../../lib/alexandria-ui-elements/dialogs/alexandria-dialog-notifier-listener.js\"></script>\n<script src=\"../../lib/alexandria-ui-elements/dialogs/alexandria-dialog-behavior.js\"></script>\n\n<dom-module id=\"")).add(mark("name", "camelCaseToSnakeCase", "lowercase")).add(literal("\">\n\n\t<template>\n\t\t<iframe id=\"frame\" class=\"dialog\" src=\"[[delegateUrl]]\"></iframe>\n\t</template>\n\n\t<style>\n\t\t:host {\n\t\t\tdisplay: block;\n\t\t\theight: calc(100% - 2px);\n\t\t}\n\t\t:host iframe {\n\t\t\tborder: 0;\n\t\t\toverflow: auto;\n\t\t\twidth: 100%;\n\t\t\theight: calc(100% - 2px);\n\t\t}\n\t</style>\n\n\t<script>\n\t\tconst ")).add(mark("name", "firstUpperCase")).add(literal("Dictionary = {\n\t\t\tes: {\n\t\t\t},\n\t\t\ten: {\n\t\t\t}\n\t\t};\n\n\t\tPolymer({\n\t\t\tis: '")).add(mark("name", "camelCaseToSnakeCase", "lowercase")).add(literal("',\n\n\t\t\tbehaviors: [ CottonBehaviors.CarrierBehavior,\n\t\t\t\tCottonBehaviors.PushBehavior,\n\t\t\t\tCottonBehaviors.TranslatorBehavior,\n\t\t\t\tAlexandriaDialogBehaviors.Requester,\n\t\t\t\tAlexandriaDialogBehaviors.NotifierListener,\n\t\t\t\tAlexandriaDialogBehaviors.DialogBehavior ],\n\n\t\t\tattached : function() {\n\t\t\t\tthis.translate(")).add(mark("name", "firstUpperCase")).add(literal("Dictionary);\n\t\t\t\tthis.listenToDisplay();\n\t\t\t\tthis._initDelegate();\n\t\t\t}\n\n\t\t});\n\t</script>\n</dom-module>"))
		);
		return this;
	}
}