package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.Template;
import io.intino.konos.model.graph.desktop.DesktopTemplate;

public class DisplayRendererFactory {

	public <T extends UIRenderer> T renderer(Settings settings, Display display, TemplateProvider provider, UIRenderer.Target target) {
		if (display.i$(DesktopTemplate.class)) return (T) new DesktopRenderer(settings, display.a$(Template.class), provider, target);
		if (display.i$(Template.class)) return (T) new TemplateRenderer(settings, display.a$(Template.class), provider, target);
		if (display.i$(Component.class)) return (T) new ComponentRenderer(settings, display.a$(Component.class), provider, target);
		return (T) new DisplayRenderer(settings, display, provider, target);
	}

}
