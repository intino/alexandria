package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.Template;
import io.intino.konos.model.graph.desktop.DesktopTemplate;

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
	public FrameBuilder frameBuilder() {
		FrameBuilder result = super.frameBuilder();
		DesktopTemplate template = element.asDesktop();
		result.add("headerId", shortId(element, "headerId"));
		result.add("tabBarId", shortId(element, "tabBarId"));
		addComponents(result);
//		template.tabs().tabList().forEach(t -> addTab(t, frame));
		result.add("componentReferences", componentReferences());
		return result;
	}

	private FrameBuilder componentReferences() {
		FrameBuilder result = new FrameBuilder("componentReferences");
		if (element.i$(Block.class)) result.add("forBlock");
		addComponents(result);
		return result;
	}

	private void addComponents(FrameBuilder builder) {
		DesktopTemplate template = element.asDesktop();
		template.header().componentList().forEach(c -> addComponent(c, builder));
	}

//	private void addTab(Tab tab, Frame frame) {
//		frame.addSlot("tab", componentFrame(tab));
//	}

}
