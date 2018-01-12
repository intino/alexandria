package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class LayoutTemplate extends Template {

	protected LayoutTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new LayoutTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "layout"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\t")).add(mark("elementOption").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "elementOption & options"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic static String label(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, ")).add(mark("modelClass")).add(literal(" ")).add(mark("modelClass", "shortType", "firstLowerCase")).add(literal(") {\n\t\treturn null;\n\t}\n\n\tpublic static String icon(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, ")).add(mark("modelClass")).add(literal(" ")).add(mark("modelClass", "shortType", "firstLowerCase")).add(literal(") {\n\t\treturn null;\n\t}\n\n\tpublic static Integer bubble(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, ")).add(mark("modelClass")).add(literal(" ")).add(mark("modelClass", "shortType", "firstLowerCase")).add(literal(") {\n\t\treturn null;\n\t}\n\n\t")).add(mark("render")).add(literal("\n\n\t")).add(mark("elementOption").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "elementOption & group"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("elementOption").multiple("\n")).add(literal("\n\n\t")).add(mark("render")).add(literal("\n}")),
			rule().add((condition("type", "elementOption & option"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("elementOption").multiple("\n")).add(literal("\n\n\t")).add(mark("render")).add(literal("\n}")),
			rule().add((condition("type", "catalogs"))).add(literal("public static boolean filter(io.intino.konos.alexandria.activity.model.Catalog catalog, io.intino.konos.alexandria.activity.model.Element context, Object target, Object object, String username) {\n\treturn true;\n}")),
			rule().add((condition("type", "objects"))).add(literal("public static java.util.List<io.intino.konos.alexandria.activity.model.renders.RenderObjects.Source.Entry> objects(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String username) {\n\treturn java.util.Collections.emptyList();\n}")),
			rule().add((condition("type", "panels"))),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}