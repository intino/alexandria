package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.actionable.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.collection.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.data.*;
import io.intino.konos.builder.codegeneration.ui.displays.components.other.*;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.ActionableComponents.Actionable;
import io.intino.konos.dsl.*;
import io.intino.konos.dsl.DataComponents.Date;
import io.intino.konos.dsl.DataComponents.File;
import io.intino.konos.dsl.DataComponents.Image;
import io.intino.konos.dsl.DataComponents.Text;
import io.intino.konos.dsl.InteractionComponents.Toolbar;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;
import static io.intino.konos.dsl.VisualizationComponents.*;

public class ComponentRendererFactory {
	private static final Map<String, Function<Configuration, UIRenderer>> map = new LinkedHashMap<>();

	static {
		map.put(conceptOf(Text.class), config -> new TextRenderer(config.context(), config.component().a$(Text.class), config.provider()));
		map.put(conceptOf(Date.class), config -> new DateRenderer(config.context(), config.component().a$(Date.class), config.provider()));
		map.put(conceptOf(File.class), config -> new FileRenderer(config.context(), config.component().a$(File.class), config.provider()));
		map.put(conceptOf(Image.class), config -> new ImageRenderer(config.context(), config.component().a$(Image.class), config.provider()));
		map.put(conceptOf(DocumentEditor.class), config -> new DocumentEditorRenderer(config.context(), config.component().a$(DocumentEditor.class), config.provider()));
		map.put(conceptOf(DateNavigator.class), config -> new DateNavigatorRenderer(config.context(), config.component().a$(DateNavigator.class), config.provider()));
		map.put(conceptOf(Reel.class), config -> new ReelRenderer(config.context(), config.component().a$(Reel.class), config.provider()));
		map.put(conceptOf(Eventline.class), config -> new EventlineRenderer(config.context(), config.component().a$(Eventline.class), config.provider()));
		map.put(conceptOf(Timeline.class), config -> new TimelineRenderer(config.context(), config.component().a$(Timeline.class), config.provider()));
		map.put(conceptOf(DataComponents.Number.class), config -> new NumberRenderer(config.context(), config.component().a$(DataComponents.Number.class), config.provider()));
		map.put(conceptOf(DataComponents.Location.class), config -> new LocationRenderer(config.context(), config.component().a$(DataComponents.Location.class), config.provider()));
		map.put(conceptOf(Spinner.class), config -> new SpinnerRenderer(config.context(), config.component().a$(Spinner.class), config.provider()));
		map.put(conceptOf(OtherComponents.Selector.class), config -> new SelectorRenderer(config.context(), config.component().a$(OtherComponents.Selector.class), config.provider()));
		map.put(conceptOf(RangeSlider.class), config -> new RangeSliderRenderer(config.context(), config.component().a$(RangeSlider.class), config.provider()));
		map.put(conceptOf(Slider.class), config -> new SliderRenderer(config.context(), config.component().a$(AbstractSlider.class), config.provider()));
		map.put(conceptOf(TemporalSlider.class), config -> new SliderRenderer(config.context(), config.component().a$(AbstractSlider.class), config.provider()));
		map.put(conceptOf(Block.class), config -> new BlockRenderer(config.context(), config.component().a$(Block.class), config.provider()));
		map.put(conceptOf(Chart.class), config -> new ChartRenderer(config.context(), config.component().a$(Chart.class), config.provider()));
		map.put(conceptOf(Dashboard.class), config -> new DashboardRenderer(config.context(), config.component().a$(Dashboard.class), config.provider()));
		map.put(conceptOf(AppDirectory.class), config -> new AppDirectoryRenderer(config.context(), config.component().a$(AppDirectory.class), config.provider()));
		map.put(conceptOf(OtherComponents.AbstractDialog.class), config -> new DialogRenderer(config.context(), config.component().a$(OtherComponents.AbstractDialog.class), config.provider()));
		map.put(conceptOf(OtherComponents.BaseIcon.class), config -> new IconRenderer(config.context(), config.component().a$(OtherComponents.BaseIcon.class), config.provider()));
		map.put(conceptOf(OtherComponents.User.class), config -> new UserRenderer(config.context(), config.component().a$(OtherComponents.User.class), config.provider()));
		map.put(conceptOf(OtherComponents.ProxyStamp.class), config -> new ProxyStampRenderer(config.context(), config.component().a$(OtherComponents.ProxyStamp.class), config.provider()));
		map.put(conceptOf(OtherComponents.Frame.class), config -> new FrameRenderer(config.context(), config.component().a$(OtherComponents.Frame.class), config.provider()));
		map.put(conceptOf(OtherComponents.MicroSite.class), config -> new MicroSiteRenderer(config.context(), config.component().a$(OtherComponents.MicroSite.class), config.provider()));
		map.put(conceptOf(OtherComponents.HtmlViewer.class), config -> new HtmlViewerRenderer(config.context(), config.component().a$(OtherComponents.HtmlViewer.class), config.provider()));
		map.put(conceptOf(Stepper.class), config -> new StepperRenderer(config.context(), config.component().a$(Stepper.class), config.provider()));
		map.put(conceptOf(Stepper.Step.class), config -> new StepRenderer(config.context(), config.component().a$(Stepper.Step.class), config.provider()));
		map.put(conceptOf(Template.class), config -> new TemplateRenderer(config.context(), config.component().a$(Template.class), config.provider()));
		map.put(conceptOf(Header.class), config -> new HeaderRenderer(config.context(), config.component().a$(Header.class), config.provider()));
		map.put(conceptOf(CatalogComponents.Grouping.class), config -> new GroupingRenderer(config.context(), config.component().a$(CatalogComponents.Grouping.class), config.provider()));
		map.put(conceptOf(CatalogComponents.GroupingToolbar.class), config -> new GroupingToolbarRenderer(config.context(), config.component().a$(CatalogComponents.GroupingToolbar.class), config.provider()));
		map.put(conceptOf(CatalogComponents.Sorting.class), config -> new SortingRenderer(config.context(), config.component().a$(CatalogComponents.Sorting.class), config.provider()));
		map.put(conceptOf(CatalogComponents.Map.class), config -> new MapRenderer(config.context(), config.component().a$(CatalogComponents.Map.class), config.provider()));
		map.put(conceptOf(CatalogComponents.Collection.class), config -> new CollectionRenderer<>(config.context(), config.component().a$(CatalogComponents.Collection.class), config.provider()));
		map.put(conceptOf(CatalogComponents.Moldable.Mold.Heading.class), config -> new HeadingRenderer(config.context(), config.component().a$(CatalogComponents.Moldable.Mold.Heading.class), config.provider()));
		map.put(conceptOf(CatalogComponents.Moldable.Mold.Item.class), config -> new ItemRenderer(config.context(), config.component().a$(CatalogComponents.Moldable.Mold.Item.class), config.provider()));
		map.put(conceptOf(CatalogComponents.SearchBox.class), config -> new SearchBoxRenderer(config.context(), config.component().a$(CatalogComponents.SearchBox.class), config.provider()));
		map.put(conceptOf(Toolbar.class), config -> new ToolbarRenderer(config.context(), config.component().a$(Toolbar.class), config.provider()));
		map.put(conceptOf(Actionable.OpenPage.class), config -> new OpenPageRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.OpenSite.class), config -> new OpenSiteRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.SignText.class), config -> new SignTextRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.Authenticate.class), config -> new AuthenticateRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.SignDocument.class), config -> new SignDocumentRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.OpenDrawer.class), config -> new OpenDrawerRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.OpenLayer.class), config -> new OpenLayerRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.CloseLayer.class), config -> new CloseLayerRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.CloseDrawer.class), config -> new CloseDrawerRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.OpenBlock.class), config -> new OpenBlockRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.CloseBlock.class), config -> new CloseBlockRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.Download.class), config -> new DownloadRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.Export.class), config -> new ExportRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.OpenDialog.class), config -> new OpenDialogRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.OpenPopover.class), config -> new OpenPopoverRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.CloseDialog.class), config -> new CloseDialogRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.SelectNextItem.class), config -> new SelectNextItemRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.SelectPreviousItem.class), config -> new SelectPreviousItemRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.CopyToClipboard.class), config -> new CopyToClipboardRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.Action.class), config -> new ActionRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Actionable.class), config -> new ActionableRenderer(config.context(), config.component().a$(Actionable.class), config.provider()));
		map.put(conceptOf(Kpi.class), config -> new KpiRenderer(config.context(), config.component().a$(Kpi.class), config.provider()));
		map.put(conceptOf(Chat.class), config -> new ChatRenderer(config.context(), config.component().a$(Chat.class), config.provider()));
	}

	@SuppressWarnings({"unchecked"})
	public static <T extends UIRenderer> T renderer(CompilationContext context, Component component, RendererWriter provider) {
		return (T) map.entrySet().stream().filter(e -> component.i$(e.getKey())).map(e -> e.getValue().apply(config(context, component, provider))).findFirst().orElse(defaultRenderer(context, component, provider));
	}

	private static UIRenderer defaultRenderer(CompilationContext context, Component component, RendererWriter provider) {
		return new ComponentRenderer<>(context, component, provider);
	}

	private static Configuration config(CompilationContext context, Component component, RendererWriter provider) {
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
			public RendererWriter provider() {
				return provider;
			}
		};
	}

	public interface Configuration {
		CompilationContext context();

		Component component();

		RendererWriter provider();
	}

}
