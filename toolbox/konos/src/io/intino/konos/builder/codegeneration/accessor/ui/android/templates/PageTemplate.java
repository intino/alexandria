package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class PageTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("resource", "template")).output(literal("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<androidx.constraintlayout.widget.ConstraintLayout\nxmlns:android=\"http://schemas.android.com/apk/res/android\"\nxmlns:alexandria=\"http://schemas.android.com/apk/res-auto\"\nxmlns:tools=\"http://schemas.android.com/tools\"\nandroid:layout_width=\"match_parent\"\nandroid:layout_height=\"match_parent\"\ntools:context=\".pages.")).output(placeholder("name", "PascalCase")).output(literal("Activity\">\n\n<")).output(placeholder("package", "validPackage")).output(literal(".mobile.android.displays.templates.")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal("\n\tandroid:id=\"@+id/")).output(placeholder("pageDisplayId", "firstLowerCase")).output(literal("\"\n\tandroid:layout_width=\"match_parent\"\n\tandroid:layout_height=\"match_parent\">\n</")).output(placeholder("package", "validPackage")).output(literal(".mobile.android.displays.templates.")).output(placeholder("pageDisplay", "firstUpperCase")).output(literal(">\n</androidx.constraintlayout.widget.ConstraintLayout>")));
		rules.add(rule().condition(allTypes("resource", "main")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".mobile.android.pages\n\nimport android.os.Bundle\nimport io.intino.alexandria.mobile.Application\nimport io.intino.alexandria.mobile.android.AlexandriaActivity\nimport ")).output(placeholder("package", "validPackage")).output(literal(".mobile.android.R\n\nclass ")).output(placeholder("name", "PascalCase")).output(literal("Activity : AlexandriaActivity(\"")).output(placeholder("name")).output(literal("\") {\n\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)\n        Application.onVerifyPermissions { type, callback -> verifyPermissions(type, callback) }\n        Application.onPickFile { callback -> pickFile(callback) }\n        Application.start(\"")).output(placeholder("name")).output(literal("\", \"")).output(placeholder("url")).output(literal("\", pushService(withParams(\"")).output(placeholder("url")).output(placeholder("path")).output(literal("\")) { showNoConnectionDialog(); })\n        setContentView(R.layout.")).output(placeholder("name", "camelCaseToUnderscoreCase", "lowerCase")).output(literal("_activity)\n    }\n\n\n}")));
		rules.add(rule().condition(allTypes("resource")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".mobile.android.pages\n\nimport android.os.Bundle\nimport io.intino.alexandria.mobile.Application\nimport io.intino.alexandria.mobile.android.AlexandriaActivity\nimport ")).output(placeholder("package", "validPackage")).output(literal(".mobile.android.R\n\nclass ")).output(placeholder("name", "PascalCase")).output(literal("Activity : AlexandriaActivity(\"")).output(placeholder("name")).output(literal("\") {\n\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)\n        Application.onVerifyPermissions { type, callback -> verifyPermissions(type, callback) }\n        Application.onPickFile { callback -> pickFile(callback) }\n        Application.close(\"")).output(placeholder("name")).output(literal("\")\n        Application.open(\"")).output(placeholder("name")).output(literal("\", \"")).output(placeholder("url")).output(literal("\", pushService(withParams(\"")).output(placeholder("url")).output(placeholder("path")).output(literal("\")) { showNoConnectionDialog(); })\n        setContentView(R.layout.")).output(placeholder("name", "camelCaseToUnderscoreCase", "lowerCase")).output(literal("_activity)\n    }\n\n}")));
		rules.add(rule().condition(all(attribute("hasabstract"), trigger("origin"))).output(literal("../../src")));
		rules.add(rule().condition(all(attribute("decorated"), trigger("origin"))).output(literal("../../gen")));
		rules.add(rule().condition(trigger("origin")).output(literal("..")));
		rules.add(rule().condition(allTypes("accessibleImport")).output(literal("import ")).output(placeholder("service", "firstUpperCase")).output(literal("AccessibleDisplays from '")).output(placeholder("elements")).output(literal("/gen/AccessibleDisplays';")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}