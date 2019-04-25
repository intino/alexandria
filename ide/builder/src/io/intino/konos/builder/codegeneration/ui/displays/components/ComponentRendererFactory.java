package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.HeadingRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.ItemRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.data.*;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.ChildComponents.Number;
import io.intino.konos.model.graph.ChildComponents.*;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Template;

public class ComponentRendererFactory {

	public <T extends UIRenderer> T renderer(Settings settings, Component component, TemplateProvider provider, UIRenderer.Target target) {
		if (component.i$(Text.class)) return (T) new TextRenderer(settings, component.a$(Text.class), provider, target);
		if (component.i$(Date.class)) return (T) new DateRenderer(settings, component.a$(Date.class), provider, target);
		if (component.i$(File.class)) return (T) new FileRenderer(settings, component.a$(File.class), provider, target);
		if (component.i$(Image.class)) return (T) new ImageRenderer(settings, component.a$(Image.class), provider, target);
		if (component.i$(Number.class)) return (T) new NumberRenderer(settings, component.a$(Number.class), provider, target);

		if (component.i$(Spinner.class)) return (T) new SpinnerRenderer(settings, component.a$(Spinner.class), provider, target);
		if (component.i$(Template.class)) return (T) new TemplateRenderer(settings, component.a$(Template.class), provider, target);
		if (component.i$(Block.class)) return (T) new BlockRenderer(settings, component.a$(Block.class), provider, target);
		if (component.i$(Header.class)) return (T) new HeaderRenderer(settings, component.a$(Header.class), provider, target);
		if (component.i$(Chart.class)) return (T) new ChartRenderer(settings, component.a$(Chart.class), provider, target);
		if (component.i$(Collection.class)) return (T) new CollectionRenderer(settings, component.a$(Collection.class), provider, target);
		if (component.i$(Collection.Mold.Heading.class)) return (T) new HeadingRenderer(settings, component.a$(Collection.Mold.Heading.class), provider, target);
		if (component.i$(Collection.Mold.Item.class)) return (T) new ItemRenderer(settings, component.a$(Collection.Mold.Item.class), provider, target);

		if (component.i$(Toolbar.class)) return (T) new ToolbarRenderer(settings, component.a$(Toolbar.class), provider, target);
		if (component.i$(OpenPage.class)) return (T) new OpenPageRenderer(settings, component.a$(OpenPage.class), provider, target);
		if (component.i$(Operation.class)) return (T) new OperationRenderer(settings, component.a$(Operation.class), provider, target);

		return (T) new ComponentRenderer(settings, component, provider, target);
	}

}
