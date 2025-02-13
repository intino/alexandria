package io.intino.alexandria.ui.documentation;

import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Parameter;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.documentation.model.actionable.*;
import io.intino.alexandria.ui.documentation.model.collection.*;
import io.intino.alexandria.ui.documentation.model.data.*;
import io.intino.alexandria.ui.documentation.model.other.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
	private static Map<WidgetType, Widget> map = new HashMap<>();

	public enum WidgetType {
		Text, Number, Image, File, Date, Location, Multiple,
		Chart, Block, List, Table, DynamicTable, Grid, Task, OpenPage, OpenBlock, Export,
		Download, DownloadSelection,
		Grouping, GroupingToolbar, Sorting, SearchBox, Map, Slider, Selector,
		Dashboard, AppDirectory, DigitalSignature, Dialog, Layer, Divider, User, Wizard,
		Frame, MicroSite, HtmlViewer, Timeline, Eventline, Reel, DateNavigator, DocumentEditor, Kpi, Chat;

		public static WidgetType from(String type) {
			WidgetType[] values = values();
			for (int i = 0; i< values.length; i++) {
				if (values[i].name().toLowerCase().equalsIgnoreCase(type)) return values[i];
			}
			return null;
		}
	}

	static {
		initialize();
	}

	private static void initialize() {
		map.put(WidgetType.Text, new TextWidget());
		map.put(WidgetType.Number, new NumberWidget());
		map.put(WidgetType.Image, new ImageWidget());
		map.put(WidgetType.File, new FileWidget());
		map.put(WidgetType.Date, new DateWidget());
		map.put(WidgetType.Multiple, new MultipleWidget());
		map.put(WidgetType.Location, new LocationWidget());
		map.put(WidgetType.Chart, new ChartWidget());
		map.put(WidgetType.Dashboard, new DashboardWidget());
		map.put(WidgetType.AppDirectory, new AppDirectoryWidget());
		map.put(WidgetType.DigitalSignature, new DigitalSignatureWidget());
		map.put(WidgetType.Block, new BlockWidget());
		map.put(WidgetType.List, new ListWidget());
		map.put(WidgetType.Map, new MapWidget());
		map.put(WidgetType.Slider, new SliderWidget());
		map.put(WidgetType.Selector, new SelectorWidget());
		map.put(WidgetType.Table, new TableWidget());
		map.put(WidgetType.DynamicTable, new DynamicTableWidget());
		map.put(WidgetType.Grid, new GridWidget());
		map.put(WidgetType.Task, new ActionWidget());
		map.put(WidgetType.OpenPage, new OpenPageWidget());
		map.put(WidgetType.OpenBlock, new OpenBlockWidget());
		map.put(WidgetType.Export, new ExportWidget());
		map.put(WidgetType.Download, new DownloadWidget());
		map.put(WidgetType.DownloadSelection, new DownloadSelectionWidget());
		map.put(WidgetType.Grouping, new GroupingWidget());
		map.put(WidgetType.GroupingToolbar, new GroupingToolbarWidget());
		map.put(WidgetType.Sorting, new SortingWidget());
		map.put(WidgetType.SearchBox, new SearchBoxWidget());
		map.put(WidgetType.Dialog, new DialogWidget());
		map.put(WidgetType.Layer, new LayerWidget());
		map.put(WidgetType.Divider, new DividerWidget());
		map.put(WidgetType.User, new UserWidget());
		map.put(WidgetType.Wizard, new WizardWidget());
		map.put(WidgetType.Frame, new FrameWidget());
		map.put(WidgetType.MicroSite, new MicroSiteWidget());
		map.put(WidgetType.HtmlViewer, new HtmlViewerWidget());
		map.put(WidgetType.DateNavigator, new DateNavigatorWidget());
		map.put(WidgetType.Timeline, new TimelineWidget());
		map.put(WidgetType.Eventline, new EventlineWidget());
		map.put(WidgetType.Reel, new ReelWidget());
		map.put(WidgetType.DocumentEditor, new DocumentEditorWidget());
		map.put(WidgetType.Kpi, new KpiWidget());
		map.put(WidgetType.Chat, new ChatWidget());
	}

	public static Widget widget(WidgetType type) {
		if (!map.containsKey(type)) return new Widget();
		return map.get(type);
	}

	public static Property property(String name, Property.Type type, String description, String... values) {
		Property result = new Property().name(name).type(type).description(description);
		if (values.length > 0) result.values(Arrays.asList(values));
		return result;
	}

	public static Method method(String name, List<Parameter> params, String description, String returnType) {
		return new Method().name(name).params(params).description(description).returnType(returnType);
	}

	public static Parameter methodParameter(String name, String type) {
		return new Parameter().name(name).type(type);
	}

}
