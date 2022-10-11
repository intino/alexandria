package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.actionable.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.data.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.other.*;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.*;
import io.intino.konos.model.DataComponents.*;
import io.intino.konos.model.InteractionComponents.Actionable;
import io.intino.konos.model.InteractionComponents.Toolbar;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;
import static io.intino.konos.model.VisualizationComponents.*;

public class ComponentRendererFactory {
	private static final Map<String, Function<Configuration, UIRenderer>> map = new LinkedHashMap<>();

	static {
		map.put(conceptOf(Text.class), config -> new TextRenderer(config.context(), config.component().a$(Text.class), config.provider(), config.target()));
		map.put(conceptOf(Date.class), config -> new DateRenderer(config.context(), config.component().a$(Date.class), config.provider(), config.target()));
		map.put(conceptOf(File.class), config -> new FileRenderer(config.context(), config.component().a$(File.class), config.provider(), config.target()));
		map.put(conceptOf(Image.class), config -> new ImageRenderer(config.context(), config.component().a$(Image.class), config.provider(), config.target()));
		map.put(conceptOf(DigitalSignature.class), config -> new DigitalSignatureRenderer(config.context(), config.component().a$(DigitalSignature.class), config.provider(), config.target()));
		map.put(conceptOf(DataComponents.Number.class), config -> new NumberRenderer(config.context(), config.component().a$(DataComponents.Number.class), config.provider(), config.target()));
		map.put(conceptOf(DataComponents.Location.class), config -> new LocationRenderer(config.context(), config.component().a$(DataComponents.Location.class), config.provider(), config.target()));
		map.put(conceptOf(Spinner.class), config -> new SpinnerRenderer(config.context(), config.component().a$(Spinner.class), config.provider(), config.target()));
		map.put(conceptOf(OtherComponents.Selector.class), config -> new SelectorRenderer(config.context(), config.component().a$(OtherComponents.Selector.class), config.provider(), config.target()));
		map.put(conceptOf(AbstractSlider.class), config -> new SliderRenderer(config.context(), config.component().a$(AbstractSlider.class), config.provider(), config.target()));
		map.put(conceptOf(Block.class), config -> new BlockRenderer(config.context(), config.component().a$(Block.class), config.provider(), config.target()));
		map.put(conceptOf(Chart.class), config -> new ChartRenderer(config.context(), config.component().a$(Chart.class), config.provider(), config.target()));
		map.put(conceptOf(Dashboard.class), config -> new DashboardRenderer(config.context(), config.component().a$(Dashboard.class), config.provider(), config.target()));
		map.put(conceptOf(AppDirectory.class), config -> new AppDirectoryRenderer(config.context(), config.component().a$(AppDirectory.class), config.provider(), config.target()));
		map.put(conceptOf(OtherComponents.AbstractDialog.class), config -> new DialogRenderer(config.context(), config.component().a$(OtherComponents.AbstractDialog.class), config.provider(), config.target()));
		map.put(conceptOf(OtherComponents.BaseIcon.class), config -> new IconRenderer(config.context(), config.component().a$(OtherComponents.BaseIcon.class), config.provider(), config.target()));
		map.put(conceptOf(OtherComponents.User.class), config -> new UserRenderer(config.context(), config.component().a$(OtherComponents.User.class), config.provider(), config.target()));
		map.put(conceptOf(OtherComponents.ProxyStamp.class), config -> new ProxyStampRenderer(config.context(), config.component().a$(OtherComponents.ProxyStamp.class), config.provider(), config.target()));
		map.put(conceptOf(OtherComponents.Frame.class), config -> new FrameRenderer(config.context(), config.component().a$(OtherComponents.Frame.class), config.provider(), config.target()));
		map.put(conceptOf(OtherComponents.MicroSite.class), config -> new MicroSiteRenderer(config.context(), config.component().a$(OtherComponents.MicroSite.class), config.provider(), config.target()));
		map.put(conceptOf(OtherComponents.HtmlViewer.class), config -> new HtmlViewerRenderer(config.context(), config.component().a$(OtherComponents.HtmlViewer.class), config.provider(), config.target()));
		map.put(conceptOf(Stepper.class), config -> new StepperRenderer(config.context(), config.component().a$(Stepper.class), config.provider(), config.target()));
		map.put(conceptOf(Stepper.Step.class), config -> new StepRenderer(config.context(), config.component().a$(Stepper.Step.class), config.provider(), config.target()));
		map.put(conceptOf(Template.class), config -> new TemplateRenderer(config.context(), config.component().a$(Template.class), config.provider(), config.target()));
		map.put(conceptOf(Header.class), config -> new HeaderRenderer(config.context(), config.component().a$(Header.class), config.provider(), config.target()));
		map.put(conceptOf(CatalogComponents.Grouping.class), config -> new GroupingRenderer(config.context(), config.component().a$(CatalogComponents.Grouping.class), config.provider(), config.target()));
		map.put(conceptOf(CatalogComponents.GroupingToolbar.class), config -> new GroupingToolbarRenderer(config.context(), config.component().a$(CatalogComponents.GroupingToolbar.class), config.provider(), config.target()));
		map.put(conceptOf(CatalogComponents.Sorting.class), config -> new SortingRenderer(config.context(), config.component().a$(CatalogComponents.Sorting.class), config.provider(), config.target()));
		map.put(conceptOf(CatalogComponents.Map.class), config -> new MapRenderer(config.context(), config.component().a$(CatalogComponents.Map.class), config.provider(), config.target()));
		map.put(conceptOf(CatalogComponents.Collection.class), config -> new CollectionRenderer<>(config.context(), config.component().a$(CatalogComponents.Collection.class), config.provider(), config.target()));
		map.put(conceptOf(CatalogComponents.Moldable.Mold.Heading.class), config -> new HeadingRenderer(config.context(), config.component().a$(CatalogComponents.Moldable.Mold.Heading.class), config.provider(), config.target()));
		map.put(conceptOf(CatalogComponents.Moldable.Mold.Item.class), config -> new ItemRenderer(config.context(), config.component().a$(CatalogComponents.Moldable.Mold.Item.class), config.provider(), config.target()));
		map.put(conceptOf(CatalogComponents.SearchBox.class), config -> new SearchBoxRenderer(config.context(), config.component().a$(CatalogComponents.SearchBox.class), config.provider(), config.target()));
		map.put(conceptOf(Toolbar.class), config -> new ToolbarRenderer(config.context(), config.component().a$(Toolbar.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.OpenPage.class), config -> new OpenPageRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.OpenSite.class), config -> new OpenSiteRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.OpenDrawer.class), config -> new OpenDrawerRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.OpenLayer.class), config -> new OpenLayerRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.CloseLayer.class), config -> new CloseLayerRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.CloseDrawer.class), config -> new CloseDrawerRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.OpenBlock.class), config -> new OpenBlockRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.CloseBlock.class), config -> new CloseBlockRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.Download.class), config -> new DownloadRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.Export.class), config -> new ExportRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.OpenDialog.class), config -> new OpenDialogRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.OpenPopover.class), config -> new OpenPopoverRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.CloseDialog.class), config -> new CloseDialogRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.SelectNextItem.class), config -> new SelectNextItemRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.SelectPreviousItem.class), config -> new SelectPreviousItemRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.Action.class), config -> new ActionRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
		map.put(conceptOf(Actionable.class), config -> new ActionableRenderer(config.context(), config.component().a$(Actionable.class), config.provider(), config.target()));
	}

	@SuppressWarnings({"unchecked"})
	public static <T extends UIRenderer> T renderer(CompilationContext context, Component component, TemplateProvider provider, Target target) {
		return (T) map.entrySet().stream().filter(e -> component.i$(e.getKey())).map(e -> e.getValue().apply(config(context, component, provider, target))).findFirst().orElse(defaultRenderer(context, component, provider, target));
	}

	private static UIRenderer defaultRenderer(CompilationContext context, Component component, TemplateProvider provider, Target target) {
		return new ComponentRenderer<>(context, component, provider, target);
	}

	private static Configuration config(CompilationContext context, Component component, TemplateProvider provider, Target target) {
		return new Configuration() {
			@Override
			public CompilationContext context() {
				return context;
			}

			@Override
			public Component component() {
				return component;
			}

			@Override
			public TemplateProvider provider() {
				return provider;
			}

			@Override
			public Target target() {
				return target;
			}
		};
	}

	public interface Configuration {
		CompilationContext context();
		Component component();
		TemplateProvider provider();
		Target target();
	}

}
