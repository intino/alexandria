package io.intino.konos.builder.codegeneration.services.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.UIRenderer;
import io.intino.konos.builder.codegeneration.services.ui.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.services.ui.displays.renderers.DisplayRenderer;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Display;

public class DisplayRendererFactory {

	public <T extends UIRenderer> T renderer(Settings settings, Display display) {
//		if (display.i$(Desktop.class)) return new DesktopRenderer(settings, display);
//		if (display.i$(Editor.class)) return new EditorRenderer(settings, display);
		if (display.i$(Component.class)) return (T) new ComponentRenderer(settings, display.a$(Component.class));
		return (T) new DisplayRenderer(settings, display);
	}

}
