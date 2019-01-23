package io.intino.konos.builder.codegeneration.services.ui.displays.renderers;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.services.ui.displays.updaters.DisplayUpdater;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Desktop;
import io.intino.konos.model.graph.Tab;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

@SuppressWarnings("Duplicates")
public class DesktopRenderer extends BaseDisplayRenderer<Desktop> {

	public DesktopRenderer(Settings settings, Desktop display) {
		super(settings, display);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		element.appBar().componentList().forEach(c -> addComponent(c, frame));
		element.tabs().tabList().forEach(t -> addTab(t, frame));
		return frame;
	}

	private void addComponent(Component component, Frame frame) {
		frame.addSlot("component", componentFrame(component));
	}

	private void addTab(Tab tab, Frame frame) {
		frame.addSlot("tab", componentFrame(tab));
	}

	private Frame componentFrame(Component component) {
		return new ComponentRenderer(settings, component, true).buildFrame();
	}

	@Override
	protected Template srcTemplate() {
		if (element.isDecorated()) return null;
		return setup(DisplayTemplate.create());
	}

	@Override
	protected Template genTemplate() {
		return setup(AbstractDesktopTemplate.create());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new DisplayUpdater(settings, element, sourceFile);
	}
}
