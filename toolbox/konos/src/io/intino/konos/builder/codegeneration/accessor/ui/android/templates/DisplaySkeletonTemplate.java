package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class DisplaySkeletonTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display", "accessible")));
		rules.add(rule().condition(allTypes("display", "template", "scrollable")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.android.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal("\n\nimport android.content.Context\nimport android.util.AttributeSet\nimport android.view.View\nimport android.widget.ScrollView\nimport io.intino.alexandria.mobile.android.R\nimport io.intino.alexandria.mobile.displays.notifiers.TemplateNotifier\nimport io.intino.alexandria.mobile.displays.requesters.TemplateRequester\n\nopen class ")).output(placeholder("name", "firstUpperCase")).output(literal(" @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0, layout: Int? = null) : io.intino.alexandria.mobile.android.displays.components.Template<TemplateRequester, TemplateNotifier>(context, attrs, defStyleAttr, defStyleRes, R.layout.")).output(placeholder("name", "camelCaseToSnakeCase", "lowerCase")).output(literal(") {\n    init {\n        this.notifier = TemplateNotifier(this)\n        this.requester = TemplateRequester(this)\n        val scroller = findViewById<ScrollView>(R.id.")).output(placeholder("name", "camelCaseToSnakeCase")).output(literal("_scroll)\n        ")).output(expression().output(placeholder("component", "transfer").multiple("\n"))).output(literal("\n        scroller.setOnTouchListener { _, _ ->\n            scroller.requestDisallowInterceptTouchEvent(false)\n            false\n        }\n        ")).output(expression().output(placeholder("component", "scrolling").multiple("\n"))).output(literal("\n    }\n}")));
		rules.add(rule().condition(allTypes("display", "template")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.android.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal("\n\nimport android.content.Context\nimport android.util.AttributeSet\nimport io.intino.alexandria.mobile.android.R\nimport io.intino.alexandria.mobile.displays.notifiers.TemplateNotifier\nimport io.intino.alexandria.mobile.displays.requesters.TemplateRequester\n\nopen class ")).output(placeholder("name", "firstUpperCase")).output(literal(" @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0, layout: Int? = null) : io.intino.alexandria.mobile.android.displays.components.Template<TemplateRequester, TemplateNotifier>(context, attrs, defStyleAttr, defStyleRes, R.layout.")).output(placeholder("name", "camelCaseToSnakeCase", "lowerCase")).output(literal(") {\n    init {\n        this.notifier = TemplateNotifier(this)\n        this.requester = TemplateRequester(this)\n        ")).output(expression().output(placeholder("component", "transfer").multiple("\n"))).output(literal("\n    }\n}")));
		rules.add(rule().condition(allTypes("display", "collection")).output(literal("import React from \"react\";\nimport { withStyles } from '@material-ui/core/styles';\nimport Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(" from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays")).output(expression().output(literal("/")).output(placeholder("packageType")).output(literal("s"))).output(literal("/Abstract")).output(placeholder("name", "firstUpperCase")).output(literal("\";\nimport ")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays/notifiers/")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier\";\nimport ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester from \"../../")).output(placeholder("packageTypeRelativeDirectory")).output(literal("gen/displays/requesters/")).output(placeholder("name", "firstUpperCase")).output(literal("Requester\";\nimport DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';\nimport { withSnackbar } from 'notistack';\nimport { ")).output(placeholder("type", "firstUpperCase")).output(literal("Styles } from \"alexandria-ui-elements/src/displays/components/")).output(placeholder("type", "firstUpperCase")).output(literal("\";\n\nconst styles = theme => ({\n\t...")).output(placeholder("type", "firstUpperCase")).output(literal("Styles(theme),\n});\n\nclass ")).output(placeholder("name", "firstUpperCase")).output(literal(" extends Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(" {\n\n\tconstructor(props) {\n\t\tsuper(props);\n\t\tthis.notifier = new ")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier(this);\n\t\tthis.requester = new ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester(this);\n\t};\n\n\t")).output(placeholder("notification").multiple("\n\n")).output(literal("\n}\n\nexport default withStyles(styles, { withTheme: true })(withSnackbar(")).output(placeholder("name", "firstUpperCase")).output(literal("));\nDisplayFactory.register(\"")).output(placeholder("name", "firstUpperCase")).output(literal("\", withStyles(styles, { withTheme: true })(withSnackbar(")).output(placeholder("name", "firstUpperCase")).output(literal(")));")));
		rules.add(rule().condition(allTypes("display", "interface")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal("\n\n")).output(placeholder("schemaImport")).output(literal("\n\ninterface ")).output(placeholder("name", "firstUpperCase")).output(literal(" ")).output(placeholder("displayExtends", "interface")).output(literal(" {\n    ")).output(placeholder("notification", "interface").multiple("\n\n")).output(literal("\n}")));
		rules.add(rule().condition(allTypes("display")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.android.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal("\n\nimport android.content.Context\nimport android.util.AttributeSet\nimport androidx.core.content.ContextCompat.*\n\nimport ")).output(placeholder("package")).output(literal(".mobile.displays.notifiers.")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier\nimport ")).output(placeholder("package")).output(literal(".mobile.displays.requesters.")).output(placeholder("name", "firstUpperCase")).output(literal("Requester\nimport ")).output(placeholder("package")).output(literal(".mobile.android.R\n")).output(placeholder("schemaImport")).output(literal("\n\nopen class ")).output(placeholder("name", "firstUpperCase")).output(literal("<DR: ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester, DN: ")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0, layout: Int? = null) ")).output(placeholder("displayExtends")).output(literal(", ")).output(placeholder("package")).output(literal(".mobile.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal(".")).output(placeholder("name", "firstUpperCase")).output(literal(" {\n\n    init {\n\t\tthis.requester = ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester(this) as DR\n\t\tthis.notifier = ")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier(this) as DN\n        inflate(viewContext(), layout ?: R.layout.")).output(placeholder("name", "camelCaseToSnakeCase", "lowerCase")).output(literal(", this)\n    }\n\n\t")).output(placeholder("notification").multiple("\n\n")).output(literal("\n\n}")));
		rules.add(rule().condition(all(allTypes("notification"), trigger("interface"))).output(literal("fun ")).output(placeholder("name")).output(literal("(")).output(expression().output(literal("value : ")).output(placeholder("parameter"))).output(literal(")")));
		rules.add(rule().condition(allTypes("notification")).output(literal("override fun ")).output(placeholder("name")).output(literal("(")).output(expression().output(literal("value: ")).output(placeholder("parameter"))).output(literal(") {\n}")));
		rules.add(rule().condition(trigger("parametervalue")).output(literal("value")));
		rules.add(rule().condition(all(allTypes("date"), trigger("parameter"))).output(literal("kotlinx.datetime.Instant")));
		rules.add(rule().condition(all(allTypes("datetime"), trigger("parameter"))).output(literal("kotlinx.datetime.Instant")));
		rules.add(rule().condition(all(allTypes("integer"), trigger("parameter"))).output(literal("Int")));
		rules.add(rule().condition(all(allTypes("list"), trigger("parameter"))).output(literal("kotlin.collections.List<")).output(placeholder("value")).output(literal(">")));
		rules.add(rule().condition(trigger("parameter")).output(placeholder("value")));
		rules.add(rule().condition(all(allTypes("displayExtends", "generic"), trigger("interface"))).output(literal(": ")).output(placeholder("parentMobileShared")));
		rules.add(rule().condition(allTypes("displayExtends", "generic", "isExtensionOf")).output(literal(": ")).output(placeholder("parent")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(allTypes("displayExtends", "generic")).output(literal(": ")).output(placeholder("parent")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes, IntArray(0))")));
		rules.add(rule().condition(all(allTypes("displayExtends", "template"), trigger("interface"))).output(literal(": io.intino.alexandria.mobile.displays.components.Template")));
		rules.add(rule().condition(allTypes("displayExtends", "template")).output(literal(": io.intino.alexandria.mobile.android.displays.components.Template<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "table"), trigger("interface"))).output(literal(": io.intino.alexandria.mobile.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")));
		rules.add(rule().condition(allTypes("displayExtends", "table")).output(literal(": io.intino.alexandria.mobile.android.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "dynamictable"), trigger("interface"))).output(literal(": io.intino.alexandria.mobile.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")));
		rules.add(rule().condition(allTypes("displayExtends", "dynamictable")).output(literal(": io.intino.alexandria.mobile.android.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "collection"), trigger("interface"))).output(literal(": io.intino.alexandria.mobile.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")));
		rules.add(rule().condition(allTypes("displayExtends", "collection")).output(literal(": io.intino.alexandria.mobile.android.displays.components.")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "item"), trigger("interface"))).output(literal(": io.intino.alexandria.mobile.displays.components.Item")));
		rules.add(rule().condition(allTypes("displayExtends", "item")).output(literal(": io.intino.alexandria.mobile.android.displays.components.Item<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "row"), trigger("interface"))).output(literal(": io.intino.alexandria.mobile.displays.components.Row")));
		rules.add(rule().condition(allTypes("displayExtends", "row")).output(literal(": io.intino.alexandria.mobile.android.displays.components.Row<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends", "dialog"), trigger("interface"))).output(literal(": io.intino.alexandria.mobile.displays.components.Dialog")));
		rules.add(rule().condition(allTypes("displayExtends", "dialog")).output(literal(": io.intino.alexandria.mobile.android.displays.components.Dialog<DR, DN>(context, attrs, defStyleAttr, defStyleRes)")));
		rules.add(rule().condition(all(allTypes("displayExtends"), trigger("interface"))).output(literal(": io.intino.alexandria.mobile.displays.")).output(placeholder("type", "firstUpperCase")));
		rules.add(rule().condition(allTypes("displayExtends")).output(literal(": io.intino.alexandria.mobile.android.displays.")).output(placeholder("type", "firstUpperCase")).output(literal("<DR, DN>(context, attrs, defStyleAttr, defStyleRes, IntArray(0))")));
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