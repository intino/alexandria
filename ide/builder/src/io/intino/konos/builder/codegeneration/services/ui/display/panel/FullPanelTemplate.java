package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationSrcTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewSrcTemplate;
import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class FullPanelTemplate extends Template {

	protected FullPanelTemplate(Locale locale, LineSeparator lineSeparator) {
		super(locale, lineSeparator);
	}

	public static Template create() {
		return new FullPanelTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(PanelSrcTemplate.create().rules());
		add(OperationSrcTemplate.create().rules());
		add(ViewSrcTemplate.create().rules());
		return this;
	}
}
