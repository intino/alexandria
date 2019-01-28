package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class AbstractDesktopTemplate extends Template {

	protected AbstractDesktopTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractDesktopTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(AbstractDesktopSkeletonTemplate.create().rules());
		add(ComponentReferenceTemplate.create().rules());
		return this;
	}
}