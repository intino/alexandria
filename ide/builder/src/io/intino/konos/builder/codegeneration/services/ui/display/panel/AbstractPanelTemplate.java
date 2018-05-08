package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationGenTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewGenTemplate;
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
		add(PanelGenTemplate.create().rules());
		add(OperationGenTemplate.create().rules());
		add(ViewGenTemplate.create().rules());
		return this;
	}
}
