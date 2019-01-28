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
			rule().add((condition("type", "display & accessible"))).add(literal("import Notifier from \"./Notifier\";\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal("Notifier extends Notifier {\n\nconstructor(element) {\n    super(element);\n    this.setup();\n};\n\nsetup = () => {\n    this.pushService.listen((message) => {\n    \tif (message.target !== this.element.id) return;\n\t\tif (message.operation === \"refreshBaseUrl\")\n\t\t\tthis.refreshBaseUrl(message.parameters.value);\n\t\telse if (message.operation === \"refreshError\")\n\t\t\tthis.refreshError(message.parameters.value);\n    });\n};\n}")),
			rule().add((condition("type", "display"))).add(literal("import Notifier from \"./Notifier\";\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal("Notifier extends Notifier {\n\nconstructor(element) {\n    super(element);\n    this.setup();\n};\n\nsetup = () => {\n    this.pushService.listen((message) => {\n    \tif (message.target !== this.element.id) return;\n    \t")).add(mark("notification").multiple("\n")).add(literal("\n    });\n};\n}")),
			rule().add((condition("trigger", "notification"))).add(literal("if (message.operation === \"")).add(mark("name")).add(literal("\")\n\tthis.")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter"))).add(literal(");")),
			rule().add((condition("trigger", "parameter"))).add(literal("message.parameters.value"))
		);
		return this;
	}
}