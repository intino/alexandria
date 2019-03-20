package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.ChildComponents.Date;
import io.intino.konos.model.graph.ChildComponents.Header;
import io.intino.konos.model.graph.ChildComponents.Image;
import io.intino.konos.model.graph.ChildComponents.Text;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Mold;

public class ComponentRendererFactory {

	public <T extends UIRenderer> T renderer(Settings settings, Component component, TemplateProvider provider, UIRenderer.Target target) {
		if (component.i$(Text.class)) return (T) new TextRenderer(settings, component.a$(Text.class), provider, target);
		if (component.i$(Mold.class)) return (T) new MoldRenderer(settings, component.a$(Mold.class), provider, target);
		if (component.i$(Block.class)) return (T) new BlockRenderer(settings, component.a$(Block.class), provider, target);
		if (component.i$(Header.class)) return (T) new HeaderRenderer(settings, component.a$(Header.class), provider, target);
		if (component.i$(Date.class)) return (T) new DateRenderer(settings, component.a$(Date.class), provider, target);
		if (component.i$(Image.class)) return (T) new ImageRenderer(settings, component.a$(Image.class), provider, target);
		return (T) new ComponentRenderer(settings, component, provider, target);
	}

}
