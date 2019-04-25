package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewTemplate;
import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class PanelTemplate extends Template {

	protected PanelTemplate(Locale locale, LineSeparator lineSeparator) {
		super(locale, lineSeparator);
	}

	public static Template create() {
		return new PanelTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(PanelSkeletonTemplate.create().rules());
		add(OperationTemplate.create().rules());
		add(ViewTemplate.create().rules());
		return this;
	}
}
