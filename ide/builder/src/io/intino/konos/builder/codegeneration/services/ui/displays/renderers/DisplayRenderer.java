package io.intino.konos.builder.codegeneration.services.ui.displays.renderers;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.displays.updaters.DisplayUpdater;
import io.intino.konos.model.graph.Display;
import org.siani.itrules.Template;

import java.io.File;

@SuppressWarnings("Duplicates")
public class DisplayRenderer<D extends Display> extends BaseDisplayRenderer<D> {

	public DisplayRenderer(Settings settings, D display) {
		super(settings, display);
	}

	@Override
	protected Template srcTemplate() {
		if (!element.isDecorated()) return null;
		return setup(DisplayTemplate.create());
	}

	@Override
	protected Template genTemplate() {
		return setup(AbstractDisplayTemplate.create());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new DisplayUpdater(settings, element, sourceFile);
	}
}
