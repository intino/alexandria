package io.intino.konos.builder.codegeneration.services.ui.display.catalog;

import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewTemplate;
import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class CatalogTemplate extends Template {

	protected CatalogTemplate(Locale locale, LineSeparator lineSeparator) {
		super(locale, lineSeparator);
	}

	public static Template create() {
		return new CatalogTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(CatalogSkeletonTemplate.create().rules());
		add(OperationTemplate.create().rules());
		add(ViewTemplate.create().rules());
		return this;
	}
}
