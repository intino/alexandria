package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.ItemRenderer;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.Template;

public class DisplayRendererFactory {

	public <T extends UIRenderer> T renderer(Settings settings, Display display, TemplateProvider provider, Target target) {
		if (display.i$(Template.Desktop.class)) return (T) new DesktopRenderer(settings, display.a$(Template.class), provider, target);
		if (display.i$(Template.class)) {
			ComponentRenderer renderer = new io.intino.konos.builder.codegeneration.ui.displays.components.TemplateRenderer(settings, display.a$(Template.class), provider, target);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(CatalogComponents.Collection.Mold.Item.class)) {
			ItemRenderer renderer = new ItemRenderer(settings, display.a$(CatalogComponents.Collection.Mold.Item.class), provider, target);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(Component.class)) {
			ComponentRenderer renderer = new ComponentRenderer(settings, display.a$(Component.class), provider, target);
			renderer.owner(display);
			return (T) renderer;
		}
		return (T) new DisplayRenderer(settings, display, provider, target);
	}

}
