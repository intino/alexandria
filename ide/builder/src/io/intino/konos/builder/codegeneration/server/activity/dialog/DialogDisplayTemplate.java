package io.intino.konos.builder.codegeneration.server.activity.dialog;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DialogDisplayTemplate extends Template {

	protected DialogDisplayTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DialogDisplayTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "dialog"))).add(literal("package ")).add(mark("package")).add(literal(".dialogs;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport io.intino.konos.server.activity.dialogs.Dialog;\nimport io.intino.konos.server.activity.dialogs.DialogValidator;\nimport io.intino.konos.server.activity.dialogs.DialogDisplay;\n\nimport java.util.Arrays;\n\npublic class SolicitudDialogDisplay extends DialogDisplay {\n\n    public SolicitudDialogDisplay(KonosDialogsBox box) {\n        super(box, buildDialog());\n    }\n\n    private static Dialog buildDialog() {\n        Dialog dialog = new Dialog();\n        ")).add(mark("dialog")).add(literal("\n        return dialog;\n    }\n\n}")),
			rule().add((condition("type", "dialog"))).add(expression().add(literal("dialog.label(\"")).add(mark("label")).add(literal("\");"))).add(literal("\n")).add(expression().add(literal("dialog.description(\"")).add(mark("description")).add(literal("\");"))).add(literal("\n")).add(mark("tab").multiple("\n")).add(literal("\n\nDialog.Tab tab2 = dialog.createTab(\"Dirección\");\ntab2.createRadioBox().source((input) -> Arrays.asList(\"X\", \"Y\")).label(\"Tipo de vía pública\");\ntab2.createText().label(\"Nombre de vía pública\");\ntab2.createText().label(\"Número de vía pública\");\ntab2.createText().label(\"Portal\");\ntab2.createText().label(\"Escalera\");\ntab2.createText().label(\"Piso\");\ntab2.createText().label(\"Puerta\");\ntab2.createText().label(\"Código postal\");\ntab2.createComboBox().source((input) -> Arrays.asList(\"Las Palmas\", \"Santa Cruz\")).label(\"Provincia\").validator((input) -> SolicitudDialog.Validators.validaProvincia((Dialog.Tab.ComboBox)input));\ntab2.createComboBox().source((input) -> SolicitudDialog.Sources.islas((Dialog.Tab.ComboBox)input)).label(\"Isla\").validator((input) -> SolicitudDialog.Validators.validaIsla((Dialog.Tab.ComboBox)input));\ntab2.createComboBox().source((input) -> SolicitudDialog.Sources.municipios((Dialog.Tab.ComboBox)input)).label(\"Municipio\").validator((input) -> SolicitudDialog.Validators.validaMunicipio((Dialog.Tab.ComboBox)input));\ntab2.createComboBox().source((input) -> SolicitudDialog.Sources.localidades((Dialog.Tab.ComboBox)input)).label(\"Localidad\");\ntab2.createCheckBox().mode(Dialog.CheckBoxMode.Boolean).source((input) -> Arrays.asList(\"*Sí\", \"No\")).label(\"Usar esta dirección como dirección de notificación\");")),
			rule().add((condition("type", "tab"))).add(literal("Dialog.Tab ")).add(mark("name")).add(literal(" = dialog.createTab(\"")).add(mark("label")).add(literal("\");\n")).add(mark("text").multiple("\n")).add(literal("\n")).add(mark("section").multiple("\n")).add(literal("\n")).add(mark("memo").multiple("\n")).add(literal("\n")).add(mark("radio").multiple("\n")).add(literal("\n")).add(mark("check").multiple("\n")).add(literal("\n")).add(mark("combo").multiple("\n")).add(literal("\n")).add(mark("password").multiple("\n")).add(literal("\n")).add(mark("file").multiple("\n")).add(literal("\n")).add(mark("picture").multiple("\n")).add(literal("\n")).add(mark("date").multiple("\n")).add(literal("\n")).add(mark("dateTime").multiple("\n")),
			rule().add((condition("type", "text"))).add(literal(".createText().label(\"NIF\").defaultValue(\"43222133F\").validator((input) -> SolicitudDialog.Validators.validaNif((Dialog.Tab.Text)input));")),
			rule().add((condition("type", "section"))),
			rule().add((condition("type", "memo"))),
			rule().add((condition("type", "radio"))),
			rule().add((condition("type", "check"))),
			rule().add((condition("type", "combo"))),
			rule().add((condition("type", "password"))),
			rule().add((condition("type", "file"))),
			rule().add((condition("type", "picture"))),
			rule().add((condition("type", "date"))),
			rule().add((condition("type", "dateTime")))
		);
		return this;
	}
}