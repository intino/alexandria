package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Root;
import io.intino.konos.model.graph.Tab;
import org.siani.itrules.model.Frame;

import java.io.File;

@SuppressWarnings("Duplicates")
public class RootRenderer extends BaseDisplayRenderer<Root> {

	public RootRenderer(Settings settings, Root display, TemplateProvider provider, Target target) {
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
		return frame;
	}

	private void addComponent(Component component, Frame frame) {
		frame.addSlot("component", componentFrame(component));
	}

	private void addTab(Tab tab, Frame frame) {
		frame.addSlot("tab", componentFrame(tab));
	}

	private Frame componentFrame(Component component) {
		return new ComponentRenderer(settings, component, true, element.isDecorated(), templateProvider, target).buildFrame();
	}

}
