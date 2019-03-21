package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.Template;
import org.siani.itrules.model.Frame;

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
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		element.componentList().forEach(c -> addComponent(c, frame));
		frame.addSlot("componentReferences", componentReferencesFrame());
		return frame;
	}

	private Frame componentReferencesFrame() {
		Frame result = new Frame("componentReferences");
		if (element.i$(Block.class)) result.addTypes("forBlock");
		addComponents(result);
		return result;
	}

	private void addComponents(Frame frame) {
		element.componentList().forEach(c -> addComponent(c, frame));
	}

}
