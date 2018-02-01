package io.intino.konos.builder.codegeneration.services.activity.dialog;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DialogsTemplate extends Template {

	protected DialogsTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DialogsTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "Dialogs"))).add(literal("package ")).add(mark("package")).add(literal(".dialogs;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class Dialogs {\n\tprivate static Map<String, DialogBuilder> dialogMap = new HashMap<>();\n\n\tstatic {\n\t\t{\n\t\t\t")).add(mark("dialog").multiple("\n")).add(literal("\n\t\t}\n\t}\n\n\tpublic static AlexandriaDialog dialogFor(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String name) {\n\t\tif (!dialogMap.containsKey(name)) return null;\n\t\treturn dialogMap.get(name).build(box);\n\t}\n\n\tprivate interface DialogBuilder {\n\t\tAlexandriaDialog build(")).add(mark("box", "firstUpperCase")).add(literal("Box box);\n\t}\n}")),
			rule().add((condition("trigger", "dialog"))).add(literal("dialogMap.put(\"")).add(mark("name")).add(literal("\", (box) -> new ")).add(mark("name", "FirstUpperCase")).add(literal("(box));"))
		);
		return this;
	}
}