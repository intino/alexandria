package io.intino.konos.builder.codegeneration.services.ui.display.catalog;

import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.AbstractOperationTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.AbstractViewTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewTemplate;
import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class AbstractCatalogTemplate extends Template {

	protected AbstractCatalogTemplate(Locale locale, LineSeparator lineSeparator) {
		super(locale, lineSeparator);
	}

	public static Template create() {
		return new AbstractCatalogTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(AbstractCatalogSkeletonTemplate.create().rules());
		add(AbstractOperationTemplate.create().rules());
		add(AbstractViewTemplate.create().rules());
		return this;
	}
}
