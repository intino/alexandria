package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.data.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.operation.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.other.*;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.DataComponents.Date;
import io.intino.konos.model.graph.DataComponents.File;
import io.intino.konos.model.graph.DataComponents.Image;
import io.intino.konos.model.graph.DataComponents.Text;
import io.intino.konos.model.graph.OperationComponents.*;
import io.intino.konos.model.graph.OtherComponents.Chart;
import io.intino.konos.model.graph.OtherComponents.Header;

public class ComponentRendererFactory {

	public <T extends UIRenderer> T renderer(Settings settings, Component component, TemplateProvider provider, Target target) {
		if (component.i$(Text.class)) return (T) new TextRenderer(settings, component.a$(Text.class), provider, target);
		if (component.i$(Date.class)) return (T) new DateRenderer(settings, component.a$(Date.class), provider, target);
		if (component.i$(File.class)) return (T) new FileRenderer(settings, component.a$(File.class), provider, target);
		if (component.i$(Image.class)) return (T) new ImageRenderer(settings, component.a$(Image.class), provider, target);
		if (component.i$(DataComponents.Number.class)) return (T) new NumberRenderer(settings, component.a$(DataComponents.Number.class), provider, target);
		if (component.i$(DataComponents.Location.class)) return (T) new LocationRenderer(settings, component.a$(DataComponents.Location.class), provider, target);

		if (component.i$(OtherComponents.Spinner.class)) return (T) new SpinnerRenderer(settings, component.a$(OtherComponents.Spinner.class), provider, target);
		if (component.i$(OtherComponents.Selector.class)) return (T) new SelectorRenderer(settings, component.a$(OtherComponents.Selector.class), provider, target);
		if (component.i$(OtherComponents.AbstractSlider.class)) return (T) new SliderRenderer(settings, component.a$(OtherComponents.AbstractSlider.class), provider, target);
		if (component.i$(Block.class)) return (T) new BlockRenderer(settings, component.a$(Block.class), provider, target);
		if (component.i$(OtherComponents.Chart.class)) return (T) new ChartRenderer(settings, component.a$(Chart.class), provider, target);
		if (component.i$(OtherComponents.Dashboard.class)) return (T) new DashboardRenderer(settings, component.a$(OtherComponents.Dashboard.class), provider, target);
		if (component.i$(OtherComponents.AbstractDialog.class)) return (T) new DialogRenderer(settings, component.a$(OtherComponents.AbstractDialog.class), provider, target);
		if (component.i$(OtherComponents.BaseIcon.class)) return (T) new IconRenderer(settings, component.a$(OtherComponents.BaseIcon.class), provider, target);
		if (component.i$(OtherComponents.Portal.class)) return (T) new PortalRenderer(settings, component.a$(OtherComponents.Portal.class), provider, target);

		if (component.i$(Template.class)) return (T) new TemplateRenderer(settings, component.a$(Template.class), provider, target);
		if (component.i$(Header.class)) return (T) new HeaderRenderer(settings, component.a$(Header.class), provider, target);

		if (component.i$(CatalogComponents.Grouping.class)) return (T) new GroupingRenderer(settings, component.a$(CatalogComponents.Grouping.class), provider, target);
		if (component.i$(CatalogComponents.Sorting.class)) return (T) new SortingRenderer(settings, component.a$(CatalogComponents.Sorting.class), provider, target);
		if (component.i$(CatalogComponents.Map.class)) return (T) new MapRenderer(settings, component.a$(CatalogComponents.Map.class), provider, target);
		if (component.i$(CatalogComponents.Collection.class)) return (T) new CollectionRenderer(settings, component.a$(CatalogComponents.Collection.class), provider, target);
		if (component.i$(CatalogComponents.Collection.Mold.Heading.class)) return (T) new HeadingRenderer(settings, component.a$(CatalogComponents.Collection.Mold.Heading.class), provider, target);
		if (component.i$(CatalogComponents.Collection.Mold.Item.class)) return (T) new ItemRenderer(settings, component.a$(CatalogComponents.Collection.Mold.Item.class), provider, target);
		if (component.i$(CatalogComponents.SearchBox.class)) return (T) new SearchBoxRenderer(settings, component.a$(CatalogComponents.SearchBox.class), provider, target);

		if (component.i$(Toolbar.class)) return (T) new ToolbarRenderer(settings, component.a$(Toolbar.class), provider, target);
		if (component.i$(OpenPage.class)) return (T) new OpenPageRenderer(settings, component.a$(OpenPage.class), provider, target);
		if (component.i$(OpenDrawer.class)) return (T) new OpenDrawerRenderer(settings, component.a$(OpenDrawer.class), provider, target);
		if (component.i$(CloseDrawer.class)) return (T) new CloseDrawerRenderer(settings, component.a$(CloseDrawer.class), provider, target);
		if (component.i$(OpenBlock.class)) return (T) new OpenBlockRenderer(settings, component.a$(OpenBlock.class), provider, target);
		if (component.i$(Download.class)) return (T) new DownloadRenderer(settings, component.a$(Download.class), provider, target);
		if (component.i$(DownloadSelection.class)) return (T) new DownloadSelectionRenderer(settings, component.a$(DownloadSelection.class), provider, target);
		if (component.i$(Export.class)) return (T) new ExportRenderer(settings, component.a$(Export.class), provider, target);
		if (component.i$(OpenDialog.class)) return (T) new OpenDialogRenderer(settings, component.a$(OpenDialog.class), provider, target);
		if (component.i$(CloseDialog.class)) return (T) new CloseDialogRenderer(settings, component.a$(CloseDialog.class), provider, target);
		if (component.i$(OperationComponents.Task.class)) return (T) new TaskRenderer(settings, component.a$(OperationComponents.Task.class), provider, target);
		if (component.i$(Operation.class)) return (T) new OperationRenderer(settings, component.a$(Operation.class), provider, target);

		return (T) new ComponentRenderer(settings, component, provider, target);
	}

}
