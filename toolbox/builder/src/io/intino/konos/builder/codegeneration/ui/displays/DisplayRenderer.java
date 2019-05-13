package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Display;

import java.io.File;

public class DisplayRenderer<D extends Display> extends BaseDisplayRenderer<D> {

	public DisplayRenderer(Settings settings, D display, TemplateProvider provider, Target target) {
		super(settings, display, provider, target);
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new DisplayUpdater(settings, element, sourceFile);
	}

}
