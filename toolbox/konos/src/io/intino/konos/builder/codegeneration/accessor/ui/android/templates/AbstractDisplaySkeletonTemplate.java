package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AbstractDisplaySkeletonTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display", "accessible")).output(literal("import React from \"react\";\n")).output(expression().output(placeholder("alexandriaBlockImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaTemplateStampImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaDisplayStampImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(placeholder("projectComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(placeholder("parent", "import"))).output(literal("\n")).output(expression().output(literal("import ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier from \"")).output(placeholder("notifierDirectory")).output(literal("/notifiers/")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier\";"))).output(literal("\n")).output(expression().output(literal("import ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester from \"")).output(placeholder("requesterDirectory")).output(literal("/requesters/")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester\";"))).output(literal("\n")).output(expression().output(placeholder("displayRegistration", "import"))).output(literal("\nimport ")).output(placeholder("name", "PascalCase")).output(literal(" from './")).output(placeholder("name", "PascalCase")).output(literal("';\n\nexport default class ")).output(placeholder("name", "PascalCase")).output(literal("Proxy extends ")).output(placeholder("displayExtends")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\t")).output(expression().output(literal("this.notifier = new ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier(this);"))).output(literal("\n\t\t")).output(expression().output(literal("this.requester = new ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester(this);"))).output(literal("\n\t\t")).output(expression().output(placeholder("properties", "initialization"))).output(literal("\n\t};\n\n}\n\n")).output(expression().output(placeholder("displayRegistration", "declaration"))));
		rules.add(rule().condition(allTypes("display", "res", "noAbstract", "scrollable")).output(literal("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<com.google.android.flexbox.FlexboxLayout\n    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n    xmlns:alexandria=\"http://schemas.android.com/apk/res-auto\"\n    android:layout_width=\"match_parent\"\n    android:layout_height=\"match_parent\"\n    alexandria:flexDirection=\"")).output(placeholder("layout")).output(literal("\">\n\n    <ScrollView\n        android:id=\"@+id/")).output(placeholder("name", "camelCaseToSnakeCase")).output(literal("_scroll\"\n        android:layout_width=\"match_parent\"\n\t    android:layout_height=\"match_parent\"\n\t    android:fillViewport=\"true\">\n\n        <com.google.android.flexbox.FlexboxLayout\n            android:layout_width=\"match_parent\"\n            android:layout_height=\"wrap_content\"\n            alexandria:flexDirection=\"")).output(placeholder("layout")).output(literal("\">\n            ")).output(expression().output(placeholder("component").multiple("\n"))).output(literal("\n\t\t</com.google.android.flexbox.FlexboxLayout>\n    </ScrollView>\n\n</com.google.android.flexbox.FlexboxLayout>")));
		rules.add(rule().condition(allTypes("display", "res", "noAbstract")).output(literal("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<com.google.android.flexbox.FlexboxLayout\n    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n    xmlns:alexandria=\"http://schemas.android.com/apk/res-auto\"\n    android:layout_width=\"match_parent\"\n    android:layout_height=\"match_parent\"\n    alexandria:flexDirection=\"")).output(placeholder("layout")).output(literal("\">\n\n    ")).output(expression().output(placeholder("component").multiple("\n"))).output(literal("\n\n</com.google.android.flexbox.FlexboxLayout>")));
		rules.add(rule().condition(allTypes("layout", "horizontal")).output(literal("row")));
		rules.add(rule().condition(allTypes("layout", "vertical")).output(literal("column")));
		rules.add(rule().condition(allTypes("display", "res")).output(literal("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<com.google.android.flexbox.FlexboxLayout\n    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n    xmlns:alexandria=\"http://schemas.android.com/apk/res-auto\"\n    android:layout_width=\"match_parent\"\n    android:layout_height=\"match_parent\"\n    alexandria:flexDirection=\"")).output(placeholder("layout")).output(literal("\">\n\n    <TextView\n            android:layout_width=\"wrap_content\"\n            android:layout_height=\"wrap_content\"\n            android:text=\"I am ")).output(placeholder("name")).output(literal("\"\n        />\n\n    ")).output(expression().output(placeholder("component").multiple("\n"))).output(literal("\n\n</com.google.android.flexbox.FlexboxLayout>")));
		rules.add(rule().condition(allTypes("display", "noAbstract")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.android.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal("\n\nimport android.content.Context\nimport android.util.AttributeSet\nimport androidx.core.content.ContextCompat.*\n\n")).output(expression().output(placeholder("parent", "import"))).output(literal("\n")).output(placeholder("accessibleNotifierImport")).output(literal("\nimport io.intino.alexandria.mobile.displays.requesters.")).output(placeholder("requester")).output(literal("Requester\nimport ")).output(placeholder("package")).output(literal(".mobile.android.R\n\nclass ")).output(placeholder("name", "firstUpperCase")).output(literal(" @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : ")).output(placeholder("displayExtends")).output(literal("<")).output(placeholder("requester")).output(literal("Requester, ")).output(placeholder("notifier")).output(literal("Notifier>(context, attrs, defStyleAttr, defStyleRes, R.layout.")).output(placeholder("name", "camelCaseToSnakeCase", "lowerCase")).output(literal(") {\n\n\tinit {\n\t\tthis.notifier = ")).output(placeholder("notifier")).output(literal("Notifier(this);\n\t\tthis.requester = ")).output(placeholder("requester")).output(literal("Requester(this);\n\t\t")).output(expression().output(placeholder("properties", "initialization"))).output(literal("\n\t}\n\n\t")).output(placeholder("notification").multiple("\n\n")).output(literal("\n\t")).output(expression().output(placeholder("notifyProxyMethod"))).output(literal("\n}")));
		rules.add(rule().condition(allTypes("accessibleNotifierImport", "accessible")).output(literal("import ")).output(placeholder("package")).output(literal(".mobile.displays.notifiers.")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier")));
		rules.add(rule().condition(allTypes("accessibleNotifierImport")).output(literal("import io.intino.alexandria.mobile.displays.notifiers.")).output(placeholder("notifier")).output(literal("Notifier")));
		rules.add(rule().condition(allTypes("notifyProxyMethod")).output(literal("notifyProxyMessage = (name) => {\n\tif (!this.props.onProxyMessage) return;\n\tthis.props.onProxyMessage(name);\n};")));
		rules.add(rule().condition(allTypes("display", "collection")).output(literal("import React from \"react\";\n")).output(expression().output(placeholder("alexandriaBlockImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaTemplateStampImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaDisplayStampImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(placeholder("projectComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(placeholder("parent", "import"))).output(literal("\n")).output(expression().output(literal("import ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier from \"")).output(placeholder("notifierDirectory")).output(literal("/notifiers/")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier\";"))).output(literal("\n")).output(expression().output(literal("import ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester from \"")).output(placeholder("requesterDirectory")).output(literal("/requesters/")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester\";"))).output(literal("\n")).output(expression().output(placeholder("displayRegistration", "import"))).output(literal("\n\nexport default class ")).output(placeholder("name", "firstUpperCase")).output(literal(" extends ")).output(placeholder("displayExtends")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\t")).output(expression().output(literal("this.notifier = new ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier(this);"))).output(literal("\n\t\t")).output(expression().output(literal("this.requester = new ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester(this);"))).output(literal("\n\t\t")).output(expression().output(placeholder("properties", "initialization"))).output(literal("\n\t};\n\n}\n\n")).output(expression().output(placeholder("displayRegistration", "declaration"))));
		rules.add(rule().condition(allTypes("display")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.android.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal("\n\nimport android.content.Context\nimport android.util.AttributeSet\nimport androidx.core.content.ContextCompat.*\n\n")).output(expression().output(placeholder("alexandriaBlockImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaTemplateStampImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaDisplayStampImport"))).output(literal("\n")).output(expression().output(placeholder("alexandriaComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(placeholder("projectComponentImport").multiple("\n"))).output(literal("\n")).output(expression().output(placeholder("parent", "import"))).output(literal("\nimport ")).output(placeholder("package")).output(literal(".mobile.displays.notifiers.")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier\nimport ")).output(placeholder("package")).output(literal(".mobile.displays.requesters.")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester\n")).output(expression().output(placeholder("displayRegistration", "import"))).output(literal("\n")).output(placeholder("schemaImport")).output(literal("\nimport ")).output(placeholder("package")).output(literal(".mobile.android.R\n\nopen class ")).output(placeholder("name", "firstUpperCase")).output(literal("<DR: ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester, DN: ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0, layout: Int? = null) ")).output(placeholder("displayExtends", "display")).output(literal(", ")).output(placeholder("package")).output(literal(".mobile.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal(".")).output(placeholder("name", "firstUpperCase")).output(literal(" {\n\n    init {\n        this.requester = ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Requester(this) as DR\n        this.notifier = ")).output(placeholder("notDecorated", "firstUpperCase")).output(literal("Notifier(this) as DN\n        inflate(viewContext(), layout ?: R.layout.")).output(placeholder("name", "camelCaseToSnakeCase", "lowerCase")).output(literal(", this)\n    }\n\n\t")).output(placeholder("notification").multiple("\n\n")).output(literal("\n    ")).output(expression().output(placeholder("reference").multiple("\n"))).output(literal("\n    ")).output(expression().output(placeholder("component").multiple("\n"))).output(literal("\n\n}")));
		rules.add(rule().condition(all(allTypes("notification"), trigger("interface"))).output(literal("fun ")).output(placeholder("name")).output(literal("(")).output(expression().output(literal("value : ")).output(placeholder("parameter"))).output(literal(")")));
		rules.add(rule().condition(allTypes("notification")).output(literal("override fun ")).output(placeholder("name")).output(literal("(")).output(expression().output(literal("value: ")).output(placeholder("parameter"))).output(literal(") {\n}")));
		rules.add(rule().condition(trigger("parametervalue")).output(literal("value")));
		rules.add(rule().condition(all(allTypes("date"), trigger("parameter"))).output(literal("kotlinx.datetime.Instant")));
		rules.add(rule().condition(all(allTypes("datetime"), trigger("parameter"))).output(literal("kotlinx.datetime.Instant")));
		rules.add(rule().condition(all(allTypes("integer"), trigger("parameter"))).output(literal("Int")));
		rules.add(rule().condition(trigger("parameter")).output(placeholder("value")));
		rules.add(rule().condition(all(allTypes("displayRegistration"), trigger("import"))).output(literal("import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';")));
		rules.add(rule().condition(all(allTypes("displayRegistration", "accessible"), trigger("declaration"))).output(literal("DisplayFactory.register(\"")).output(placeholder("name", "PascalCase")).output(literal("Proxy\", ")).output(placeholder("name", "PascalCase")).output(literal("Proxy);")));
		rules.add(rule().condition(all(allTypes("displayRegistration"), trigger("declaration"))).output(literal("DisplayFactory.register(\"")).output(placeholder("name", "firstUpperCase")).output(literal("\", ")).output(placeholder("name", "firstUpperCase")).output(literal(");")));
		rules.add(rule().condition(allTypes("alexandriaImport")).output(literal("import Ui")).output(placeholder("name", "firstUpperCase")).output(literal(" from \"alexandria-ui-elements/src/displays/components/")).output(placeholder("name", "firstUpperCase")).output(literal("\";")));
		rules.add(rule().condition(allTypes("alexandriaImport", "embedded")).output(literal("import { Embedded")).output(placeholder("name", "firstUpperCase")).output(literal(" as Ui")).output(placeholder("name", "firstUpperCase")).output(literal(" } from \"alexandria-ui-elements/src/displays/components/")).output(placeholder("name", "firstUpperCase")).output(literal("\";")));
		rules.add(rule().condition(allTypes("alexandriaComponentImport")).output(literal("import Ui")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")).output(literal(" from \"alexandria-ui-elements/")).output(expression().output(placeholder("componentTarget"))).output(literal("/displays/")).output(expression().output(placeholder("componentDirectory")).output(literal("/"))).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")).output(literal("\";")));
		rules.add(rule().condition(allTypes("projectComponentImport", "ownertemplatestamp")).output(literal("import Displays")).output(placeholder("name", "firstUpperCase")).output(literal(" from \"")).output(placeholder("ownerModuleName")).output(literal("/gen/displays/templates/")).output(placeholder("name", "firstUpperCase")).output(literal("\";")));
		rules.add(rule().condition(allTypes("projectComponentImport")).output(literal("import Displays")).output(placeholder("name", "firstUpperCase")).output(literal(" from \"")).output(placeholder("serviceName")).output(literal("/")).output(expression().output(placeholder("directory"))).output(literal("/displays/")).output(expression().output(placeholder("componentDirectory")).output(literal("/"))).output(placeholder("name", "firstUpperCase")).output(literal("\";")));
		rules.add(rule().condition(all(allTypes("displayExtends", "generic", "isExtensionOf"), trigger("display"))).output(literal(": ")).output(placeholder("parent")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "generic"), trigger("display"))).output(literal(": ")).output(placeholder("parent")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes, IntArray(0))")));
		rules.add(rule().condition(all(allTypes("displayExtends", "template"), trigger("display"))).output(literal(": io.intino.alexandria.mobile.android.displays.components.Template<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "table"), trigger("display"))).output(literal(": io.intino.alexandria.mobile.android.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "dynamictable"), trigger("display"))).output(literal(": io.intino.alexandria.mobile.android.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "collection"), trigger("display"))).output(literal(": io.intino.alexandria.mobile.android.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "item"), trigger("display"))).output(literal(": io.intino.alexandria.mobile.android.displays.components.Item<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "row"), trigger("display"))).output(literal(": io.intino.alexandria.mobile.android.displays.components.Row<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "dialog"), trigger("display"))).output(literal(": io.intino.alexandria.mobile.android.displays.components.Dialog<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends"), trigger("display"))).output(literal(": io.intino.alexandria.mobile.android.displays.")).output(placeholder("type", "firstUpperCase")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes, IntArray(0))")));
		rules.add(rule().condition(allTypes("displayExtends", "generic", "isExtensionOf")).output(placeholder("parent")));
		rules.add(rule().condition(allTypes("displayExtends", "generic")).output(placeholder("parent")));
		rules.add(rule().condition(allTypes("displayExtends")).output(placeholder("type", "firstUpperCase")));
		rules.add(rule().condition(all(allTypes("renderTag", "template"), trigger("end"))).output(literal("</UiBlock>")));
		rules.add(rule().condition(all(allTypes("renderTag", "block"), trigger("end"))).output(literal("</UiBlock>")));
		rules.add(rule().condition(all(allTypes("renderTag", "item"), trigger("end"))).output(literal("</div>")));
		rules.add(rule().condition(all(allTypes("renderTag"), trigger("end"))).output(literal("</React.Fragment>")));
		rules.add(rule().condition(allTypes("renderTag", "template")).output(literal("<UiBlock")).output(expression().output(placeholder("properties", "common")).output(placeholder("properties", "specific"))).output(literal(" style={{...this.props.style,...display}}>")));
		rules.add(rule().condition(allTypes("renderTag", "block")).output(literal("<UiBlock")).output(expression().output(placeholder("properties", "common")).output(placeholder("properties", "specific"))).output(literal(" style={{...this.props.style,...display}}>")));
		rules.add(rule().condition(allTypes("renderTag", "item")).output(literal("<div style={{width:\"100%\",height:\"100%\",...this.props.style,...this.style(),...display}} className={className}>")));
		rules.add(rule().condition(allTypes("renderTag")).output(literal("<React.Fragment>")));
		rules.add(rule().condition(allTypes("renderTagAttributes", "block")).output(placeholder("properties", "common")).output(placeholder("properties", "specific")));
		rules.add(rule().condition(allTypes("renderTagAttributes")));
		rules.add(rule().condition(all(attribute("parent"), trigger("import"))).output(literal("import ")).output(placeholder("package")).output(literal(".mobile.android.displays.")).output(placeholder("parentDirectory")).output(literal(".")).output(placeholder("parent", "firstUpperCase")));
		rules.add(rule().condition(all(attribute("accessible"), trigger("import"))).output(literal("import io.intino.alexandria.mobile.android.displays.ProxyDisplay")));
		rules.add(rule().condition(all(attribute("basedisplay"), trigger("import"))).output(literal("import io.intino.alexandria.mobile.android.displays.Display")));
		rules.add(rule().condition(all(attribute("basecomponent"), trigger("import"))).output(literal("import io.intino.alexandria.mobile.android.displays.Component")));
		rules.add(rule().condition(all(attribute("embeddedcomponent"), trigger("import"))).output(literal("import io.intino.alexandria.mobile.android.displays.components.")).output(placeholder("value", "firstUpperCase")));
		rules.add(rule().condition(all(attribute("component"), trigger("import"))).output(literal("import io.intino.alexandria.mobile.android.displays.components.")).output(placeholder("value", "firstUpperCase")));
		rules.add(rule().condition(trigger("import")).output(literal("import io.intino.alexandria.mobile.android.displays.")).output(placeholder("value", "firstUpperCase")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".mobile.schemas.*;")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}