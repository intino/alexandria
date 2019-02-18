package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Desktop;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.Root;

public class DisplayRendererFactory {

	public <T extends UIRenderer> T renderer(Settings settings, Display display, TemplateProvider provider, UIRenderer.Target target) {
		if (display.i$(Desktop.class)) return (T) new DesktopRenderer(settings, display.a$(Desktop.class), provider, target);
		if (display.i$(Root.class)) return (T) new RootRenderer(settings, display.a$(Root.class), provider, target);
//		if (display.i$(Editor.class)) return new EditorRenderer(settings, display);
		if (display.i$(Component.class)) return (T) new ComponentRenderer(settings, display.a$(Component.class), provider, target);
		return (T) new DisplayRenderer(settings, display, provider, target);
	}

}
