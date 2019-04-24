package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.ItemRenderer;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.desktop.DesktopTemplate;

public class DisplayRendererFactory {

	public <T extends UIRenderer> T renderer(Settings settings, Display display, TemplateProvider provider, UIRenderer.Target target) {
		if (display.i$(DesktopTemplate.class)) return (T) new DesktopRenderer(settings, display.a$(Template.class), provider, target);
		if (display.i$(Template.class)) {
			ComponentRenderer renderer = new io.intino.konos.builder.codegeneration.ui.displays.components.TemplateRenderer(settings, display.a$(Template.class), provider, target);
			renderer.owner(display);
			return (T) renderer;
		}
		if (display.i$(ChildComponents.Collection.Mold.Item.class)) {
			ItemRenderer renderer = new ItemRenderer(settings, display.a$(ChildComponents.Collection.Mold.Item.class), provider, target);
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
