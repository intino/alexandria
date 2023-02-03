package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.Display;

import java.io.File;

public class DisplayRenderer<D extends Display> extends BaseDisplayRenderer<D> {

	public DisplayRenderer(CompilationContext compilationContext, D display, RendererWriter provider) {
		super(compilationContext, display, provider);
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
//		return new DisplayUpdater(settings, element, sourceFile);
	}

}
