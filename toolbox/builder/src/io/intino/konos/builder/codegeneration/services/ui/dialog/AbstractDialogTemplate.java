package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractDialogTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("dialogdisplay"))).output(literal("package ")).output(mark("package")).output(literal(".dialogs;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.ui.model.Dialog;\nimport io.intino.alexandria.ui.displays.DialogValidator;\nimport io.intino.alexandria.ui.displays.AlexandriaDialog;\nimport io.intino.alexandria.ui.displays.AlexandriaDialogNotifier;\n\nimport java.util.Arrays;\n\npublic abstract class Abstract")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal(" extends AlexandriaDialog {\n\n\tpublic Abstract")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("(")).output(mark("box", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box box) {\n        super(box, buildDialog(box));\n    }\n\n    private static Dialog buildDialog(")).output(mark("box", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box box) {\n        Dialog dialog = new Dialog();\n        ")).output(mark("dialog")).output(literal("\n        return dialog;\n    }\n}")),
				rule().condition((type("dialog"))).output(expression().output(literal("dialog.name(\"")).output(mark("name")).output(literal("\");"))).output(literal("\n")).output(expression().output(literal("dialog.label(\"")).output(mark("label")).output(literal("\");"))).output(literal("\n")).output(expression().output(literal("dialog.description(\"")).output(mark("description")).output(literal("\");"))).output(literal("\n")).output(expression().output(mark("operation").multiple("\n"))).output(literal("\n")).output(mark("tab").multiple("\n")),
				rule().condition((type("tab"))).output(literal("Dialog.Tab w")).output(mark("name", "SnakeCaseToCamelCase")).output(literal(" = dialog.createTab(\"")).output(mark("label")).output(literal("\");\n")).output(mark("input").multiple("\n")),
				rule().condition((trigger("operation"))).output(literal("dialog.toolbar().createOperation().name(\"")).output(mark("name")).output(literal("\")")).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(literal(".closeAfterExecution(")).output(mark("closeAfterExecution")).output(literal(").execute((operation, session) -> ")).output(mark("dialog", "FirstUpperCase")).output(literal(".Toolbar.")).output(mark("execution")).output(literal("(box, dialog, operation, session));")),
				rule().condition((type("section"))).output(literal("Dialog.Tab.Section w")).output(mark("name", "SnakeCaseToCamelCase")).output(literal(" = (Dialog.Tab.Section) w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createSection()")).output(expression().output(mark("multiple"))).output(literal(";\nw")).output(mark("name", "SnakeCaseToCamelCase")).output(literal(".label(\"")).output(mark("label")).output(literal("\");\n")).output(mark("input").multiple("\n")),
				rule().condition((type("text"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createText()")).output(expression().output(literal(".edition(Dialog.TextEdition.")).output(mark("edition")).output(literal(")"))).output(expression().output(literal(".validation(")).output(mark("validation")).output(literal(")"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("memo"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createMemo()")).output(expression().output(literal(".mode(Dialog.MemoMode.")).output(mark("mode", "firstUpperCase")).output(literal(")"))).output(expression().output(literal(".height(")).output(mark("height")).output(literal(")"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("radio"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createRadioBox()")).output(expression().output(literal(".source(input -> java.util.Arrays.asList(")).output(mark("options", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(mark("source"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("check"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createCheckBox()")).output(expression().output(literal(".mode(Dialog.CheckBoxMode.")).output(mark("mode", "firstUpperCase")).output(literal(")"))).output(expression().output(literal(".source(input -> java.util.Arrays.asList(")).output(mark("options", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(mark("source"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("combo"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createComboBox()")).output(expression().output(literal(".source(input -> java.util.Arrays.asList(")).output(mark("options", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(mark("source"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("password"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createPassword()")).output(expression().output(literal(".validation(")).output(mark("validation")).output(literal(")"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("file"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createFile()")).output(expression().output(literal(".showPreview(")).output(mark("showPreview")).output(literal(")"))).output(expression().output(literal(".validation(")).output(mark("validation")).output(literal(")"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("picture"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createPicture()")).output(expression().output(literal(".showPreview(")).output(mark("showPreview")).output(literal(")"))).output(expression().output(literal(".validation(")).output(mark("validation")).output(literal(")"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("date"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createDate()")).output(expression().output(literal(".format(\"")).output(mark("format")).output(literal("\")"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((type("datetime"))).output(literal("w")).output(mark("owner", "SnakeCaseToCamelCase")).output(literal(".createDateTime()")).output(expression().output(literal(".format(\"")).output(mark("format")).output(literal("\")"))).output(expression().output(literal(".name(\"")).output(mark("name")).output(literal("\")"))).output(expression().output(literal(".label(\"")).output(mark("label")).output(literal("\")"))).output(expression().output(literal(".required(")).output(mark("required")).output(literal(")"))).output(expression().output(literal(".readonly(")).output(mark("readonly")).output(literal(")"))).output(expression().output(mark("multiple"))).output(expression().output(literal(".placeholder(\"")).output(mark("placeholder")).output(literal("\")"))).output(expression().output(literal(".helper(\"")).output(mark("helper")).output(literal("\")"))).output(expression().output(literal(".defaultValue(\"")).output(mark("defaultValue")).output(literal("\")"))).output(expression().output(mark("validator"))).output(literal(";")),
				rule().condition((allTypes("text", "validation"))).output(literal("new Dialog.Tab.Text.Validation()")).output(expression().output(literal(".allowedValues(java.util.Arrays.asList(")).output(mark("allowedValues", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(literal(".disallowedValues(java.util.Arrays.asList(")).output(mark("disallowedValues", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(literal(".disallowEmptySpaces(java.util.Arrays.asList(")).output(mark("disallowEmptySpaces", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(literal(".mask(")).output(mark("mask")).output(literal(")"))).output(expression().output(literal(".lenght(")).output(mark("min")).output(literal(",")).output(mark("max")).output(literal(")"))),
				rule().condition((allTypes("password", "validation"))).output(literal("new Dialog.Tab.Password.Validation()")).output(expression().output(literal(".allowedValues(java.util.Arrays.asList(")).output(mark("allowedValues", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(literal(".disallowedValues(java.util.Arrays.asList(")).output(mark("disallowedValues", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(literal(".disallowEmptySpaces(java.util.Arrays.asList(")).output(mark("disallowEmptySpaces", "quoted").multiple(", ")).output(literal("))"))).output(expression().output(literal(".mask(")).output(mark("mask")).output(literal(")"))).output(expression().output(literal(".lenght(")).output(mark("min")).output(literal(",")).output(mark("max")).output(literal(")"))),
				rule().condition((allTypes("resource", "validation"))).output(literal("new Dialog.Tab.Resource.Validation()")).output(expression().output(literal(".maxSize(")).output(mark("maxSize")).output(literal(")"))).output(expression().output(literal(".allowedExtensions(")).output(mark("allowedExtensions")).output(literal(")"))),
				rule().condition((type("validator"))).output(literal(".validator(input -> ")).output(mark("dialog", "FirstUpperCase")).output(literal(".Validators.")).output(mark("name")).output(literal("(box, (Dialog.Tab.")).output(mark("type")).output(literal(") input))")),
				rule().condition((trigger("source"))).output(literal(".source(input -> ")).output(mark("dialog", "FirstUpperCase")).output(literal(".Sources.")).output(mark("name")).output(literal("(box, (Dialog.Tab.")).output(mark("type")).output(literal(") input))")),
				rule().condition((type("multiple"))).output(literal(".multiple(")).output(mark("min")).output(literal(", ")).output(mark("max")).output(literal(")"))
		);
	}
}