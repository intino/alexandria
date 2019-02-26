package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Tab;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Template;
import io.intino.konos.model.graph.desktop.DesktopTemplate;
import org.siani.itrules.model.Frame;

import java.io.File;

@SuppressWarnings("Duplicates")
public class DesktopRenderer extends BaseDisplayRenderer<Template> {

	public DesktopRenderer(Settings settings, Template display, TemplateProvider provider, Target target) {
		super(settings, display, provider, target);
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		DesktopTemplate template = element.asDesktop();
		frame.addSlot("headerId", shortId(element, "headerId"));
		frame.addSlot("tabBarId", shortId(element, "tabBarId"));
		template.header().componentList().forEach(c -> addComponent(c, frame));
		template.tabs().tabList().forEach(t -> addTab(t, frame));
		return frame;
	}

	private void addComponent(Component component, Frame frame) {
		frame.addSlot("component", componentFrame(component));
	}

	private void addTab(Tab tab, Frame frame) {
		frame.addSlot("tab", componentFrame(tab));
	}

}
