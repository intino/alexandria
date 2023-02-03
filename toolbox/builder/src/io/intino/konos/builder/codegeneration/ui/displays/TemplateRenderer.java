package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.Block;
import io.intino.konos.model.Component;
import io.intino.konos.model.Template;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class TemplateRenderer extends BaseDisplayRenderer<Template> {

	public TemplateRenderer(CompilationContext compilationContext, Template display, RendererWriter writer) {
		super(compilationContext, display, writer);
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder frame = super.buildFrame();
		List<Component> components = element.componentList();
		addComponentsImports(components, frame);
		components.forEach(c -> addComponent(c, virtualParent(), frame));
		frame.add("componentReferences", componentReferencesFrame());
		return frame;
	}

	private FrameBuilder componentReferencesFrame() {
		FrameBuilder result = new FrameBuilder("componentReferences");
		if (element.i$(conceptOf(Block.class))) result.add("forBlock");
		addComponents(result);
		return result;
	}

	private void addComponents(FrameBuilder frame) {
		element.componentList().forEach(c -> addComponent(c, virtualParent(), frame));
	}

}
