package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PassiveViewNotifierTemplate extends Template {

	protected PassiveViewNotifierTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PassiveViewNotifierTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display & accessible"))).add(literal("import Notifier from \"./Notifier\";\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal("Notifier extends Notifier {\n\nconstructor(element) {\n    super(element);\n    this.setup();\n};\n\nsetup() {\n\tif (this.element == null || this.pushLinked) return;\n\tsuper.setup();\n\tthis.when(\"refreshBaseUrl\").toSelf().execute((parameters) => this.element.refreshBaseUrl(parameters.v));\n\tthis.when(\"refreshError\").toSelf().execute((parameters) => this.element.refreshError(parameters.v));\n\tthis.pushLinked = true;\n};\n}")),
			rule().add((condition("type", "display"))).add(mark("import")).add(literal("\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal("Notifier extends ")).add(mark("parentType")).add(literal(" {\n\nconstructor(element) {\n    super(element);\n    this.setup();\n};\n\nsetup() {\n\tif (this.element == null || this.pushLinked) return;\n\tsuper.setup();\n\t")).add(mark("notification").multiple("\n")).add(literal("\n\tthis.pushLinked = true;\n};\n}")),
			rule().add((condition("attribute", "extensionOf")), (condition("trigger", "import"))).add(literal("import ")).add(mark("parent", "firstUpperCase")).add(literal("Notifier from \"./")).add(mark("parent", "firstUpperCase")).add(literal("Notifier\"")),
			rule().add((condition("trigger", "import"))).add(literal("import Notifier from \"./Notifier\";")),
			rule().add((condition("attribute", "extensionOf")), (condition("trigger", "parentType"))).add(mark("parent", "firstUpperCase")).add(literal("Notifier")),
			rule().add((condition("trigger", "parentType"))).add(literal("Notifier")),
			rule().add((condition("trigger", "notification"))).add(literal("this.when(\"")).add(mark("name")).add(literal("\")")).add(expression().add(mark("target"))).add(literal(".execute((")).add(expression().add(mark("parameter", "call"))).add(literal(") => this.element.")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter", "value"))).add(literal("));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "call"))).add(literal("parameters")),
			rule().add((condition("type", "parameter")), (condition("trigger", "value"))).add(literal("parameters.v")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "target"))).add(literal(".toSelf()")),
			rule().add((condition("trigger", "target")))
		);
		return this;
	}
}