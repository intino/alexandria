package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplayBoxTemplate extends Template {

	protected DisplayBoxTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplayBoxTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "box & decorated")), (condition("trigger", "extension"))).add(literal("B extends Box")),
			rule().add((condition("type", "box")), (condition("trigger", "extension"))),
			rule().add((condition("type", "box & decorated")), (condition("trigger", "extensionTagged"))).add(literal("<B extends Box>")),
			rule().add((condition("type", "box")), (condition("trigger", "extensionTagged"))),
			rule().add((condition("type", "box & decorated")), (condition("trigger", "type"))).add(literal("B")),
			rule().add((condition("type", "box")), (condition("trigger", "type"))).add(mark("box", "firstUpperCase")).add(literal("Box"))
		);
		return this;
	}
}