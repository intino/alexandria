package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.ItemRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents;
import io.intino.konos.model.Component;
import io.intino.konos.model.Display;
import io.intino.konos.model.Template;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class DisplayRendererFactory {

	@SuppressWarnings("unchecked")
	public <T extends UIRenderer> T renderer(CompilationContext compilationContext, Display display, RendererWriter writer) {
		if (display.i$(conceptOf(Template.Desktop.class))) return (T) new DesktopRenderer(compilationContext, display.a$(Template.class), writer);
		if (display.i$(conceptOf(Template.class))) {
			ComponentRenderer<?> renderer = new io.intino.konos.builder.codegeneration.ui.displays.components.TemplateRenderer(compilationContext, display.a$(Template.class), writer);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(conceptOf(CatalogComponents.Collection.class))) {
			ComponentRenderer<?> renderer = new io.intino.konos.builder.codegeneration.ui.displays.components.CollectionRenderer<>(compilationContext, display.a$(CatalogComponents.Collection.class), writer);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) {
			ItemRenderer renderer = new ItemRenderer(compilationContext, display.a$(CatalogComponents.Moldable.Mold.Item.class), writer);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(conceptOf(Component.class))) {
			ComponentRenderer<?> renderer = new ComponentRenderer<>(compilationContext, display.a$(Component.class), writer);
			renderer.owner(display);
			return (T) renderer;
		}
		return (T) new DisplayRenderer<>(compilationContext, display, writer);
	}

}
