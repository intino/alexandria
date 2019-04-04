package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DatasourceTemplate extends Template {

	protected DatasourceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DatasourceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "datasource & abstract"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ui.sources;\n\nimport io.intino.alexandria.ui.model.Datasource;\nimport io.intino.alexandria.ui.model.datasource.Category;\n\nimport java.util.List;\n\npublic abstract class Abstract")).add(mark("name", "firstUpperCase")).add(literal(" extends DataSource<")).add(mark("modelClass")).add(literal("> {\n\n\tpublic Abstract")).add(mark("name", "firstUpperCase")).add(literal("() {\n\t\t")).add(mark("grouping", "constructor").multiple("\n")).add(literal("\n\t\t")).add(mark("sorting", "constructor").multiple("\n")).add(literal("\n\t}\n\n\tpublic abstract List<")).add(mark("modelClass")).add(literal("> items(int start, int limit, String condition);\n\t")).add(mark("grouping").multiple("\n")).add(literal("\n\t")).add(mark("sorting").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "datasource"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ui.sources;\n\nimport io.intino.alexandria.ui.model.Datasource;\nimport io.intino.alexandria.ui.model.datasource.Category;\nimport io.intino.alexandria.ui.model.datasource.Group;\n\nimport java.util.List;\n\npublic abstract class ")).add(mark("name", "firstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "firstUpperCase")).add(literal(" {\n\n\t@Override\n\tpublic List<Item> items(int start, int limit, String condition) {\n\t\treturn null;\n\t}\n\n\t")).add(mark("grouping").multiple("\n")).add(literal("\n\t")).add(mark("sorting").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "grouping & abstract")), (condition("trigger", "constructor"))).add(literal("add(")).add(mark("name", "firstLowerCase")).add(literal("());")),
			rule().add((condition("type", "grouping & abstract"))).add(literal("public abstract Grouping<")).add(mark("modelClass")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(literal("();")),
			rule().add((condition("type", "sorting & abstract")), (condition("trigger", "constructor"))).add(literal("add(")).add(mark("name", "firstLowerCase")).add(literal("());")),
			rule().add((condition("type", "sorting & abstract"))).add(literal("public abstract Sorting<")).add(mark("modelClass")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(literal("();")),
			rule().add((condition("type", "grouping"))).add(literal("@Override\npublic Grouping<")).add(mark("modelClass")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(literal("() {\n\treturn null;\n}")),
			rule().add((condition("type", "sorting"))).add(literal("@Override\npublic Sorting<")).add(mark("modelClass")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(literal("() {\n\treturn null;\n}"))
		);
		return this;
	}
}