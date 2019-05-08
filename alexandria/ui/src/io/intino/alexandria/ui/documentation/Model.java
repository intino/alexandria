package io.intino.alexandria.ui.documentation;

import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.documentation.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
	private static Map<WidgetType, Widget> map = new HashMap<>();

	public enum WidgetType {
		Text, Number, Image, File, Date, Chart, Block, List, Table, Task, OpenPage, Export, Download, DownloadSelection,
		GroupBox;

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
		map.put(WidgetType.Chart, new ChartWidget());
		map.put(WidgetType.Block, new BlockWidget());
		map.put(WidgetType.List, new ListWidget());
		map.put(WidgetType.Table, new TableWidget());
		map.put(WidgetType.Task, new TaskWidget());
		map.put(WidgetType.OpenPage, new OpenPageWidget());
		map.put(WidgetType.Export, new ExportWidget());
		map.put(WidgetType.Download, new DownloadWidget());
		map.put(WidgetType.DownloadSelection, new DownloadSelectionWidget());
		map.put(WidgetType.GroupBox, new GroupBoxWidget());
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
