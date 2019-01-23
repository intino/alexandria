package io.intino.konos.builder.codegeneration.services.ui.displays.renderers;

import io.intino.konos.builder.codegeneration.services.ui.components.ComponentReferenceTemplate;
import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractDisplayTemplate extends Template {

	protected AbstractDisplayTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractDisplayTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(AbstractDisplaySkeletonTemplate.create().rules());
		add(ComponentReferenceTemplate.create().rules());
		return this;
	}
}