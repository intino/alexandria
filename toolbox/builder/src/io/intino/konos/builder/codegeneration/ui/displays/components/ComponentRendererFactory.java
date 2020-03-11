package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.data.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.actionable.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.other.*;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.DataComponents.Date;
import io.intino.konos.model.graph.DataComponents.File;
import io.intino.konos.model.graph.DataComponents.Image;
import io.intino.konos.model.graph.DataComponents.Text;
import io.intino.konos.model.graph.InteractionComponents.Actionable;
import io.intino.konos.model.graph.InteractionComponents.Toolbar;
import io.intino.konos.model.graph.OtherComponents.Header;

public class ComponentRendererFactory {

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T extends UIRenderer> T renderer(CompilationContext context, Component component, TemplateProvider provider, Target target) {
		if (component.i$(Text.class)) return (T) new TextRenderer(context, component.a$(Text.class), provider, target);
		if (component.i$(Date.class)) return (T) new DateRenderer(context, component.a$(Date.class), provider, target);
		if (component.i$(File.class)) return (T) new FileRenderer(context, component.a$(File.class), provider, target);
		if (component.i$(Image.class)) return (T) new ImageRenderer(context, component.a$(Image.class), provider, target);
		if (component.i$(DataComponents.Number.class)) return (T) new NumberRenderer(context, component.a$(DataComponents.Number.class), provider, target);
		if (component.i$(DataComponents.Location.class)) return (T) new LocationRenderer(context, component.a$(DataComponents.Location.class), provider, target);

		if (component.i$(VisualizationComponents.Spinner.class)) return (T) new SpinnerRenderer(context, component.a$(VisualizationComponents.Spinner.class), provider, target);
		if (component.i$(OtherComponents.Selector.class)) return (T) new SelectorRenderer(context, component.a$(OtherComponents.Selector.class), provider, target);
		if (component.i$(VisualizationComponents.AbstractSlider.class)) return (T) new SliderRenderer(context, component.a$(VisualizationComponents.AbstractSlider.class), provider, target);
		if (component.i$(Block.class)) return (T) new BlockRenderer(context, component.a$(Block.class), provider, target);
		if (component.i$(VisualizationComponents.Chart.class)) return (T) new ChartRenderer(context, component.a$(VisualizationComponents.Chart.class), provider, target);
		if (component.i$(VisualizationComponents.Dashboard.class)) return (T) new DashboardRenderer(context, component.a$(VisualizationComponents.Dashboard.class), provider, target);
		if (component.i$(OtherComponents.AbstractDialog.class)) return (T) new DialogRenderer(context, component.a$(OtherComponents.AbstractDialog.class), provider, target);
		if (component.i$(OtherComponents.BaseIcon.class)) return (T) new IconRenderer(context, component.a$(OtherComponents.BaseIcon.class), provider, target);
		if (component.i$(OtherComponents.Portal.class)) return (T) new PortalRenderer(context, component.a$(OtherComponents.Portal.class), provider, target);
		if (component.i$(OtherComponents.User.class)) return (T) new UserRenderer(context, component.a$(OtherComponents.User.class), provider, target);
		if (component.i$(OtherComponents.ProxyStamp.class)) return (T) new ProxyStampRenderer(context, component.a$(OtherComponents.ProxyStamp.class), provider, target);
		if (component.i$(VisualizationComponents.Stepper.class)) return (T) new StepperRenderer(context, component.a$(VisualizationComponents.Stepper.class), provider, target);
		if (component.i$(VisualizationComponents.Stepper.Step.class)) return (T) new StepRenderer(context, component.a$(VisualizationComponents.Stepper.Step.class), provider, target);

		if (component.i$(Template.class)) return (T) new TemplateRenderer(context, component.a$(Template.class), provider, target);
		if (component.i$(Header.class)) return (T) new HeaderRenderer(context, component.a$(Header.class), provider, target);

		if (component.i$(CatalogComponents.Grouping.class)) return (T) new GroupingRenderer(context, component.a$(CatalogComponents.Grouping.class), provider, target);
		if (component.i$(CatalogComponents.Sorting.class)) return (T) new SortingRenderer(context, component.a$(CatalogComponents.Sorting.class), provider, target);
		if (component.i$(CatalogComponents.Map.class)) return (T) new MapRenderer(context, component.a$(CatalogComponents.Map.class), provider, target);
		if (component.i$(CatalogComponents.Collection.class)) return (T) new CollectionRenderer(context, component.a$(CatalogComponents.Collection.class), provider, target);
		if (component.i$(CatalogComponents.Collection.Mold.Heading.class)) return (T) new HeadingRenderer(context, component.a$(CatalogComponents.Collection.Mold.Heading.class), provider, target);
		if (component.i$(CatalogComponents.Collection.Mold.Item.class)) return (T) new ItemRenderer(context, component.a$(CatalogComponents.Collection.Mold.Item.class), provider, target);
		if (component.i$(CatalogComponents.SearchBox.class)) return (T) new SearchBoxRenderer(context, component.a$(CatalogComponents.SearchBox.class), provider, target);

		if (component.i$(Toolbar.class)) return (T) new ToolbarRenderer(context, component.a$(Toolbar.class), provider, target);
		if (component.i$(Actionable.OpenPage.class)) return (T) new OpenPageRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.OpenSite.class)) return (T) new OpenSiteRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.OpenDrawer.class)) return (T) new OpenDrawerRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.CloseDrawer.class)) return (T) new CloseDrawerRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.OpenBlock.class)) return (T) new OpenBlockRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.Download.class)) return (T) new DownloadRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.Export.class)) return (T) new ExportRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.OpenDialog.class)) return (T) new OpenDialogRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.CloseDialog.class)) return (T) new CloseDialogRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.Action.class)) return (T) new ActionRenderer(context, component.a$(Actionable.class), provider, target);
		if (component.i$(Actionable.class)) return (T) new ActionableRenderer(context, component.a$(Actionable.class), provider, target);

		return (T) new ComponentRenderer(context, component, provider, target);
	}

}
