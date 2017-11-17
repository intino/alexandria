package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class CatalogTemplate extends Template {

	protected CatalogTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new CatalogTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "desktop & gen"))).add(literal("\n\n")),
			rule().add((condition("type", "desktop & src"))).add(literal("\n\n"))
		);
		return this;
	}
}