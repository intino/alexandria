package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.CompilationContext;
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
import io.intino.konos.model.graph.OtherComponents.Header;

import static io.intino.konos.model.graph.BIComponents.*;

public class ComponentRendererFactory {

	public <T extends UIRenderer> T renderer(CompilationContext compilationContext, Component component, TemplateProvider provider, Target target) {
		if (component.i$(Text.class)) return (T) new TextRenderer(compilationContext, component.a$(Text.class), provider, target);
		if (component.i$(Date.class)) return (T) new DateRenderer(compilationContext, component.a$(Date.class), provider, target);
		if (component.i$(File.class)) return (T) new FileRenderer(compilationContext, component.a$(File.class), provider, target);
		if (component.i$(Image.class)) return (T) new ImageRenderer(compilationContext, component.a$(Image.class), provider, target);
		if (component.i$(DataComponents.Number.class)) return (T) new NumberRenderer(compilationContext, component.a$(DataComponents.Number.class), provider, target);
		if (component.i$(DataComponents.Location.class)) return (T) new LocationRenderer(compilationContext, component.a$(DataComponents.Location.class), provider, target);

		if (component.i$(Spinner.class)) return (T) new SpinnerRenderer(compilationContext, component.a$(BIComponents.Spinner.class), provider, target);
		if (component.i$(OtherComponents.Selector.class)) return (T) new SelectorRenderer(compilationContext, component.a$(OtherComponents.Selector.class), provider, target);
		if (component.i$(AbstractSlider.class)) return (T) new SliderRenderer(compilationContext, component.a$(AbstractSlider.class), provider, target);
		if (component.i$(Block.class)) return (T) new BlockRenderer(compilationContext, component.a$(Block.class), provider, target);
		if (component.i$(Chart.class)) return (T) new ChartRenderer(compilationContext, component.a$(Chart.class), provider, target);
		if (component.i$(Dashboard.class)) return (T) new DashboardRenderer(compilationContext, component.a$(BIComponents.Dashboard.class), provider, target);
		if (component.i$(OtherComponents.AbstractDialog.class)) return (T) new DialogRenderer(compilationContext, component.a$(OtherComponents.AbstractDialog.class), provider, target);
		if (component.i$(OtherComponents.BaseIcon.class)) return (T) new IconRenderer(compilationContext, component.a$(OtherComponents.BaseIcon.class), provider, target);
		if (component.i$(OtherComponents.Portal.class)) return (T) new PortalRenderer(compilationContext, component.a$(OtherComponents.Portal.class), provider, target);
		if (component.i$(OtherComponents.User.class)) return (T) new UserRenderer(compilationContext, component.a$(OtherComponents.User.class), provider, target);
		if (component.i$(Stepper.class)) return (T) new StepperRenderer(compilationContext, component.a$(Stepper.class), provider, target);
		if (component.i$(Stepper.Step.class)) return (T) new StepRenderer(compilationContext, component.a$(Stepper.Step.class), provider, target);

		if (component.i$(Template.class)) return (T) new TemplateRenderer(compilationContext, component.a$(Template.class), provider, target);
		if (component.i$(Header.class)) return (T) new HeaderRenderer(compilationContext, component.a$(Header.class), provider, target);

		if (component.i$(CatalogComponents.Grouping.class)) return (T) new GroupingRenderer(compilationContext, component.a$(CatalogComponents.Grouping.class), provider, target);
		if (component.i$(CatalogComponents.Sorting.class)) return (T) new SortingRenderer(compilationContext, component.a$(CatalogComponents.Sorting.class), provider, target);
		if (component.i$(CatalogComponents.Map.class)) return (T) new MapRenderer(compilationContext, component.a$(CatalogComponents.Map.class), provider, target);
		if (component.i$(CatalogComponents.Collection.class)) return (T) new CollectionRenderer(compilationContext, component.a$(CatalogComponents.Collection.class), provider, target);
		if (component.i$(CatalogComponents.Collection.Mold.Heading.class)) return (T) new HeadingRenderer(compilationContext, component.a$(CatalogComponents.Collection.Mold.Heading.class), provider, target);
		if (component.i$(CatalogComponents.Collection.Mold.Item.class)) return (T) new ItemRenderer(compilationContext, component.a$(CatalogComponents.Collection.Mold.Item.class), provider, target);
		if (component.i$(CatalogComponents.SearchBox.class)) return (T) new SearchBoxRenderer(compilationContext, component.a$(CatalogComponents.SearchBox.class), provider, target);

		if (component.i$(Toolbar.class)) return (T) new ToolbarRenderer(compilationContext, component.a$(Toolbar.class), provider, target);
		if (component.i$(OpenPage.class)) return (T) new OpenPageRenderer(compilationContext, component.a$(OpenPage.class), provider, target);
		if (component.i$(OpenSite.class)) return (T) new OpenSiteRenderer(compilationContext, component.a$(OpenSite.class), provider, target);
		if (component.i$(OpenDrawer.class)) return (T) new OpenDrawerRenderer(compilationContext, component.a$(OpenDrawer.class), provider, target);
		if (component.i$(CloseDrawer.class)) return (T) new CloseDrawerRenderer(compilationContext, component.a$(CloseDrawer.class), provider, target);
		if (component.i$(OpenBlock.class)) return (T) new OpenBlockRenderer(compilationContext, component.a$(OpenBlock.class), provider, target);
		if (component.i$(Download.class)) return (T) new DownloadRenderer(compilationContext, component.a$(Download.class), provider, target);
		if (component.i$(DownloadSelection.class)) return (T) new DownloadSelectionRenderer(compilationContext, component.a$(DownloadSelection.class), provider, target);
		if (component.i$(Export.class)) return (T) new ExportRenderer(compilationContext, component.a$(Export.class), provider, target);
		if (component.i$(OpenDialog.class)) return (T) new OpenDialogRenderer(compilationContext, component.a$(OpenDialog.class), provider, target);
		if (component.i$(CloseDialog.class)) return (T) new CloseDialogRenderer(compilationContext, component.a$(CloseDialog.class), provider, target);
		if (component.i$(OperationComponents.Task.class)) return (T) new TaskRenderer(compilationContext, component.a$(OperationComponents.Task.class), provider, target);
		if (component.i$(Operation.class)) return (T) new OperationRenderer(compilationContext, component.a$(Operation.class), provider, target);

		return (T) new ComponentRenderer(compilationContext, component, provider, target);
	}

}
