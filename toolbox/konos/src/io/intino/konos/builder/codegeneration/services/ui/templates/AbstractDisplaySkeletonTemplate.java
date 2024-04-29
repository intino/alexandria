package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractDisplaySkeletonTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("display", "accessible"))).output(literal("package ")).output(mark("package")).output(literal(".ui.displays")).output(expression().output(literal(".")).output(mark("packageType")).output(literal("s"))).output(literal(";\n\n")).output(mark("templatesImport")).output(literal("\n")).output(mark("tablesImport")).output(literal("\n")).output(mark("listsImport")).output(literal("\n")).output(mark("magazinesImport")).output(literal("\n")).output(mark("mapsImport")).output(literal("\n")).output(mark("dynamicTablesImport")).output(literal("\n")).output(mark("blocksImport")).output(literal("\n")).output(mark("itemsImport")).output(literal("\nimport ")).output(mark("package", "validPackage")).output(literal(".ui.displays.notifiers.")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("ProxyNotifier;\nimport io.intino.alexandria.ui.displays.ProxyDisplay;\nimport io.intino.alexandria.ui.services.push.UISession;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Proxy extends ProxyDisplay<")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("ProxyNotifier> {\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Proxy(io.intino.alexandria.ui.spark.pages.Unit unit) {\n\t\tsuper(\"")).output(mark("name", "firstUpperCase")).output(literal("\", unit, \"/")).output(mark("name", "SnakeCaseToCamelCase", "lowercase")).output(literal("proxy\");\n\t}\n\n\t")).output(expression().output(mark("request", "accessible").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(mark("parameter", "method").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(mark("event", "method").multiple("\n\n"))).output(literal("\n}")),
				rule().condition((type("display"))).output(literal("package ")).output(mark("package")).output(literal(".ui.displays")).output(expression().output(literal(".")).output(mark("packageType")).output(literal("s"))).output(literal(";\n\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.ui.displays.components.*;\nimport ")).output(mark("package")).output(literal(".ui.*;\n")).output(mark("schemaImport")).output(literal("\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\n")).output(mark("templatesImport")).output(literal("\n")).output(mark("tablesImport")).output(literal("\n")).output(mark("listsImport")).output(literal("\n")).output(mark("magazinesImport")).output(literal("\n")).output(mark("mapsImport")).output(literal("\n")).output(mark("dynamicTablesImport")).output(literal("\n")).output(mark("blocksImport")).output(literal("\n")).output(mark("itemsImport")).output(literal("\n")).output(mark("rowsImport")).output(literal("\n")).output(mark("notifierImport")).output(literal("\n\npublic ")).output(expression().output(mark("abstract", "lowerCase"))).output(literal(" class ")).output(mark("abstract")).output(mark("name", "firstUpperCase")).output(mark("parametrized")).output(literal(" ")).output(mark("displayExtends")).output(expression().output(literal(" ")).output(mark("implements"))).output(literal(" {\n\t")).output(expression().output(mark("reference", "declaration").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("component", "declarations").multiple("\n"))).output(literal("\n\n\tpublic ")).output(mark("abstract")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("abstractBox", "type")).output(literal(" box) {\n\t\tsuper(box);\n\t\tid(\"")).output(mark("id")).output(literal("\");\n\t}\n\n\t@Override\n\tpublic void init() {\n\t\tsuper.init();\n\t\t")).output(expression().output(mark("reference").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("componentReferences"))).output(literal("\n\t\t")).output(expression().output(mark("component", "initializations").multiple("\n"))).output(literal("\n\t}\n\n\t@Override\n\tpublic void remove() {\n\t\tsuper.remove();\n\t\t")).output(expression().output(mark("component", "unregister").multiple("\n"))).output(literal("\n\t}\n\t")).output(mark("methods")).output(literal("\n\t")).output(expression().output(mark("component", "class").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(mark("component", "method").multiple("\n\n"))).output(literal("\n}")),
				rule().condition((allTypes("notifierImport", "table"))).output(literal("import io.intino.alexandria.ui.displays.notifiers.TableNotifier;")),
				rule().condition((allTypes("notifierImport", "dynamictable"))).output(literal("import io.intino.alexandria.ui.displays.notifiers.DynamicTableNotifier;")),
				rule().condition((allTypes("notifierImport", "item"))).output(literal("import io.intino.alexandria.ui.displays.notifiers.ItemNotifier;")),
				rule().condition((allTypes("notifierImport", "row"))).output(literal("import io.intino.alexandria.ui.displays.notifiers.RowNotifier;")),
				rule().condition((allTypes("notifierImport", "template", "accessible"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.notifiers.")).output(mark("name", "firstUpperCase")).output(literal("Notifier;")),
				rule().condition((allTypes("notifierImport", "template"))).output(literal("import io.intino.alexandria.ui.displays.notifiers.TemplateNotifier;")),
				rule().condition((type("notifierImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.notifiers.")).output(mark("name", "firstUpperCase")).output(literal("Notifier;")),
				rule().condition((allTypes("parametrized", "generic"))).output(literal("<DN extends ")).output(mark("name", "firstUpperCase")).output(literal("Notifier, ")).output(mark("abstractBox", "extension")).output(literal(">")),
				rule().condition((allTypes("parametrized", "accessible"))).output(literal("<DN extends ")).output(mark("name", "firstUpperCase")).output(literal("Notifier, ")).output(mark("abstractBox", "extension")).output(literal(">")),
				rule().condition((type("parametrized"))).output(mark("abstractBox", "extensionTagged")),
				rule().condition((allTypes("displayExtends", "generic"))).output(literal("extends ")).output(mark("parent")).output(literal("<DN, ")).output(mark("abstractBox", "type")).output(literal(">")),
				rule().condition((allTypes("displayExtends", "template", "accessible"))).output(literal("extends io.intino.alexandria.ui.displays.components.Template<DN, ")).output(mark("modelClass")).output(literal(", ")).output(mark("abstractBox", "type")).output(literal(">")),
				rule().condition((allTypes("displayExtends", "template"))).output(literal("extends io.intino.alexandria.ui.displays.components.Template<TemplateNotifier, ")).output(mark("modelClass")).output(literal(", ")).output(mark("abstractBox", "type")).output(literal(">")),
				rule().condition((allTypes("displayExtends", "table"))).output(literal("extends io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("name", "firstUpperCase")).output(literal("Row, ")).output(mark("itemClass")).output(literal(">")),
				rule().condition((allTypes("displayExtends", "dynamictable"))).output(literal("extends io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("name", "firstUpperCase")).output(literal("Row, ")).output(mark("itemClass")).output(literal(">")),
				rule().condition((allTypes("displayExtends", "collection"))).output(literal("extends io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("componentType")).output(literal(", ")).output(mark("itemClass")).output(literal(">")),
				rule().condition((allTypes("displayExtends", "item"))).output(literal("extends io.intino.alexandria.ui.displays.components.Item<ItemNotifier, ")).output(mark("itemClass")).output(literal(", ")).output(mark("abstractBox", "type")).output(literal(">")),
				rule().condition((allTypes("displayExtends", "row"))).output(literal("extends io.intino.alexandria.ui.displays.components.Row<RowNotifier, ")).output(mark("itemClass")).output(literal(", ")).output(mark("abstractBox", "type")).output(literal(">")),
				rule().condition((allTypes("displayExtends", "dialog"))).output(literal("extends io.intino.alexandria.ui.displays.components.Dialog<")).output(mark("name", "firstUpperCase")).output(literal("Notifier, ")).output(mark("abstractBox", "type")).output(literal(">")),
				rule().condition((type("displayExtends"))).output(literal("extends io.intino.alexandria.ui.displays.")).output(mark("type", "firstUpperCase")).output(literal("<")).output(mark("name", "firstUpperCase")).output(literal("Notifier, ")).output(mark("abstractBox", "type")).output(literal(">")),
				rule().condition((type("templatesImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.templates.*;")),
				rule().condition((type("tablesImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.tables.*;")),
				rule().condition((type("listsImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.lists.*;")),
				rule().condition((type("magazinesImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.magazines.*;")),
				rule().condition((type("mapsImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.maps.*;")),
				rule().condition((type("dynamicTablesImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.dynamictables.*;")),
				rule().condition((type("blocksImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.blocks.*;")),
				rule().condition((type("itemsImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.items.*;")),
				rule().condition((type("rowsImport"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".ui.displays.rows.*;")),
				rule().condition((allTypes("componentReferences", "forRoot"))).output(expression().output(mark("component", "rootReferences").multiple("\n"))),
				rule().condition((type("componentReferences"))).output(expression().output(mark("component", "references").multiple("\n"))),
				rule().condition((type("attribute"))).output(literal("public ")).output(mark("clazz")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(";")),
				rule().condition((allTypes("methods", "dynamicloaded"))).output(literal("@Override\npublic io.intino.alexandria.ui.displays.components.DynamicLoaded.LoadTime loadTime() {\n\treturn io.intino.alexandria.ui.displays.components.DynamicLoaded.LoadTime.valueOf(\"")).output(mark("loadTime")).output(literal("\");\n}\n\n@Override\npublic void showLoading() {\n\tnotifier.refreshLoading(true);\n}\n\n@Override\npublic void hideLoading() {\n\tnotifier.refreshLoading(false);\n}")),
				rule().condition((type("request")), (trigger("accessible"))).output(literal("public void ")).output(mark("name")).output(literal("(")).output(expression().output(mark("parameter")).output(literal(" value"))).output(literal(") {\n\trequest(\"")).output(mark("name")).output(literal("\"")).output(expression().output(literal(", ")).output(mark("parameter", "parameterValue"))).output(literal(");\n}")),
				rule().condition((trigger("parametervalue"))).output(literal("value")),
				rule().condition((type("parameter")), (trigger("method"))).output(literal("public void ")).output(mark("value", "firstLowerCase")).output(literal("(String value) {\n\tadd(\"")).output(mark("value")).output(literal("\", value);\n}")),
				rule().condition((type("parameter"))).output(mark("value")),
				rule().condition((type("event")), (trigger("method"))).output(literal("public void on")).output(mark("name", "firstUpperCase")).output(literal("(java.util.function.Consumer<Boolean> listener) {\n\tonMessage(\"")).output(mark("name", "firstLowerCase")).output(literal("\").then(listener);\n}"))
		);
	}
}