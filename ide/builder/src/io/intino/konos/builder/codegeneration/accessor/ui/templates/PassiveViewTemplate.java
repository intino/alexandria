package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PassiveViewTemplate extends Template {

	protected PassiveViewTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PassiveViewTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "notifier"))).add(literal("export default class Notifier {\n\tconstructor(element) {\n        this.element = element;\n        this.pushService = Application.services.pushService;\n    };\n}")),
			rule().add((condition("type", "requester"))).add(literal("export default class Requester {\n\tconstructor(element) {\n\t\tthis.element = element;\n\t\tthis.pushService = Application.services.pushService;\n\t\tthis.fileService = Application.services.fileService;\n\t};\n}"))
		);
		return this;
	}
}