package io.intino.konos.builder.codegeneration.accessor.ui.mold;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MoldLayoutTemplate extends Template {

	protected MoldLayoutTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MoldLayoutTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("trigger", "mold"))).add(literal("\n"))
		);
		return this;
	}
}