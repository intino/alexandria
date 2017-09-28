package io.intino.konos.builder.codegeneration.services.activity.dialog;

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
			rule().add((condition("type", "dialogDisplay"))).add(literal("package ")).add(mark("package")).add(literal(".dialogs;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box;\nimport io.intino.konos.server.activity.dialogs.Dialog;\nimport io.intino.konos.server.activity.dialogs.DialogValidator;\nimport io.intino.konos.server.activity.dialogs.DialogDisplay;\n\nimport java.util.Arrays;\n\npublic abstract class ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("DialogDisplay extends DialogDisplay {\n\n    public ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("DialogDisplay(")).add(mark("box", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box box) {\n        super(box, buildDialog(box));\n    }\n\n    private static Dialog buildDialog(")).add(mark("box", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box box) {\n        Dialog dialog = new Dialog();\n        ")).add(mark("dialog")).add(literal("\n        return dialog;\n    }\n}")),
			rule().add((condition("type", "dialog"))).add(expression().add(literal("dialog.label(\"")).add(mark("label")).add(literal("\");"))).add(literal("\n")).add(expression().add(literal("dialog.description(\"")).add(mark("description")).add(literal("\");"))).add(expression().add(literal("\n")).add(mark("operation").multiple("\n"))).add(literal("\n")).add(mark("tab").multiple("\n")),
			rule().add((condition("type", "tab"))).add(literal("Dialog.Tab w")).add(mark("name", "SnakeCaseToCamelCase")).add(literal(" = dialog.createTab(\"")).add(mark("label")).add(literal("\");\n")).add(mark("input").multiple("\n")),
			rule().add((condition("trigger", "operation"))).add(literal("dialog.toolbar().createOperation()")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(".execute(operation -> ")).add(mark("dialog", "FirstUpperCase")).add(literal("Dialog.Toolbar.")).add(mark("execution")).add(literal("(box, dialog, operation));")),
			rule().add((condition("type", "section"))).add(literal("Dialog.Tab.Section w")).add(mark("name", "SnakeCaseToCamelCase")).add(literal(" = (Dialog.Tab.Section) w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createSection()")).add(expression().add(mark("multiple"))).add(literal(";\nw")).add(mark("name", "SnakeCaseToCamelCase")).add(literal(".label(\"")).add(mark("label")).add(literal("\");\n")).add(mark("input").multiple("\n")),
			rule().add((condition("type", "text"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createText()")).add(expression().add(literal(".edition(Dialog.TextEdition.")).add(mark("edition")).add(literal(")"))).add(expression().add(literal(".validation(")).add(mark("validation")).add(literal(")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "memo"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createMemo()")).add(expression().add(literal(".mode(Dialog.MemoMode.")).add(mark("mode", "firstUpperCase")).add(literal(")"))).add(expression().add(literal(".height(")).add(mark("height")).add(literal(")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "radio"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createRadioBox()")).add(expression().add(literal(".source(input -> java.util.Arrays.asList(")).add(mark("options", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(mark("source"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "check"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createCheckBox()")).add(expression().add(literal(".mode(Dialog.CheckBoxMode.")).add(mark("mode", "firstUpperCase")).add(literal(")"))).add(expression().add(literal(".source(input -> java.util.Arrays.asList(")).add(mark("options", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(mark("source"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "combo"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createComboBox()")).add(expression().add(literal(".source(input -> java.util.Arrays.asList(")).add(mark("options", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(mark("source"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "password"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createPassword()")).add(expression().add(literal(".validation(")).add(mark("validation")).add(literal(")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "file"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createFile()")).add(expression().add(literal(".showPreview(")).add(mark("showPreview")).add(literal(")"))).add(expression().add(literal(".validation(")).add(mark("validation")).add(literal(")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "picture"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createPicture()")).add(expression().add(literal(".showPreview(")).add(mark("showPreview")).add(literal(")"))).add(expression().add(literal(".validation(")).add(mark("validation")).add(literal(")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "date"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createDate()")).add(expression().add(literal(".format(\"")).add(mark("format")).add(literal("\")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "dateTime"))).add(literal("w")).add(mark("owner", "SnakeCaseToCamelCase")).add(literal(".createDateTime()")).add(expression().add(literal(".format(\"")).add(mark("format")).add(literal("\")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".required(")).add(mark("required")).add(literal(")"))).add(expression().add(literal(".readonly(")).add(mark("readonly")).add(literal(")"))).add(expression().add(mark("multiple"))).add(expression().add(literal(".placeholder(\"")).add(mark("placeholder")).add(literal("\")"))).add(expression().add(literal(".helper(\"")).add(mark("helper")).add(literal("\")"))).add(expression().add(literal(".defaultValue(\"")).add(mark("defaultValue")).add(literal("\")"))).add(expression().add(mark("validator"))).add(literal(";")),
			rule().add((condition("type", "validation & text"))).add(literal("new Dialog.Tab.Text.Validation()")).add(expression().add(literal(".allowedValues(java.util.Arrays.asList(")).add(mark("allowedValues", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(literal(".disallowedValues(java.util.Arrays.asList(")).add(mark("disallowedValues", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(literal(".disallowEmptySpaces(java.util.Arrays.asList(")).add(mark("disallowEmptySpaces", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(literal(".mask(")).add(mark("mask")).add(literal(")"))).add(expression().add(literal(".lenght(")).add(mark("min")).add(literal(",")).add(mark("max")).add(literal(")"))),
			rule().add((condition("type", "validation & password"))).add(literal("new Dialog.Tab.Password.Validation()")).add(expression().add(literal(".allowedValues(java.util.Arrays.asList(")).add(mark("allowedValues", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(literal(".disallowedValues(java.util.Arrays.asList(")).add(mark("disallowedValues", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(literal(".disallowEmptySpaces(java.util.Arrays.asList(")).add(mark("disallowEmptySpaces", "quoted").multiple(", ")).add(literal("))"))).add(expression().add(literal(".mask(")).add(mark("mask")).add(literal(")"))).add(expression().add(literal(".lenght(")).add(mark("min")).add(literal(",")).add(mark("max")).add(literal(")"))),
			rule().add((condition("type", "validation & resource"))).add(literal("new Dialog.Tab.Resource.Validation()")).add(expression().add(literal(".maxSize(")).add(mark("maxSize")).add(literal(")"))).add(expression().add(literal(".allowedExtensions(")).add(mark("allowedExtensions")).add(literal(")"))),
			rule().add((condition("type", "validator"))).add(literal(".validator(input -> ")).add(mark("dialog", "FirstUpperCase")).add(literal("Dialog.Validators.")).add(mark("name")).add(literal("(box, (Dialog.Tab.")).add(mark("type")).add(literal(") input))")),
			rule().add((condition("trigger", "source"))).add(literal(".source(input -> ")).add(mark("dialog", "FirstUpperCase")).add(literal("Dialog.Sources.")).add(mark("name")).add(literal("(box, (Dialog.Tab.")).add(mark("type")).add(literal(") input))")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "multiple"))).add(literal(".multiple(")).add(mark("min")).add(literal(", ")).add(mark("max")).add(literal(")"))
		);
		return this;
	}
}