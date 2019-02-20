package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.ChildComponents.Tab;
import io.intino.konos.model.graph.Desktop;
import org.siani.itrules.model.Frame;

import java.io.File;

@SuppressWarnings("Duplicates")
public class DesktopRenderer extends BaseDisplayRenderer<Desktop> {

	public DesktopRenderer(Settings settings, Desktop display, TemplateProvider provider, Target target) {
		super(settings, display, provider, target);
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		frame.addSlot("appBarId", shortId(element, "appBarId"));
		frame.addSlot("tabBarId", shortId(element, "tabBarId"));
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

}
