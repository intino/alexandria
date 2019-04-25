package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.AbstractOperationTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.AbstractViewTemplate;
import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class AbstractPanelTemplate extends Template {

	protected AbstractPanelTemplate(Locale locale, LineSeparator lineSeparator) {
		super(locale, lineSeparator);
	}

	public static Template create() {
		return new AbstractPanelTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(AbstractPanelSkeletonTemplate.create().rules());
		add(AbstractOperationTemplate.create().rules());
		add(AbstractViewTemplate.create().rules());
		return this;
	}
}
