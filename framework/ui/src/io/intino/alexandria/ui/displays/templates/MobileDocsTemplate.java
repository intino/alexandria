package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.items.WidgetListMold;
import io.intino.alexandria.ui.documentation.DisplayHelper;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.Group;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

import java.util.*;
import java.util.stream.Collectors;

public class MobileDocsTemplate extends AbstractMobileDocsTemplate<AlexandriaUiBox> {

	public MobileDocsTemplate(AlexandriaUiBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		widgetList.onAddItem(this::refresh);
		widgetTypeSelector.onSelect(this::refreshWidgets);
		widgetStamp.onBack(e -> showCatalogBlock());
		//open(Model.widget(Model.WidgetType.Dialog));
		//open(Model.widget(Model.WidgetType.Selector));
		open(Model.widget(Model.WidgetType.Export));
		//open(Model.widget(Model.WidgetType.List));
		//widgetTypeSelector.select("operationOption");
	}

	private void refreshWidgets(SelectionEvent event) {
		refreshWidgets(!event.selection().isEmpty() ? (String) event.selection().get(0) : null);
	}

	private void refreshWidgets(String group) {
		if (!catalogBlock.isVisible()) showCatalogBlock();
		this.group.value(labelOf(group));
		widgetList.source(new WidgetGroupDatasource(group));
		widgetList.reload();
	}

	private static final Map<String, String> Labels = new HashMap<>() {{
		put("dataOption", "Data widgets");
		put("catalogOption", "Catalog widgets");
		put("operationOption", "Operation widgets");
		put("otherOption", "Other widgets");
	}};
	private String labelOf(String option) {
		return Labels.getOrDefault(translate(option), option);
	}

	private void refresh(AddItemEvent event) {
		Widget widget = event.item();
		WidgetListMold display = event.component();
		display.widgetListItem.onSelect(e -> open(widget));
		display.widgetListItem.item(widget);
		display.widgetListItem.refresh();
	}

	private void open(Widget widget) {
		showWidgetBlock();
		widgetStamp.mode(WidgetMold.Mode.Embedded);
		widgetStamp.item(widget);
	}

	private void showCatalogBlock() {
		catalogBlock.show();
		widgetBlock.hide();
	}

	private void showWidgetBlock() {
		catalogBlock.hide();
		widgetBlock.show();
	}

	private static class WidgetGroupDatasource extends PageDatasource<Widget> {
		private final String group;

		public WidgetGroupDatasource(String group) {
			this.group = group;
		}

		@Override
		public List<Widget> items(int start, int count, String condition, List<Filter> filters, List<String> sortings) {
			List<Widget> result = sort(load(condition, filters), sortings);
			int from = Math.min(start, result.size());
			int end = Math.min(start + count, result.size());
			return result.subList(from, end);
		}

		@Override
		public long itemCount(String condition, List<Filter> filters) {
			return load(condition, filters).size();
		}

		@Override
		public List<Group> groups(String key) {
			return Collections.emptyList();
		}

		private static final Map<String, List<Model.WidgetType>> Widgets = new HashMap<>() {{
			put("dataOption", List.of(Model.WidgetType.Text, Model.WidgetType.Number, Model.WidgetType.Image, Model.WidgetType.File, Model.WidgetType.Date, Model.WidgetType.Location, Model.WidgetType.Multiple, Model.WidgetType.DigitalSignature));
			put("catalogOption", List.of(Model.WidgetType.List, Model.WidgetType.Table, Model.WidgetType.DynamicTable, Model.WidgetType.Grid, Model.WidgetType.Map, Model.WidgetType.Grouping, Model.WidgetType.GroupingToolbar, Model.WidgetType.Sorting, Model.WidgetType.SearchBox));
			put("operationOption", List.of(Model.WidgetType.OpenPage, Model.WidgetType.Export, Model.WidgetType.Download, Model.WidgetType.DownloadSelection));
			put("otherOption", List.of(Model.WidgetType.Block, Model.WidgetType.Chart, Model.WidgetType.Dashboard, Model.WidgetType.AppDirectory, Model.WidgetType.Slider, Model.WidgetType.Dialog, Model.WidgetType.Layer, Model.WidgetType.Divider, Model.WidgetType.User, Model.WidgetType.Selector, Model.WidgetType.Stepper, Model.WidgetType.Frame, Model.WidgetType.MicroSite, Model.WidgetType.HtmlViewer, Model.WidgetType.DateNavigator, Model.WidgetType.Timeline, Model.WidgetType.Eventline, Model.WidgetType.Reel, Model.WidgetType.DocumentEditor, Model.WidgetType.Kpi));
		}};
		private List<Widget> load(String condition, List<Filter> filters) {
			return Widgets.getOrDefault(group, Collections.emptyList()).stream().map(Model::widget).collect(Collectors.toList());
		}

		private List<Widget> sort(List<Widget> widgetList, List<String> sortings) {
			widgetList.sort(Comparator.comparing(DisplayHelper::name));
			return widgetList;
		}

	}

}