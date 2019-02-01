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
			rule().add((condition("type", "display & accessible"))).add(literal("import Notifier from \"./Notifier\";\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal("Notifier extends Notifier {\n\nconstructor(element) {\n    super(element);\n    this.setup();\n};\n\nsetup = () => {\n\tif (this.element == null || this.pushLinked) return;\n\tthis.when(\"refreshBaseUrl\").toSelf().execute((parameters) => this.refreshBaseUrl(parameters.value));\n\tthis.when(\"refreshError\").toSelf().execute((parameters) => this.refreshError(parameters.value));\n\tthis.pushLinked = true;\n};\n}")),
			rule().add((condition("type", "display"))).add(literal("import Notifier from \"./Notifier\";\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal("Notifier extends Notifier {\n\nconstructor(element) {\n    super(element);\n    this.setup();\n};\n\nsetup = () => {\n\tif (this.element == null || this.pushLinked) return;\n\t")).add(mark("notification").multiple("\n")).add(literal("\n\tthis.pushLinked = true;\n};\n}")),
			rule().add((condition("trigger", "notification"))).add(literal("this.when(\"")).add(mark("name")).add(literal("\")")).add(expression().add(mark("target"))).add(literal(".execute((")).add(expression().add(mark("parameter", "call"))).add(literal(") => this.")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter", "value"))).add(literal("));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "call"))).add(literal("parameters")),
			rule().add((condition("type", "parameter")), (condition("trigger", "value"))).add(literal("parameters.value")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "target"))).add(literal(".toSelf()")),
			rule().add((condition("trigger", "target")))
		);
		return this;
	}
}