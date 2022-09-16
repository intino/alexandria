package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.Block;
import io.intino.konos.model.Component;
import io.intino.konos.model.Template;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

@SuppressWarnings("Duplicates")
public class DesktopRenderer extends BaseDisplayRenderer<Template> {

	public DesktopRenderer(CompilationContext compilationContext, Template display, TemplateProvider provider, Target target) {
		super(compilationContext, display, provider, target);
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder result = super.buildFrame();
		result.add("headerId", shortId(element, "headerId"));
		result.add("tabBarId", shortId(element, "tabBarId"));
		addComponents(result);
		result.add("componentReferences", componentReferences());
		return result;
	}

	private FrameBuilder componentReferences() {
		FrameBuilder result = new FrameBuilder("componentReferences");
		if (element.i$(conceptOf(Block.class))) result.add("forBlock");
		addComponents(result);
		return result;
	}

	private void addComponents(FrameBuilder builder) {
		Template.Desktop template = element.asDesktop();
		List<Component> components = template.header().componentList();
		addComponentsImports(components, builder);
		components.forEach(c -> addComponent(c, virtualParent(), builder));
	}
}
