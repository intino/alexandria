package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.Template;

import java.io.File;

public class TemplateRenderer extends BaseDisplayRenderer<Template> {

	public TemplateRenderer(Settings settings, Template display, TemplateProvider provider, Target target) {
		super(settings, display, provider, target);
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder frame = super.frameBuilder();
		element.componentList().forEach(c -> addComponent(c, frame));
		frame.add("componentReferences", componentReferencesFrame());
		return frame;
	}

	private FrameBuilder componentReferencesFrame() {
		FrameBuilder result = new FrameBuilder("componentReferences");
		if (element.i$(Block.class)) result.add("forBlock");
		addComponents(result);
		return result;
	}

	private void addComponents(FrameBuilder frame) {
		element.componentList().forEach(c -> addComponent(c, frame));
	}

}
