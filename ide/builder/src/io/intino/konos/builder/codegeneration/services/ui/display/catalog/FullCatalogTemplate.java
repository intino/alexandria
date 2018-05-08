package io.intino.konos.builder.codegeneration.services.ui.display.catalog;

import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationSrcTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewSrcTemplate;
import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class FullCatalogTemplate extends Template {

	protected FullCatalogTemplate(Locale locale, LineSeparator lineSeparator) {
		super(locale, lineSeparator);
	}

	public static Template create() {
		return new FullCatalogTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(CatalogSrcTemplate.create().rules());
		add(OperationSrcTemplate.create().rules());
		add(ViewSrcTemplate.create().rules());
		return this;
	}
}
