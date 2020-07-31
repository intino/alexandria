package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.ItemRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.Template;

public class DisplayRendererFactory {

	public <T extends UIRenderer> T renderer(CompilationContext compilationContext, Display display, TemplateProvider provider, Target target) {
		if (display.i$(Template.Desktop.class)) return (T) new DesktopRenderer(compilationContext, display.a$(Template.class), provider, target);
		if (display.i$(Template.class)) {
			ComponentRenderer renderer = new io.intino.konos.builder.codegeneration.ui.displays.components.TemplateRenderer(compilationContext, display.a$(Template.class), provider, target);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(CatalogComponents.Collection.class)) {
			ComponentRenderer renderer = new io.intino.konos.builder.codegeneration.ui.displays.components.CollectionRenderer(compilationContext, display.a$(CatalogComponents.Collection.class), provider, target);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(CatalogComponents.Collection.Mold.Item.class)) {
			ItemRenderer renderer = new ItemRenderer(compilationContext, display.a$(CatalogComponents.Collection.Mold.Item.class), provider, target);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(Component.class)) {
			ComponentRenderer renderer = new ComponentRenderer(compilationContext, display.a$(Component.class), provider, target);
			renderer.owner(display);
			return (T) renderer;
		}
		return (T) new DisplayRenderer(compilationContext, display, provider, target);
	}

}
